package org.revolut.moneytransfer.service;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;

import org.revolut.moneytransfer.constants.Constants;
import org.revolut.moneytransfer.dao.InMemoryDataStore;
import org.revolut.moneytransfer.model.CustomResponse;
import org.revolut.moneytransfer.model.UserAcount;

public class UserAccountService {

	public List<UserAcount> getAllUserAccounts() {
		return new ArrayList<>(InMemoryDataStore.getMap().values());
	}

	public CustomResponse addUserAccount(UserAcount user) {
			user.setUserId((long) (InMemoryDataStore.getMap().size() + 1));
			InMemoryDataStore.getMap().put(user.getUserId(), user);
			return new CustomResponse(Constants.USER_CREATED_SUCCESFULLY, Response.Status.CREATED);
	}

	public CustomResponse getUserAccount(long userId) {
		UserAcount userAccount = InMemoryDataStore.getMap().get(userId);
		if (userAccount != null) {
			return new CustomResponse(userAccount, Response.Status.OK);
		} else {
			return new CustomResponse(Constants.USER_DOES_NOT_EXISTS, Response.Status.NOT_FOUND);
		}
	}

	public CustomResponse updateUserAccount(UserAcount user) {

		UserAcount userAccount = InMemoryDataStore.getMap().get(user.getUserId());
		if (userAccount != null) {
			InMemoryDataStore.getMap().put(user.getUserId(), user);
			return new CustomResponse(Constants.USER_ACCOUNT_UPDATED_SUCCESSFULLY, Response.Status.OK);
		} else {
			return new CustomResponse(Constants.USER_DOES_NOT_EXISTS, Response.Status.NOT_FOUND);
		}

	}

	public CustomResponse removeUserAccount(long userId) {
		UserAcount userAccount = InMemoryDataStore.getMap().get(userId);
		if (userAccount != null) {
			InMemoryDataStore.getMap().remove(userId);
			return new CustomResponse(Constants.USER_ACCOUNT_DELETED_SUCCESSFULLY, Response.Status.OK);
		} else {
			return new CustomResponse(Constants.USER_DOES_NOT_EXISTS, Response.Status.NOT_FOUND);
		}
	}

}
