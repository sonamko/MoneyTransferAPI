package org.revolut.moneytransfer.resource;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import java.util.concurrent.ConcurrentHashMap;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.revolut.moneytransfer.constants.Constants;
import org.revolut.moneytransfer.dao.InMemoryDataStore;
import org.revolut.moneytransfer.model.UserAcount;

public class UserAccountResourceTest extends JerseyTest {

	static ConcurrentHashMap<Long, UserAcount> map = InMemoryDataStore.getMap();

	@Before
	public void setUp() throws Exception {
		super.setUp();
		map.put(100L, new UserAcount(100L, "Dummy User1", "1000"));
	}

	@After
	public void tearDown() throws Exception {
		super.tearDown();
		map.remove(100L);
	}

	@Override
	protected Application configure() {
		//forceSet(TestProperties.CONTAINER_PORT, "0");
		return new ResourceConfig(UserAccountResource.class);
	}

	@Test
	public void testGetAllUserAccounts() {
		Response response = target("/userAccount").request().get();

		assertEquals(Status.OK.getStatusCode(), response.getStatus());
		assertEquals(MediaType.APPLICATION_JSON, response.getHeaderString(HttpHeaders.CONTENT_TYPE));
	}

	@Test
	public void testAddUser() {
		UserAcount userAccount = new UserAcount("TESTUSER");
		Response response = target("/userAccount").request()
				.post(Entity.entity(userAccount, MediaType.APPLICATION_JSON));
		assertEquals(Status.CREATED.getStatusCode(), response.getStatus());
		assertEquals(Constants.USER_CREATED_SUCCESFULLY, response.readEntity(String.class));
		assertEquals(MediaType.TEXT_PLAIN, response.getHeaderString(HttpHeaders.CONTENT_TYPE));

	}

	@Test
	public void testGetUserWhenUserExist() {
		final UserAcount userAcount = target("/userAccount/100").request().get(UserAcount.class);

		assertEquals(100L, userAcount.getUserId());
		assertEquals("Dummy User1", userAcount.getUserName());
		assertEquals(1000L, userAcount.getBalance());

	}
	
	@Test
	public void testGetUserWhenUserDoesNotExist() {
		Response response = target("/userAccount/0").request().get();

		assertEquals(Status.NOT_FOUND.getStatusCode(), response.getStatus());
		assertEquals(Constants.USER_DOES_NOT_EXISTS, response.readEntity(String.class));;

	}

	@Test
	public void testUpdateUserWhenUserExists() {
		UserAcount userAccount = new UserAcount(100, "TESTUSER NEW ", "100");

		Response response = target("/userAccount/100").request()
				.put(Entity.entity(userAccount, MediaType.APPLICATION_JSON));

		assertEquals(Status.OK.getStatusCode(), response.getStatus());
		assertEquals(Constants.USER_ACCOUNT_UPDATED_SUCCESSFULLY, response.readEntity(String.class));
		assertEquals(MediaType.TEXT_PLAIN, response.getHeaderString(HttpHeaders.CONTENT_TYPE));
		assertEquals("TESTUSER NEW ", map.get(100L).getUserName());
	}

	@Test
	public void testUpdateUserWhenUserDoestNotExists() {
		UserAcount userAccount = new UserAcount(0, "TESTUSER DUMMY ","100");

		Response response = target("/userAccount/0").request()
				.put(Entity.entity(userAccount, MediaType.APPLICATION_JSON));

		assertEquals(Status.NOT_FOUND.getStatusCode(), response.getStatus());
		assertEquals(Constants.USER_DOES_NOT_EXISTS, response.readEntity(String.class));
		assertEquals(MediaType.TEXT_PLAIN, response.getHeaderString(HttpHeaders.CONTENT_TYPE));
	}

	@Test
	public void testRemoveUserWhenUserExists() {
		Response response = target("/userAccount/100").request().delete();

		assertEquals(Status.OK.getStatusCode(), response.getStatus());
		assertEquals(Constants.USER_ACCOUNT_DELETED_SUCCESSFULLY, response.readEntity(String.class));
		assertEquals(MediaType.TEXT_PLAIN, response.getHeaderString(HttpHeaders.CONTENT_TYPE));
		assertNull(map.get(100L));
	}

	@Test
	public void testRemoveUserWhenUserNotExists() {
		Response response = target("/userAccount/0").request().delete();

		assertEquals(Status.NOT_FOUND.getStatusCode(), response.getStatus());
		assertEquals(Constants.USER_DOES_NOT_EXISTS, response.readEntity(String.class));
		assertEquals(MediaType.TEXT_PLAIN, response.getHeaderString(HttpHeaders.CONTENT_TYPE));

	}

}
