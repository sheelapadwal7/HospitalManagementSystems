package com.hms.model;

import java.time.LocalDateTime;
import java.util.Date;

import com.hms.enums.LinkType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
@Data
@Entity
@Table(name = "token_log")
public class TokenLog {

	@Id
	@Column(name = "token_log_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "link_id")
	private int linkId;

	@Enumerated(EnumType.STRING)
	@Column(name = "link_type")
	private LinkType linkType;

	@Column(name = "user_name")
	private String userName;

	@Column(name = "token")
	private String token;

	@Column(name = "ip", length = 128)
	private String ip;

	@Column(name = "attempt")
	private int attempt;

	@Column(name = "is_valid")
	private boolean isValid;

	@Column(name = "created_at")
	private Date createdAt;

	@Column(name = "expiry_time")
	private LocalDateTime expiryTime;

	@Column(name = "logout_time")
	private LocalDateTime logoutTime;

}
