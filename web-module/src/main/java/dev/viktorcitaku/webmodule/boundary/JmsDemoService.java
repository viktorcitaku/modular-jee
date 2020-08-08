/*
 * MIT License
 *
 * Copyright (c) 2020 Viktor Citaku
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package dev.viktorcitaku.webmodule.boundary;

import dev.viktorcitaku.webmodule.config.SimpleQueue;
import dev.viktorcitaku.webmodule.config.XmlBasedSimpleQueue;
import java.util.logging.Logger;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.jms.JMSContext;
import javax.jms.Queue;
import javax.jms.Topic;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("jms")
@RequestScoped
public class JmsDemoService {

  private static final Logger LOGGER = Logger.getLogger(JmsDemoService.class.getName());

  @Inject JMSContext jmsContext;

  @Inject @SimpleQueue Queue queueDestination;

  @Inject @XmlBasedSimpleQueue Queue xmlBasedMdbDestination;

  @Inject Topic topicDestination;

  @POST
  @Path("queue")
  public Response sendMessageToQueue(@HeaderParam("message") String message) {
    LOGGER.info("sendMessageToQueue");
    jmsContext.createProducer().send(queueDestination, message);
    return Response.ok().build();
  }

  @POST
  @Path("queue/xml-descriptor")
  public Response sendMessageToQueueWithXmlBasedMdb(@HeaderParam("message") String message) {
    LOGGER.info("sendMessageToQueueWithXmlBasedMdb");
    jmsContext.createProducer().send(xmlBasedMdbDestination, message);
    return Response.ok().build();
  }

  @POST
  @Path("topic")
  public Response sendMessageToTopic(@HeaderParam("message") String message) {
    LOGGER.info("sendMessageToTopic");
    jmsContext.createProducer().send(topicDestination, message);
    return Response.ok().build();
  }
}
