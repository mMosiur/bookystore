package edu.umcs.bookystore.components;

import java.util.Set;

public interface Cart {

	boolean add(Long bookId);

	boolean remove(Long bookId);

	Set<Long> getBookIds();

	void clear();

	int getSize();

}
