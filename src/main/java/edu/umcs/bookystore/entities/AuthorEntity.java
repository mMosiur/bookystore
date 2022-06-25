package edu.umcs.bookystore.entities;

import java.util.Set;

import javax.persistence.*;

@Entity
@Table(name = "authors")
public class AuthorEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false, updatable = false)
	private Long id;

	@Column(name = "first_name", nullable = false)
	private String firstName;

	@Column(name = "last_name", nullable = false)
	private String lastName;

	@OneToMany(mappedBy = "author", fetch = FetchType.LAZY)
	private Set<BookEntity> books;

	public AuthorEntity() {
	}

	public AuthorEntity(String firstName, String lastName) {
		if (firstName == null || firstName.isBlank()) {
			throw new IllegalArgumentException("First name cannot be null or blank");
		}
		this.firstName = firstName;
		if (lastName == null || lastName.isBlank()) {
			throw new IllegalArgumentException("Last name cannot be null or blank");
		}
		this.lastName = lastName;
	}

	public Long getId() {
		return id;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	@Transient
	public String getFullName() {
		return String.format("%s %s", firstName, lastName);
	}

	public Set<BookEntity> getBooks() {
		return books;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

}
