/*
 * MIT License
 *
 * Copyright (c) 2017 Viktor Citaku
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

import dev.viktorcitaku.contract.Person;
import dev.viktorcitaku.contract.PersonDao;
import java.util.List;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("persons")
@RequestScoped
@Transactional
public class PersonService {

  // In Java 9 and above System.getLogger(...) will be used!
  // private static final System.Logger LOGGER = System.getLogger(Class.class.getName());
  private static final Logger LOGGER = Logger.getLogger(PersonService.class.getName());

  // No need to make this private
  @Inject PersonDao personDao;

  @PostConstruct
  protected void postConstruct() {
    LOGGER.info("PersonService @PostConstruct");
  }

  @PreDestroy
  protected void preDestroy() {
    LOGGER.info("PersonService @PreDestroy");
  }

  @GET
  @Path("ping")
  @Produces({MediaType.TEXT_PLAIN})
  public Response getPong() {
    return Response.ok().entity("pong").build();
  }

  @GET
  @Produces({MediaType.APPLICATION_JSON})
  public Response getPersons(@QueryParam("id") Long id) {
    if (id == null) {
      LOGGER.warning("ID is null!");
      List<Person> people = personDao.getPersons();
      if (people == null || people.isEmpty()) {
        LOGGER.warning("Persons are null or empty!");
        return Response.status(Response.Status.NO_CONTENT).build();
      }
      JsonArrayBuilder builder = Json.createArrayBuilder();
      people.stream().map(Person::toJson).forEach(builder::add);
      LOGGER.info("Persons are found!");
      return Response.ok().entity(builder.build()).build();
    } else {
      Person person = personDao.findById(id);
      if (person == null) {
        LOGGER.warning("No person found with given ID: " + id);
        return Response.status(Response.Status.NO_CONTENT).build();
      }
      LOGGER.info("Person: " + person);
      return Response.ok().entity(person).build();
    }
  }

  @POST
  @Consumes({MediaType.APPLICATION_JSON})
  public Response createPerson(Person person) {
    if (person == null) {
      LOGGER.warning("Person is null!");
      return Response.status(Response.Status.BAD_REQUEST).build();
    }
    LOGGER.info("Person: " + person);
    personDao.create((Person) person);
    return Response.status(Response.Status.CREATED).build();
  }

  @PUT
  @Produces({MediaType.APPLICATION_JSON})
  @Consumes({MediaType.APPLICATION_JSON})
  public Response updatePerson(Person person) {
    if (person == null) {
      LOGGER.warning("Person is null!");
      return Response.status(Response.Status.BAD_REQUEST).build();
    }
    Person updatedPerson = personDao.update(person);
    LOGGER.info("Person: " + updatedPerson);
    return Response.ok().entity(updatedPerson).build();
  }

  @DELETE
  @Consumes({MediaType.APPLICATION_JSON})
  public Response deletePerson(Person person, @QueryParam("id") Long id) {
    if (person == null && id == null) {
      LOGGER.warning("Person payload and also the id are null!");
      return Response.status(Response.Status.BAD_REQUEST).build();
    }
    Person personFound = personDao.findById(id);
    if (personFound == null) {
      LOGGER.warning("Person may not be deleted!");
      personDao.delete(person);
      return Response.status(Response.Status.ACCEPTED).build();
    }
    // Safe point, that we ensure user is really deleted!
    personDao.delete(personFound);
    LOGGER.info("Deleted Person: " + personFound);
    return Response.ok().build();
  }
}
