package edu.umcs.bookystore.entities;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

@Entity
@Table(name = "users")
public class UserEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false, updatable = false)
	private Long id;

	@Column(name = "username", nullable = false, unique = true)
	private String username;

	@Column(name = "first_name", nullable = true)
	private String firstName;

	@Column(name = "last_name", nullable = true)
	private String lastName;

	@Column(name = "email", nullable = false, unique = true)
	private String email;

	@Column(name = "password_hash", nullable = false)
	private String passwordHash;

	@ManyToMany(cascade = { CascadeType.MERGE }, fetch = FetchType.EAGER)
	@JoinTable(name = "users_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<RoleEntity> roles;

	// Getters and setters

	public UserEntity() {
		this.roles = new HashSet<>();
	}

	public UserEntity(String username, String firstName, String lastName, String email, String passwordHash) {
		this(username, firstName, lastName, email, passwordHash, new HashSet<>());
	}

	public UserEntity(String username, String firstName, String lastName, String email, String passwordHash,
			Set<RoleEntity> roles) {
		if (username == null || username.isBlank()) {
			throw new IllegalArgumentException("Username cannot be null or blank");
		}
		this.username = username;
		if (firstName == null || firstName.isBlank()) {
			throw new IllegalArgumentException("First name cannot be null or blank");
		}
		this.firstName = firstName;
		if (lastName == null || lastName.isBlank()) {
			throw new IllegalArgumentException("Last name cannot be null or blank");
		}
		this.lastName = lastName;
		if (email == null || email.isBlank()) {
			throw new IllegalArgumentException("Email cannot be null or blank");
		}
		this.email = email;
		if (passwordHash == null || passwordHash.isBlank()) {
			throw new IllegalArgumentException("Password hash cannot be null or blank");
		}
		this.passwordHash = passwordHash;
		this.roles = roles;
	}

	public Long getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		if (username == null || username.isBlank()) {
			throw new IllegalArgumentException("Username cannot be null or blank");
		}
		this.username = username;
	}

	public String getPasswordHash() {
		return passwordHash;
	}

	public void setPasswordHash(String passwordHash) {
		if (passwordHash == null || passwordHash.isBlank()) {
			throw new IllegalArgumentException("Password hash cannot be null or blank");
		}
		this.passwordHash = passwordHash;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		if (email == null || email.isBlank()) {
			throw new IllegalArgumentException("Email cannot be null or blank");
		}
		this.email = email;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		if (firstName == null || firstName.isBlank()) {
			throw new IllegalArgumentException("First name cannot be null or blank");
		}
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		if (lastName == null || lastName.isBlank()) {
			throw new IllegalArgumentException("Last name cannot be null or blank");
		}
		this.lastName = lastName;
	}

	public Set<RoleEntity> getRoles() {
		return roles;
	}

	public void setRoles(Set<RoleEntity> roles) {
		this.roles = roles;
	}

	public void addRole(RoleEntity role) {
		if (this.roles == null) {
			this.roles = new HashSet<>();
		}
		this.roles.add(role);
	}

}
