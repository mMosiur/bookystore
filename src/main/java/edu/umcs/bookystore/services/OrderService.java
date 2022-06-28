package edu.umcs.bookystore.services;

import edu.umcs.bookystore.dtos.OrderDto;
import edu.umcs.bookystore.exceptions.OutOfStockException;

public interface OrderService {
	Long createOrder(OrderDto order) throws OutOfStockException;
}
