package edu.umcs.bookystore.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.umcs.bookystore.entities.PublisherEntity;

public interface PublisherRepository extends JpaRepository<PublisherEntity, Long> {
	Optional<PublisherEntity> findByName(String name);
}
