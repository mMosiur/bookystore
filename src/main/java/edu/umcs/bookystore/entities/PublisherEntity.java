package edu.umcs.bookystore.entities;

import java.util.Set;

import javax.persistence.*;

@Entity
@Table(name = "publishers")
public class PublisherEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false, updatable = false)
	private Long id;

	@Column(name = "name", nullable = false)
	private String name;

	@OneToMany(mappedBy = "publisher", fetch = FetchType.LAZY)
	private Set<BookEntity> books;

	public PublisherEntity() {
	}

	public PublisherEntity(String name) {
		if (name == null || name.isBlank()) {
			throw new IllegalArgumentException("Name cannot be null or blank");
		}
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Set<BookEntity> getBooks() {
		return books;
	}

	public void setName(String name) {
		if (name == null || name.isBlank()) {
			throw new IllegalArgumentException("Name cannot be null or blank");
		}
		this.name = name;
	}

}
