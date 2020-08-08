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

import dev.viktorcitaku.contract.User;
import dev.viktorcitaku.contract.UserDao;
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

@Path("users")
@RequestScoped
@Transactional
public class UserService {

  // In Java 9 and above System.getLogger(...) will be used!
  // private static final System.Logger LOGGER = System.getLogger(Class.class.getName());
  private static final Logger LOGGER = Logger.getLogger(UserService.class.getName());

  // No need to make this private
  @Inject UserDao userDao;

  @PostConstruct
  protected void postConstruct() {
    LOGGER.info("UserService @PostConstruct");
  }

  @PreDestroy
  protected void preDestroy() {
    LOGGER.info("UserService @PreDestroy");
  }

  @GET
  @Path("ping")
  @Produces({MediaType.TEXT_PLAIN})
  public Response getPong() {
    return Response.ok().entity("pong").build();
  }

  @GET
  @Produces({MediaType.APPLICATION_JSON})
  public Response getUsers(@QueryParam("id") Long id) {
    if (id == null) {
      LOGGER.warning("ID is null!");
      List<User> users = userDao.getUsers();
      if (users == null || users.isEmpty()) {
        LOGGER.warning("Users are null or empty!");
        return Response.status(Response.Status.NO_CONTENT).build();
      }
      JsonArrayBuilder builder = Json.createArrayBuilder();
      users.stream().map(User::toJson).forEach(builder::add);
      LOGGER.info("Users are found!");
      return Response.ok().entity(builder.build()).build();
    } else {
      User user = userDao.findById(id);
      if (user == null) {
        LOGGER.warning("No user found with given ID: " + id);
        return Response.status(Response.Status.NO_CONTENT).build();
      }
      LOGGER.info("User: " + user);
      return Response.ok().entity(user).build();
    }
  }

  @POST
  @Consumes({MediaType.APPLICATION_JSON})
  public Response createUser(User user) {
    if (user == null) {
      LOGGER.warning("User is null!");
      return Response.status(Response.Status.BAD_REQUEST).build();
    }
    LOGGER.info("User: " + user);
    userDao.create((User) user);
    return Response.status(Response.Status.CREATED).build();
  }

  @PUT
  @Produces({MediaType.APPLICATION_JSON})
  @Consumes({MediaType.APPLICATION_JSON})
  public Response updateUser(User user) {
    if (user == null) {
      LOGGER.warning("User is null!");
      return Response.status(Response.Status.BAD_REQUEST).build();
    }
    User updatedUser = userDao.update(user);
    LOGGER.info("User: " + updatedUser);
    return Response.ok().entity(updatedUser).build();
  }

  @DELETE
  @Consumes({MediaType.APPLICATION_JSON})
  public Response deleteUser(User user, @QueryParam("id") Long id) {
    if (user == null && id == null) {
      LOGGER.warning("User payload and also the id are null!");
      return Response.status(Response.Status.BAD_REQUEST).build();
    }
    User userFound = userDao.findById(id);
    if (userFound == null) {
      LOGGER.warning("User may not be deleted!");
      userDao.delete(user);
      return Response.status(Response.Status.ACCEPTED).build();
    }
    // Safe point, that we ensure user is really deleted!
    userDao.delete(userFound);
    LOGGER.info("Deleted User: " + userFound);
    return Response.ok().build();
  }
}
