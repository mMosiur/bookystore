package edu.umcs.bookystore.entities;

import java.util.Set;

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
	private double price;

	@OneToMany(mappedBy = "book", fetch = FetchType.LAZY)
	private Set<StockItemEntity> stock;

	public BookEntity() {
	}

	public BookEntity(String title, AuthorEntity author, PublisherEntity publisher, CategoryEntity category,
			double price) {
		this.title = title;
		this.author = author;
		this.publisher = publisher;
		this.category = category;
		this.price = price;
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

	public double getPrice() {
		return price;
	}

	public Set<StockItemEntity> getStock() {
		return stock;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setAuthor(AuthorEntity author) {
		this.author = author;
	}

	public void setPublisher(PublisherEntity publisher) {
		this.publisher = publisher;
	}

	public void setCategory(CategoryEntity category) {
		this.category = category;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public void setStock(Set<StockItemEntity> stock) {
		this.stock = stock;
	}

}
