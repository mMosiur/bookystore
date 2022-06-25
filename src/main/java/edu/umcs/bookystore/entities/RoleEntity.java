package edu.umcs.bookystore.entities;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "roles")
public class RoleEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false, updatable = false)
	private Long id;

	@Column(name = "name", nullable = false, unique = true)
	private String name;

	@Column(name = "description", nullable = true)
	private String description;

	@ManyToMany(mappedBy = "roles")
	private Set<UserEntity> users;

	// Constructors

	public RoleEntity() {
	}

	public RoleEntity(String name, String description) {
		if (name == null || name.isBlank()) {
			throw new IllegalArgumentException("Name cannot be null or blank");
		}
		this.name = name;
		this.description = description;
	}

	// Getters and setters

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		if (name == null || name.isBlank()) {
			throw new IllegalArgumentException("Name cannot be null or blank");
		}
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Set<UserEntity> getUsers() {
		return users;
	}
}
