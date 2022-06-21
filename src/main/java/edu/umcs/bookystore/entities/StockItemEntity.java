package edu.umcs.bookystore.entities;

import javax.persistence.*;

@Entity
@Table(name = "stock_items")
public class StockItemEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false, updatable = false)
	private Long id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "book_id", nullable = true)
	private BookEntity book;

	public StockItemEntity() {
	}

	public StockItemEntity(BookEntity book) {
		this.book = book;
	}

	public Long getId() {
		return id;
	}

	public BookEntity getBook() {
		return book;
	}

	public void setBook(BookEntity book) {
		this.book = book;
	}

}
