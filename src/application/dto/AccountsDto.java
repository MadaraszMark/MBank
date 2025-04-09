package application.dto;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="accounts")
public class AccountsDto {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(name="user_id")
	private Long userId;
	@Column(name="account_number")
	private String accountNumber;
	@Column(name="account_type")
	private String accountType;
    private Double balance;
    private String currency;
    private String status;
    @Column(name="created_at")
	private LocalDateTime createdAt;
    @Column(name = "card_number")
    private String cardNumber;
    
	public AccountsDto(Long id, Long userId, String accountNumber, String accountType, Double balance, String currency,
			String status, LocalDateTime createdAt, String cardNumber) {
		this.id = id;
		this.userId = userId;
		this.accountNumber = accountNumber;
		this.accountType = accountType;
		this.balance = balance;
		this.currency = currency;
		this.status = status;
		this.createdAt = createdAt;
		this.cardNumber = cardNumber;
	}
	
	public AccountsDto(String accountNumber, String accountType, Double balance, String currency,
			String status, LocalDateTime createdAt, String cardNumber) {
		this.accountNumber = accountNumber;
		this.accountType = accountType;
		this.balance = balance;
		this.currency = currency;
		this.status = status;
		this.createdAt = createdAt;
		this.cardNumber = cardNumber;
	}
	
	public AccountsDto() {} // Hibernate
	
	public String getStatusText() {
		String statusText = "";
		switch (status) {
		    case "active": statusText = "Aktív";
		    break;
		    case "inactive": statusText = "Inaktív";
		    break;
		}
		return statusText;
	}
	
	public String getAccountTypeText() {
		String accountTypeTex = "";
		switch (accountType) {
		    case "checking": accountTypeTex = "Folyószámla";
		}
		return accountTypeTex;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public Long getId() {
		return id;
	}

	public Long getUserId() {
		return userId;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public String getAccountType() {
		return accountType;
	}

	public Double getBalance() {
		return balance;
	}

	public String getCurrency() {
		return currency;
	}

	public String getStatus() {
		return status;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
	
	public String getCardNumber() {
	    return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
	    this.cardNumber = cardNumber;
	}

	@Override
	public String toString() {
		return "AccountsDto [id=" + id + ", userId=" + userId + ", accountNumber=" + accountNumber + ", accountType="
				+ accountType + ", balance=" + balance + ", currency=" + currency + ", status=" + status
				+ ", createdAt=" + createdAt + "]";
	}

}
