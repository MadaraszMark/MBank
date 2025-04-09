package application.dto;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="reports")
public class ReportsDto {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(name="user_id")
	private Long userId;
	private String message;
	@Column(name="submitted_at")
	private LocalDateTime submittedAt;
	
	public ReportsDto(Long id, Long userId, String message, LocalDateTime submittedAt) {
		this.id = id;
		this.userId = userId;
		this.message = message;
		this.submittedAt = submittedAt;
	}
	
	public ReportsDto(String message, LocalDateTime submittedAt) {
		this.message = message;
		this.submittedAt = submittedAt;
	}
	
	public ReportsDto() {
		// Hibernate
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setSubmittedAt(LocalDateTime submittedAt) {
		this.submittedAt = submittedAt;
	}

	public Long getId() {
		return id;
	}

	public Long getUserId() {
		return userId;
	}

	public String getMessage() {
		return message;
	}

	public LocalDateTime getSubmittedAt() {
		return submittedAt;
	}

}
