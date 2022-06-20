package edu.umcs.bookystore.entities;

import java.util.Set;

import javax.persistence.*;

@Entity
@Table(name = "categories")
public class CategoryEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false, updatable = false)
	private Long id;

	@Column(name = "name", nullable = false)
	private String name;

	@OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
	private Set<BookEntity> books;

	public CategoryEntity() {
	}

	public CategoryEntity(String name) {
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
		this.name = name;
	}

	public void setBooks(Set<BookEntity> books) {
		this.books = books;
	}

}
