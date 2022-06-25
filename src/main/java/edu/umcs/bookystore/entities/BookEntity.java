package edu.umcs.bookystore.entities;

import javax.persistence.*;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

@Entity
@Table(name = "books")
public class BookEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false, updatable = false)
	private Long id;

	@Column(name = "title", nullable = false)
	private String title;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "author_id", nullable = false)
	@Cascade(CascadeType.MERGE)
	private AuthorEntity author;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "publisher_id", nullable = false)
	@Cascade(CascadeType.MERGE)
	private PublisherEntity publisher;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "category_id", nullable = true)
	@Cascade(CascadeType.MERGE)
	private CategoryEntity category;

	@Column(name = "price", nullable = false, precision = 2)
	private Double price;

	@Column(name = "stock", nullable = false)
	private Integer stock;

	public BookEntity() {
	}

	public BookEntity(String title, AuthorEntity author, PublisherEntity publisher, CategoryEntity category,
			double price, int stock) {
		if (title == null || title.isBlank()) {
			throw new IllegalArgumentException("Title cannot be null or blank");
		}
		this.title = title.trim();
		if (author == null) {
			throw new IllegalArgumentException("Author cannot be null");
		}
		this.author = author;
		if (publisher == null) {
			throw new IllegalArgumentException("Publisher cannot be null");
		}
		this.publisher = publisher;
		if (category == null) {
			throw new IllegalArgumentException("Category cannot be null");
		}
		this.category = category;
		if (price < 0.00) {
			throw new IllegalArgumentException("Price cannot be negative");
		}
		this.price = price;
		if (stock < 0) {
			throw new IllegalArgumentException("Stock cannot be negative");
		}
		this.stock = stock;
	}

	public Long getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public AuthorEntity getAuthor() {
		return author;
	}

	public PublisherEntity getPublisher() {
		return publisher;
	}

	public CategoryEntity getCategory() {
		return category;
	}

	public Double getPrice() {
		return price;
	}

	public Integer getStock() {
		return stock;
	}

	public void setTitle(String title) {
		if (title == null || title.isBlank()) {
			throw new IllegalArgumentException("Title cannot be null or blank");
		}
		this.title = title.trim();
	}

	public void setAuthor(AuthorEntity author) {
		if (author == null) {
			throw new IllegalArgumentException("Author cannot be null");
		}
		this.author = author;
	}

	public void setPublisher(PublisherEntity publisher) {
		if (publisher == null) {
			throw new IllegalArgumentException("Publisher cannot be null");
		}
		this.publisher = publisher;
	}

	public void setCategory(CategoryEntity category) {
		if (category == null) {
			throw new IllegalArgumentException("Category cannot be null");
		}
		this.category = category;
	}

	public void setPrice(double price) {
		if (price < 0.00) {
			throw new IllegalArgumentException("Price cannot be negative");
		}
		this.price = price;
	}

	public void setStock(int stock) {
		if (stock < 0) {
			throw new IllegalArgumentException("Stock cannot be negative");
		}
		this.stock = stock;
	}

}
