package org.revolut.moneytransfer.resource;

import java.util.List;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.revolut.moneytransfer.model.CustomResponse;
import org.revolut.moneytransfer.model.UserAcount;
import org.revolut.moneytransfer.service.UserAccountService;

@Path("/userAccount")
public class UserAccountResource {

	private UserAccountService userAccService = new UserAccountService();

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllUserAccounts() {
		GenericEntity<List<UserAcount>> userList = new GenericEntity<List<UserAcount>>(
				userAccService.getAllUserAccounts()) {
		};
		return Response.status(Response.Status.OK).entity(userList).build();
	}

	@Path("/{userId}")
	@GET
	public Response getUser(@PathParam("userId") long userId) {
		CustomResponse response = userAccService.getUserAccount(userId);

		if (response.getResponse().equals(Response.Status.OK)) {
			return Response.status(response.getResponse()).entity(response.getObject()).type(MediaType.APPLICATION_JSON).build();
		} else {
			return Response.status(response.getResponse()).entity(response.getObject()).type(MediaType.TEXT_PLAIN).build();
		}
	}

	@POST
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addUser(@Valid UserAcount user) {
		CustomResponse response = userAccService.addUserAccount(user);
		return Response.status(response.getResponse()).entity(response.getObject()).build();
	}

	@PUT
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/{userId}")
	public Response updateUser(@PathParam("userId") Long userId, @Valid UserAcount account) {
		account.setUserId(userId);
		CustomResponse response = userAccService.updateUserAccount(account);
		return Response.status(response.getResponse()).entity(response.getObject()).build();
	}

	@DELETE
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/{userId}")
	public Response removeUser(@PathParam("userId") long userId) {
		CustomResponse response = userAccService.removeUserAccount(userId);
		return Response.status(response.getResponse()).entity(response.getObject()).build();
	}

}
