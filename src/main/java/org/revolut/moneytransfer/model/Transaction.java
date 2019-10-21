package org.revolut.moneytransfer.model;

import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.validator.constraints.NotBlank;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Transaction {

	private long fromUserId;
	private long toUserId;
	@Min(value = 1, message = "Amount should be greater than 0")
	@NotBlank(message = "Amount should not be empty")
	@Pattern(regexp = "[0-9]+", message = "Balance must be a valid number")
	private String amount;

	public Transaction() {

	}

	public Transaction(long fromUserId, long toUserId, String amount) {
		super();
		this.fromUserId = fromUserId;
		this.toUserId = toUserId;
		this.amount = amount;
	}

	public long getFromUserId() {
		return fromUserId;
	}

	public void setFromUserId(long fromUserId) {
		this.fromUserId = fromUserId;
	}

	public void setAmount(Long amount) {
		this.amount = amount.toString();
	}

	public long getToUserId() {
		return toUserId;
	}

	public void setToUserId(long toUserId) {
		this.toUserId = toUserId;
	}

	public long getAmount() {
		return Long.parseLong(amount);
	}

	@Override
	public String toString() {
		return "Transaction [fromUserId=" + fromUserId + ", toUserId=" + toUserId + ", amount=" + amount + "]";
	}

}
