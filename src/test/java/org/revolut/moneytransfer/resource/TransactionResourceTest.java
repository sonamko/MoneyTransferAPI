package org.revolut.moneytransfer.resource;

import static org.junit.Assert.*;

import java.util.concurrent.ConcurrentHashMap;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.revolut.moneytransfer.constants.Constants;
import org.revolut.moneytransfer.dao.InMemoryDataStore;
import org.revolut.moneytransfer.model.Transaction;
import org.revolut.moneytransfer.model.UserAcount;

public class TransactionResourceTest extends JerseyTest {

	static ConcurrentHashMap<Long, UserAcount> map = InMemoryDataStore.getMap();

	@Override
	protected Application configure() {
		//forceSet(TestProperties.CONTAINER_PORT, "1");
		return new ResourceConfig(TransactionResource.class);
	}

	@Before
	public void setUp() throws Exception {
		super.setUp();
		map.put(100L, new UserAcount(100L, "Dummy User1", "100"));
		map.put(200L, new UserAcount(200L, "Dummy User2", "100"));
	}

	@After
	public void tearDown() throws Exception {
		super.tearDown();
		map.remove(100L);
		map.remove(200L);
	}

	@Test
	public void testDepositMoneyToAccountWhenUserExists() {
		Transaction tx = new Transaction(0, 100, "100");

		long balance = map.get(100L).getBalance();

		Response response = target("/transaction/deposit").request()
				.post(Entity.entity(tx, MediaType.APPLICATION_JSON_TYPE));

		assertEquals(Status.OK.getStatusCode(), response.getStatus());
		assertEquals(Constants.AMOUNT_DEPOSITED_SUCCESSFULLY, response.readEntity(String.class));
		assertEquals(MediaType.TEXT_PLAIN, response.getHeaderString(HttpHeaders.CONTENT_TYPE));
		assertEquals(balance + 100, map.get(100L).getBalance());

	}

	@Test
	public void testDepositMoneyToAccountWhenUserNotExists() {
		Transaction tx = new Transaction(0, 0, "100");
		Response response = target("/transaction/deposit").request()
				.post(Entity.entity(tx, MediaType.APPLICATION_JSON_TYPE));
		assertEquals(Status.NOT_FOUND.getStatusCode(), response.getStatus());
		assertEquals(Constants.USER_DOES_NOT_EXISTS, response.readEntity(String.class));
		assertEquals(MediaType.TEXT_PLAIN, response.getHeaderString(HttpHeaders.CONTENT_TYPE));

	}

	@Test
	public void testWithDrawMoneyFromAccountWhenUserExistsWithSufficientBalance() {
		Transaction tx = new Transaction(200, 0, "50");
		long balance = map.get(200L).getBalance();

		Response response = target("/transaction/withdraw").request()
				.post(Entity.entity(tx, MediaType.APPLICATION_JSON_TYPE));

		assertEquals(Status.OK.getStatusCode(), response.getStatus());
		assertEquals(Constants.AMOUNT_WITHDRAWN_SUCCESSFULLY, response.readEntity(String.class));
		assertEquals(MediaType.TEXT_PLAIN, response.getHeaderString(HttpHeaders.CONTENT_TYPE));
		assertEquals(balance - 50, map.get(200L).getBalance());
	}

	@Test
	public void testWithDrawMoneyFromAccountWhenUserExistsWithNoNSufficientBalance() {
		Transaction tx = new Transaction(200, 0, "500");
		long balance = map.get(200L).getBalance();

		Response response = target("/transaction/withdraw").request()
				.post(Entity.entity(tx, MediaType.APPLICATION_JSON_TYPE));

		assertEquals(Status.BAD_REQUEST.getStatusCode(), response.getStatus());
		assertEquals(Constants.USER_DOES_NOT_HAVE_SUFFICIENT_BALANCE, response.readEntity(String.class));
		assertEquals(MediaType.TEXT_PLAIN, response.getHeaderString(HttpHeaders.CONTENT_TYPE));
		assertEquals(balance, map.get(200L).getBalance());

	}

