package edu.umcs.bookystore.dtos;

import java.util.HashSet;
import java.util.Set;

public class OrderDto {

	private Long userId;
	private Set<Long> bookIds;

	public OrderDto() {
	}

	public OrderDto(Long userId) {
		this.userId = userId;
		this.bookIds = new HashSet<>();
	}

	public OrderDto(Long userId, Set<Long> bookIds) {
		this.userId = userId;
		this.bookIds = bookIds;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Set<Long> getBookIds() {
		return bookIds;
	}

	public void setBookIds(Set<Long> bookIds) {
		this.bookIds = bookIds;
	}

}
