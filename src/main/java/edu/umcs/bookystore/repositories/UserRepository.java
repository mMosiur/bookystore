package edu.umcs.bookystore.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.umcs.bookystore.entities.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
	Optional<UserEntity> findByUsername(String username);
	Optional<UserEntity> findByEmail(String email);
}
