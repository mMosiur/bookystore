package edu.umcs.bookystore.dtos;

public class BookDto {
	private String title;
	private long authorId;
	private long publisherId;
	private long categoryId;
	private double price;
	private int stock;

	public BookDto() {
	}

	public BookDto(String title, long authorId, long publisherId,
			long categoryId, double price, int stock) {
		this.title = title;
		this.authorId = authorId;
		this.publisherId = publisherId;
		this.categoryId = categoryId;
		this.price = price;
		this.stock = stock;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public long getAuthorId() {
		return authorId;
	}

	public void setAuthorId(long authorId) {
		this.authorId = authorId;
	}

	public long getPublisherId() {
		return publisherId;
	}

	public void setPublisherId(long publisherId) {
		this.publisherId = publisherId;
	}

	public long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(long categoryId) {
		this.categoryId = categoryId;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}
}
