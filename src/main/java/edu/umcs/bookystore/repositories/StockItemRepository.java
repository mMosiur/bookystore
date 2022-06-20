package edu.umcs.bookystore.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.umcs.bookystore.entities.StockItemEntity;

public interface StockItemRepository extends JpaRepository<StockItemEntity, Long> {
}
