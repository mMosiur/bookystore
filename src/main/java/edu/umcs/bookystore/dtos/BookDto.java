package edu.umcs.bookystore.dtos;

public class BookDto {

	private String title;
	private Long authorId;
	private Long publisherId;
	private Long categoryId;
	private Double price;
	private Integer stock;

	public BookDto() {
	}

	public BookDto(String title, Long authorId, Long publisherId,
			Long categoryId, Double price, Integer stock) {
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

	public Long getAuthorId() {
		return authorId;
	}

	public void setAuthorId(Long authorId) {
		this.authorId = authorId;
	}

	public Long getPublisherId() {
		return publisherId;
	}

	public void setPublisherId(Long publisherId) {
		this.publisherId = publisherId;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Integer getStock() {
		return stock;
	}

	public void setStock(Integer stock) {
		this.stock = stock;
	}

}
