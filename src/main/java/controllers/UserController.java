package controllers;

import model.User;
import service.UserService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Set;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserController {
    @Inject
    private UserService userService;

    @GET
    @Path("/authorization")
    public Response getUserByLoginAndPassword(User user) {
        User theUser = userService.getUserByLoginAndPassword(user.getUsername(), user.getPassword());
        return Response.ok(theUser).build();
    }

    @PUT
    @Path("/registration")
    public boolean registrationUser(User user) {
        if (userService.checkUniquenessUsername(user)) {
            userService.createUser(user);
            return true;
        }
        return false;
    }

    @GET
    public Response getUserList() {
        List<User> users = userService.findAll();
        return Response.ok(users).build();
    }


    @Path("/{id}")
    @PATCH  // Nie wiem jak to zrobić tak, by zmieniać tylko wybrane dane
    public void editUser(@PathParam("id") Integer id, User user) {
        User loadedUser = userService.findById(id);
        userService.createUser(user);
    }

    @GET
    @Path("/{id}")
    public Response getUserById(@PathParam("id") Integer id) {
        User user = userService.findById(id);
        return Response.ok(user).build();
    }


    @DELETE
    @Path("/{id}")
    public boolean deleteUser(@PathParam("id") Integer id) {
        try {
            userService.delete(id);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}