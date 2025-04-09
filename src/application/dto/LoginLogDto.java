package application.dto;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="login_logs")
public class LoginLogDto {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(name="user_id")
	private Long userId;
	@Column(name="login_time")
    private LocalDateTime loginTime;
	@Column(name="ip_address")
	private String ipAddress;
	@Column(name="was_successful")
	private Boolean wasSuccessful;
	
	public LoginLogDto(Long id, Long userId, LocalDateTime loginTime, String ipAddress, Boolean wasSuccessful) {
		this.id = id;
		this.userId = userId;
		this.loginTime = loginTime;
		this.ipAddress = ipAddress;
		this.wasSuccessful = wasSuccessful;
	}
	
	public LoginLogDto(LocalDateTime loginTime, String ipAddress, Boolean wasSuccessful) {
		this.loginTime = loginTime;
		this.ipAddress = ipAddress;
		this.wasSuccessful = wasSuccessful;
	}
	
	public LoginLogDto() {
	    //Hibernate
	}

	public Long getId() {
		return id;
	}

	public Long getUserId() {
		return userId;
	}

	public LocalDateTime getLoginTime() {
		return loginTime;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public Boolean getWasSuccessful() {
		return wasSuccessful;
	}
	
	public void setUserId(Long userId) {
	    this.userId = userId;
	}

	@Override
	public String toString() {
		return "LoginLogDto [id=" + id + ", userId=" + userId + ", loginTime=" + loginTime + ", ipAddress=" + ipAddress
				+ ", wasSuccessful=" + wasSuccessful + "]";
	}

}
