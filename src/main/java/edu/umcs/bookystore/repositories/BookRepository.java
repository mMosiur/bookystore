package edu.umcs.bookystore.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.umcs.bookystore.entities.BookEntity;

public interface BookRepository extends JpaRepository<BookEntity, Long> {
	Optional<BookEntity> findByTitle(String title);
}
