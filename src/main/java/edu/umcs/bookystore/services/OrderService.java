package edu.umcs.bookystore.services;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import edu.umcs.bookystore.dtos.CreateOrderDto;
import edu.umcs.bookystore.entities.OrderEntity;
import edu.umcs.bookystore.exceptions.OutOfStockException;

public interface OrderService {
	Long createOrder(CreateOrderDto order) throws OutOfStockException;
	List<OrderEntity> getAllOrders();
	Set<OrderEntity> getOrdersOfUser(String username) throws NoSuchElementException;
	OrderEntity getOrderById(Long id) throws NoSuchElementException;
	void commit();
}
