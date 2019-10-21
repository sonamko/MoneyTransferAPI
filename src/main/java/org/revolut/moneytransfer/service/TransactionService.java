package org.revolut.moneytransfer.service;

import java.util.concurrent.ConcurrentHashMap;

import javax.ws.rs.core.Response;

import org.revolut.moneytransfer.constants.Constants;
import org.revolut.moneytransfer.dao.InMemoryDataStore;
import org.revolut.moneytransfer.model.CustomResponse;
import org.revolut.moneytransfer.model.Transaction;
import org.revolut.moneytransfer.model.UserAcount;

public class TransactionService {

	ConcurrentHashMap<Long, UserAcount> userMap = InMemoryDataStore.getMap();

	public CustomResponse depositMoneyToAccount(Transaction tx) {
		UserAcount useraccount = userMap.get(tx.getToUserId());
		if (useraccount != null) {
			useraccount.setBalance(tx.getAmount() + useraccount.getBalance());
			userMap.put(tx.getToUserId(), useraccount);
			return new CustomResponse(Constants.AMOUNT_DEPOSITED_SUCCESSFULLY,Response.Status.OK);
		} else {
			return new CustomResponse(Constants.USER_DOES_NOT_EXISTS,Response.Status.NOT_FOUND);
		}
	}

	public CustomResponse withDrawMoneyFromAccount(Transaction tx) {
		UserAcount useraccount = userMap.get(tx.getFromUserId());
		if (useraccount != null) {
			if (hasSufficientBalance(tx, useraccount)) {
				useraccount.setBalance(useraccount.getBalance() - tx.getAmount());
				userMap.put(tx.getFromUserId(), useraccount);
				return new CustomResponse(Constants.AMOUNT_WITHDRAWN_SUCCESSFULLY,Response.Status.OK);
			} else {
				return new CustomResponse(Constants.USER_DOES_NOT_HAVE_SUFFICIENT_BALANCE,Response.Status.BAD_REQUEST);
			}
		} else {
			return new CustomResponse(Constants.USER_DOES_NOT_EXISTS,Response.Status.NOT_FOUND);
		}
	}

	public CustomResponse transferFromAccount(Transaction tx) {
		UserAcount toUseraccount = userMap.get(tx.getToUserId());
		UserAcount fromUseraccount = userMap.get(tx.getFromUserId());
		if (toUseraccount != null && fromUseraccount != null) {
			if (hasSufficientBalance(tx, fromUseraccount)) {
				fromUseraccount.setBalance(fromUseraccount.getBalance() - tx.getAmount());
				userMap.put(tx.getFromUserId(), fromUseraccount);

				toUseraccount.setBalance(tx.getAmount() + toUseraccount.getBalance());
				userMap.put(tx.getToUserId(), toUseraccount);
				return new CustomResponse(Constants.AMOUNT_TRANSFERED_SUCCESSFULLY,Response.Status.OK);
			} else {
				return new CustomResponse(Constants.USER_DOES_NOT_HAVE_SUFFICIENT_BALANCE,Response.Status.BAD_REQUEST);
			}
		} else {
			return new CustomResponse(Constants.USERS_DOES_NOT_EXISTS,Response.Status.NOT_FOUND);
		}

	}

	private boolean hasSufficientBalance(Transaction tx, UserAcount fromUseraccount) {
		return tx.getAmount() <= fromUseraccount.getBalance();
	}

}