	@Test
	public void testWithDrawMoneyFromAccountWhenUserNotExists() {
		Transaction tx = new Transaction(0, 1, "100");
		Response response = target("/transaction/withdraw").request()
				.post(Entity.entity(tx, MediaType.APPLICATION_JSON_TYPE));
		assertEquals(Status.NOT_FOUND.getStatusCode(), response.getStatus());
		assertEquals(Constants.USER_DOES_NOT_EXISTS, response.readEntity(String.class));
		assertEquals(MediaType.TEXT_PLAIN, response.getHeaderString(HttpHeaders.CONTENT_TYPE));

	}

	@Test
	public void testTranferMoneyToAccountWhenToUserNotExists() {
		long fromUserBalance = map.get(100L).getBalance();
		Transaction tx = new Transaction(100, 0, "100");

		Response response = target("/transaction/transfer").request()
				.post(Entity.entity(tx, MediaType.APPLICATION_JSON_TYPE));

		assertEquals(Status.NOT_FOUND.getStatusCode(), response.getStatus());
		assertEquals(Constants.USERS_DOES_NOT_EXISTS, response.readEntity(String.class));
		assertEquals(MediaType.TEXT_PLAIN, response.getHeaderString(HttpHeaders.CONTENT_TYPE));
		assertEquals(fromUserBalance, map.get(100L).getBalance());

	}

	@Test
	public void testTranferMoneyToAccountWhenFromUserNotExists() {
		long toUserBalance = map.get(100L).getBalance();
		Transaction tx = new Transaction(0, 100, "100");

		Response response = target("/transaction/transfer").request()
				.post(Entity.entity(tx, MediaType.APPLICATION_JSON_TYPE));

		assertEquals(Status.NOT_FOUND.getStatusCode(), response.getStatus());
		assertEquals(Constants.USERS_DOES_NOT_EXISTS, response.readEntity(String.class));
		assertEquals(MediaType.TEXT_PLAIN, response.getHeaderString(HttpHeaders.CONTENT_TYPE));
		assertEquals(toUserBalance, map.get(100L).getBalance());

	}

	@Test
	public void testTranferMoneyToAccountWhenFromUserNotHasSuffincientBalance() {
		long toUserBalance = map.get(100L).getBalance();
		long fromUserBalance = map.get(200L).getBalance();
		Transaction tx = new Transaction(200, 100, "1000");

		Response response = target("/transaction/transfer").request()
				.post(Entity.entity(tx, MediaType.APPLICATION_JSON_TYPE));

		assertEquals(Status.BAD_REQUEST.getStatusCode(), response.getStatus());
		assertEquals(Constants.USER_DOES_NOT_HAVE_SUFFICIENT_BALANCE, response.readEntity(String.class));
		assertEquals(MediaType.TEXT_PLAIN, response.getHeaderString(HttpHeaders.CONTENT_TYPE));
		assertEquals(toUserBalance, map.get(100L).getBalance());
		assertEquals(fromUserBalance, map.get(200L).getBalance());

	}

	@Test
	public void testTranferMoneyToAccountWhenFromUserHasSuffincientBalance() {
		long toUserBalance = map.get(100L).getBalance();
		long fromUserBalance = map.get(200L).getBalance();
		Transaction tx = new Transaction(200, 100, "10");

		Response response = target("/transaction/transfer").request()
				.post(Entity.entity(tx, MediaType.APPLICATION_JSON_TYPE));

		assertEquals(Status.OK.getStatusCode(), response.getStatus());
		assertEquals(Constants.AMOUNT_TRANSFERED_SUCCESSFULLY, response.readEntity(String.class));
		assertEquals(MediaType.TEXT_PLAIN, response.getHeaderString(HttpHeaders.CONTENT_TYPE));
		assertEquals(toUserBalance + 10, map.get(100L).getBalance());
		assertEquals(fromUserBalance - 10, map.get(200L).getBalance());

	}

}
