package com.saturn.service.dto;

import com.saturn.config.Constants;

import com.saturn.domain.util.Role;
import com.saturn.domain.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.ZonedDateTime;

import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.*;

/**
 * A DTO representing a user
 */
public class UserDTO {

	public static final int PASSWORD_MIN_LENGTH = 4;
	public static final int PASSWORD_MAX_LENGTH = 50;

	private Long id;

	@Pattern(regexp = Constants.LOGIN_REGEX)
	@Size(min = 1, max = 50)
	private String login;

	@NotNull
	@Size(min = PASSWORD_MIN_LENGTH, max = PASSWORD_MAX_LENGTH)
	private String password;

	@Size(max = 50)
	private String firstName;

	@Size(max = 50)
	private String lastName;

	@Email
	@Size(min = 5, max = 100)
	private String email;

	private boolean activated = false;

	@Size(min = 2, max = 5)
	private String langKey;

	private Role role;

	private String createdBy;

	private ZonedDateTime createdDate;

	private String lastModifiedBy;

	private ZonedDateTime lastModifiedDate;

	public UserDTO() {
	}

	public UserDTO(User user) {
		this(user.getId(), user.getLogin(), null, user.getFirstName(), user.getLastName(), user.getEmail(),
			user.getActivated(), user.getLangKey(), user.getRole(), user.getCreatedBy(),
			user.getCreatedDate(), user.getLastModifiedBy(), user.getLastModifiedDate());
	}

	public UserDTO(Long id, String login, String password, String firstName, String lastName, String email,
		boolean activated, String langKey, Role role, String createdBy,
		ZonedDateTime createdDate, String lastModifiedBy, ZonedDateTime lastModifiedDate) {
		this.id = id;
		this.login = login;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.activated = activated;
		this.langKey = langKey;
		this.role = role;
		this.createdBy = createdBy;
		this.createdDate = createdDate;
		this.lastModifiedBy = lastModifiedBy;
		this.lastModifiedDate = lastModifiedDate;
	}

	public Long getId() {
		return id;
	}

	public String getLogin() {
		return login;
	}

	public String getPassword() {
		return password;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getEmail() {
		return email;
	}

	public boolean isActivated() {
		return activated;
	}

	public String getLangKey() {
		return langKey;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public ZonedDateTime getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(ZonedDateTime createdDate) {
		this.createdDate = createdDate;
	}

	public String getLastModifiedBy() {
		return lastModifiedBy;
	}

	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	public ZonedDateTime getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(ZonedDateTime lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	@Override
	public String toString() {
		return "UserDTO{"
			+ "login='" + login + '\''
			+ ", firstName='" + firstName + '\''
			+ ", lastName='" + lastName + '\''
			+ ", email='" + email + '\''
			+ ", activated=" + activated
			+ ", langKey='" + langKey + '\''
			+ ", role=" + role
			+ ", createdBy='" + createdBy + '\''
			+ ", createdDate=" + createdDate
			+ ", lastModifiedBy='" + lastModifiedBy + '\''
			+ ", lastModifiedDate=" + lastModifiedDate
			+ "}";
	}
}
