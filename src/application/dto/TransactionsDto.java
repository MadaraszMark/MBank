package application.dto;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="transactions")
public class TransactionsDto {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(name = "from_account")
	private Long fromAccount;
	@Column(name = "to_account")
	private Long toAccount;
	private Double amount;
	private String currency;
	@Column(name="timestamp")
	private LocalDateTime timeStamp;
	private String message;
	
	public TransactionsDto(Long id, Long fromAccount, Long toAccount, Double amount, String currency,
			LocalDateTime timeStamp) {
		this.id = id;
		this.fromAccount = fromAccount;
		this.toAccount = toAccount;
		this.amount = amount;
		this.currency = currency;
		this.timeStamp = timeStamp;
	}
	
	public TransactionsDto(Long fromAccount, Long toAccount, Double amount, String currency,
			LocalDateTime timeStamp) {
		this.fromAccount = fromAccount;
		this.toAccount = toAccount;
		this.amount = amount;
		this.currency = currency;
		this.timeStamp = timeStamp;
	}
	
	public TransactionsDto() {
        // Hibernate
    }
	
	public String getMessage() {
	    return message;
	}

	public void setMessage(String message) {
	    this.message = message;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setFromAccount(Long fromAccount) {
		this.fromAccount = fromAccount;
	}

	public void setToAccount(Long toAccount) {
		this.toAccount = toAccount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public void setTimeStamp(LocalDateTime timeStamp) {
		this.timeStamp = timeStamp;
	}

	public Long getId() {
		return id;
	}

	public Long getFromAccount() {
		return fromAccount;
	}

	public Long getToAccount() {
		return toAccount;
	}

	public Double getAmount() {
		return amount;
	}

	public String getCurrency() {
		return currency;
	}

	public LocalDateTime getTimeStamp() {
		return timeStamp;
	}

	@Override
	public String toString() {
		return "TransactionsDto [id=" + id + ", fromAccount=" + fromAccount + ", toAccount=" + toAccount + ", amount="
				+ amount + ", currency=" + currency + ", timeStamp=" + timeStamp + "]";
	}
}
