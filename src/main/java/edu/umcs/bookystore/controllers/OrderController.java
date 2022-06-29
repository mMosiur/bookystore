package edu.umcs.bookystore.controllers;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import edu.umcs.bookystore.components.Cart;
import edu.umcs.bookystore.dtos.CreateOrderDto;
import edu.umcs.bookystore.dtos.OrderDto;
import edu.umcs.bookystore.dtos.UserInfoDto;
import edu.umcs.bookystore.entities.BookEntity;
import edu.umcs.bookystore.entities.OrderEntity;
import edu.umcs.bookystore.entities.UserEntity;
import edu.umcs.bookystore.exceptions.OutOfStockException;
import edu.umcs.bookystore.repositories.BookRepository;
import edu.umcs.bookystore.repositories.UserRepository;
import edu.umcs.bookystore.services.OrderService;

@Controller
@RequestMapping("/orders")
public class OrderController {

	private static final String TEMPLATES_DIRECTORY = "orders";
	private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

	private Cart cart;
	private UserRepository userRepository;
	private BookRepository bookRepository;
	private OrderService orderService;

	public OrderController(Cart cart, UserRepository userRepository, BookRepository bookRepository,
			OrderService orderService) {
		this.cart = cart;
		this.userRepository = userRepository;
		this.bookRepository = bookRepository;
		this.orderService = orderService;
	}

	private String template(String name) {
		return String.format("%s/%s", TEMPLATES_DIRECTORY, name);
	}

	@GetMapping("/list")
	public String getOrderList(Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Set<OrderEntity> orders = orderService.getOrdersOfUser(auth.getName());
		List<OrderDto> ordersCompleted = orders.stream()
				.filter(o -> o.isCompleted())
				.map(o -> getOrderDtoFromEntity(o))
				.collect(Collectors.toList());
		List<OrderDto> ordersInProgress = orders.stream()
				.filter(o -> !o.isCompleted())
				.map(o -> getOrderDtoFromEntity(o))
				.collect(Collectors.toList());
		model.addAttribute("ordersCompleted", ordersCompleted);
		model.addAttribute("ordersInProgress", ordersInProgress);
		return template("list");
	}

	private static OrderDto getOrderDtoFromEntity(OrderEntity orderEntity) {
		UserEntity user = orderEntity.getUser();
		UserInfoDto userInfoDto = new UserInfoDto(
				user.getEmail(),
				user.getFirstName(),
				user.getLastName());
		Set<BookEntity> books = orderEntity.getBooks();
		Set<String> booksNames = books.stream().map(b -> b.getTitle()).collect(Collectors.toSet());
		return new OrderDto(orderEntity.getId(), userInfoDto, orderEntity.isCompleted(), orderEntity.isPaid(),
				booksNames);
	}

	@PostMapping("/create")
	public String postCreateOrder(Model model) {
		logger.debug("POST order endpoint called");
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (!auth.isAuthenticated() || "anonymousUser".equals(auth.getName())) {
			return "redirect:/user/login";
		}
		UserEntity user = userRepository.findByUsername(auth.getName()).get();
		CreateOrderDto order = new CreateOrderDto(user.getId(), cart.getBookIds());
		try {
			Long orderId = this.orderService.createOrder(order);
			cart.clear();
			model.addAttribute("successMessage", "Order created successfully.");
			return String.format("redirect:/orders/%d/details", orderId);
		} catch (OutOfStockException e) {
			BookEntity book = bookRepository.findById(e.getBookId()).get();
			String errorMessage = String.format("The book \"%s\" is out of stock.", book.getTitle());
			model.addAttribute("errorMessage", errorMessage);
			return "redirect:/cart";
		}
	}

	@GetMapping("/manage")
	public String getManageOrders(Model model) {
		logger.debug("GET manage orders endpoint called");
		List<OrderEntity> orders = orderService.getAllOrders();
		List<OrderDto> ordersCompleted = orders.stream()
				.filter(o -> o.isCompleted())
				.map(o -> getOrderDtoFromEntity(o))
				.collect(Collectors.toList());
		List<OrderDto> ordersInProgress = orders.stream()
				.filter(o -> !o.isCompleted())
				.map(o -> getOrderDtoFromEntity(o))
				.collect(Collectors.toList());
		model.addAttribute("ordersCompleted", ordersCompleted);
		model.addAttribute("ordersInProgress", ordersInProgress);
		return template("manage");
	}

	@PostMapping("/manage/complete")
	public String postManageOrdersComplete(Model model, @RequestParam("id") long id) {
		logger.debug("POST manage orders complete endpoint called");
		try {
			OrderEntity order = this.orderService.getOrderById(id);
			if (order.isCompleted()) {
				model.addAttribute("errorMessage", "Order is already completed.");
			} else {
				order.setIsCompleted(true);
				this.orderService.commit();
				model.addAttribute("successMessage", "Order completed successfully.");
			}
		} catch (NoSuchElementException e) {
			String errorMessage = String.format("Order with id %d does not exist.", id);
			model.addAttribute("errorMessage", errorMessage);
		}
		return getOrderDetails(model, id);
	}

	@PostMapping("/manage/incomplete")
	public String postManageOrdersIncomplete(Model model, @RequestParam("id") long id) {
		logger.debug("POST manage orders incomplete endpoint called");
		try {
			OrderEntity order = this.orderService.getOrderById(id);
			if (!order.isCompleted()) {
				model.addAttribute("errorMessage", "Order is already marked not completed.");
			} else {
				order.setIsCompleted(false);
				this.orderService.commit();
				model.addAttribute("successMessage", "Order marked incomplete successfully.");
			}
		} catch (NoSuchElementException e) {
			String errorMessage = String.format("Order with id %d does not exist.", id);
			model.addAttribute("errorMessage", errorMessage);
		}
		return getOrderDetails(model, id);
	}

	@GetMapping("/{id}/details")
	public String getOrderDetails(Model model, @PathVariable("id") Long id) {
		logger.debug("GET order details endpoint called");
		try {
			OrderEntity order = orderService.getOrderById(id);
			OrderDto orderDto = getOrderDtoFromEntity(order);
			model.addAttribute("order", orderDto);
		} catch (NoSuchElementException e) {
			model.addAttribute("errorMessage", "Order not found.");
		}
		return template("details");
	}

}
