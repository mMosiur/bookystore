package edu.umcs.bookystore.components.implementations;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import edu.umcs.bookystore.components.Cart;

@Component
@SessionScope
public class CartImpl implements Cart {

	private Set<Long> bookIds = new HashSet<>();

	public boolean add(Long bookId) {
		return bookIds.add(bookId);
	}

	public boolean remove(Long bookId) {
		return bookIds.remove(bookId);
	}

	public Set<Long> getBookIds() {
		return bookIds;
	}

	public void clear() {
		bookIds.clear();
	}

	public int getSize() {
		return bookIds.size();
	}

}
