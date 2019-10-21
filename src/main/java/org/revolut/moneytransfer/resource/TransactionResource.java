package org.revolut.moneytransfer.resource;

import javax.validation.Valid;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.revolut.moneytransfer.model.CustomResponse;
import org.revolut.moneytransfer.model.Transaction;
import org.revolut.moneytransfer.service.TransactionService;

@Path("/transaction")
public class TransactionResource {

	private TransactionService txService = new TransactionService();

	@POST
	@Path("/deposit")
	@Produces(MediaType.TEXT_PLAIN)
	public Response depositMoneyToAccount(@Valid Transaction tx) {
		CustomResponse response = txService.depositMoneyToAccount(tx);
		return Response.status(response.getResponse()).entity(response.getObject()).build();
	}

	@POST
	@Path("/withdraw")
	@Produces(MediaType.TEXT_PLAIN)
	public Response withDrawMoneyFromAccount(@Valid Transaction tx) {
		CustomResponse response = txService.withDrawMoneyFromAccount(tx);
		return Response.status(response.getResponse()).entity(response.getObject()).build();
	}

	@POST
	@Path("/transfer")
	@Produces(MediaType.TEXT_PLAIN)
	public Response transferFromAccount(@Valid Transaction tx) {
		CustomResponse response = txService.transferFromAccount(tx);
		return Response.status(response.getResponse()).entity(response.getObject()).build();
	}

}
