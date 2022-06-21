package edu.umcs.bookystore.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.umcs.bookystore.entities.AuthorEntity;

public interface AuthorRepository extends JpaRepository<AuthorEntity, Long> {
	Optional<AuthorEntity> findByLastName(String lastName);
}
