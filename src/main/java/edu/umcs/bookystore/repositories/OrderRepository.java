package edu.umcs.bookystore.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.umcs.bookystore.entities.OrderEntity;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {

}
