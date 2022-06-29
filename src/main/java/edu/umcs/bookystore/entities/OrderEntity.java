package edu.umcs.bookystore.entities;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

@Entity
@Table(name = "orders")
public class OrderEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false, updatable = false)
	private Long id;

	@ManyToMany
	@JoinTable(name = "orders_books", joinColumns = @JoinColumn(name = "order_id"), inverseJoinColumns = @JoinColumn(name = "book_id"))
	private Set<BookEntity> books = new HashSet<>();

	@Column(name = "is_paid")
	private boolean isPaid = false;

	@Column(name = "is_completed")
	private boolean isCompleted = false;

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private UserEntity user;

	public OrderEntity() {
	}

	public Long getId() {
		return id;
	}

	public Set<BookEntity> getBooks() {
		return books;
	}

	public UserEntity getUser() {
		return user;
	}

	public void setUser(UserEntity user) {
		if (user == null) {
			throw new IllegalArgumentException("User cannot be null");
		}
		this.user = user;
	}

	public void addBook(BookEntity book) {
		if (book == null) {
			throw new IllegalArgumentException("Book cannot be null");
		}
		books.add(book);
	}

	public void removeBook(BookEntity book) {
		if (book == null) {
			throw new IllegalArgumentException("Book cannot be null");
		}
		books.remove(book);
	}

	public boolean isPaid() {
		return isPaid;
	}

	public void setIsPaid(boolean isPaid) {
		this.isPaid = isPaid;
	}

	public boolean isCompleted() {
		return isCompleted;
	}

	public void setIsCompleted(boolean isCompleted) {
		this.isCompleted = isCompleted;
	}

}
