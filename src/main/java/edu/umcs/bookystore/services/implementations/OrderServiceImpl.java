package edu.umcs.bookystore.services.implementations;

import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Service;

import edu.umcs.bookystore.dtos.CreateOrderDto;
import edu.umcs.bookystore.entities.BookEntity;
import edu.umcs.bookystore.entities.OrderEntity;
import edu.umcs.bookystore.entities.UserEntity;
import edu.umcs.bookystore.exceptions.OutOfStockException;
import edu.umcs.bookystore.repositories.BookRepository;
import edu.umcs.bookystore.repositories.OrderRepository;
import edu.umcs.bookystore.repositories.UserRepository;
import edu.umcs.bookystore.services.OrderService;

@Service
public class OrderServiceImpl implements OrderService {

	private UserRepository userRepository;
	private BookRepository bookRepository;
	private OrderRepository orderRepository;

	public OrderServiceImpl(UserRepository userRepository, BookRepository bookRepository,
			OrderRepository orderRepository) {
		this.userRepository = userRepository;
		this.bookRepository = bookRepository;
		this.orderRepository = orderRepository;
	}

	public Long createOrder(CreateOrderDto order) throws OutOfStockException {
		OrderEntity orderEntity = new OrderEntity();
		UserEntity user = userRepository.findById(order.getUserId())
				.orElseThrow(() -> new IllegalArgumentException("User with id " + order.getUserId() + " not found"));
		orderEntity.setUser(user);
		Set<BookEntity> books = new HashSet<>();
		for (Long bookId : order.getBookIds()) {
			BookEntity book = bookRepository.findById(bookId)
					.orElseThrow(() -> new IllegalArgumentException("Book with id " + bookId + " not found"));
			int stock = book.getStock();
			if (stock < 1) {
				throw new OutOfStockException("Book out of stock", bookId);
			}
			books.add(book);
			book.setStock(stock - 1);
		}
		if (books.size() != order.getBookIds().size()) {
			throw new IllegalArgumentException("Some books not found");
		}
		orderEntity.getBooks().addAll(books);
		orderEntity = orderRepository.save(orderEntity);
		return orderEntity.getId();
	}

	public List<OrderEntity> getAllOrders() {
		return orderRepository.findAll();
	}

	public Set<OrderEntity> getOrdersOfUser(String username) throws NoSuchElementException {
		UserEntity user = userRepository.findByUsername(username).get();
		return user.getOrders();
	}

	public OrderEntity getOrderById(Long orderId) throws NoSuchElementException {
		Optional<OrderEntity> order = orderRepository.findById(orderId);
		return order.get();
	}

	public void commit() {
		this.orderRepository.flush();
	}

}
