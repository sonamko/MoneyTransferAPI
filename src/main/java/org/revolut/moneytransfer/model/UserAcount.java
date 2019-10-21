package org.revolut.moneytransfer.model;

import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import javax.ws.rs.DefaultValue;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.validator.constraints.NotBlank;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class UserAcount {

	private long userId;
	@NotBlank(message = "User Name cannot be empty or null")
	private String userName;
	@DefaultValue(value = "0")
	@Min(value = 1)
	@Pattern(regexp = "[0-9]+", message = "Balance must be a valid number")
	private String balance;

	public UserAcount() {
	}

	public UserAcount(long userId, String userName, String balance) {
		super();
		this.userId = userId;
		this.userName = userName;
		this.balance = balance;
	}

	public UserAcount(String userName) {
		super();
		this.userName = userName;
	}

	public long getBalance() {
		return Long.parseLong(balance);
	}

	public void setBalance(Long balance) {
		this.balance = balance.toString();
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Override
	public String toString() {
		return "UserAcount [userId=" + userId + ", userName=" + userName + ", balance=" + balance + "]";
	}

}
