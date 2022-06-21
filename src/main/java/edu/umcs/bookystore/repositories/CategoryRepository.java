package edu.umcs.bookystore.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.umcs.bookystore.entities.CategoryEntity;

public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {
	Optional<CategoryEntity> findByName(String name);
}
