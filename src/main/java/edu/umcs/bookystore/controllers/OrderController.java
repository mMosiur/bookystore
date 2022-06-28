package edu.umcs.bookystore.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.umcs.bookystore.components.Cart;
import edu.umcs.bookystore.dtos.OrderDto;
import edu.umcs.bookystore.entities.BookEntity;
import edu.umcs.bookystore.entities.UserEntity;
import edu.umcs.bookystore.exceptions.OutOfStockException;
import edu.umcs.bookystore.repositories.BookRepository;
import edu.umcs.bookystore.repositories.UserRepository;
import edu.umcs.bookystore.services.OrderService;

@Controller
@RequestMapping("/order")
public class OrderController {

	private static final String TEMPLATES_DIRECTORY = "order";
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
		if (!auth.isAuthenticated() || "anonymousUser".equals(auth.getName())) {
			return "redirect:/user/login";
		}
		UserEntity user = userRepository.findByUsername(auth.getName()).get();
		model.addAttribute("orders", user);
		return template("list");
	}

	@PostMapping("/create")
	public String postCreateOrder(Model model) {
		logger.debug("POST order endpoint called");
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (!auth.isAuthenticated() || "anonymousUser".equals(auth.getName())) {
			return "redirect:/user/login";
		}
		UserEntity user = userRepository.findByUsername(auth.getName()).get();
		OrderDto order = new OrderDto(user.getId(), cart.getBookIds());
		try {
			Long orderId = this.orderService.createOrder(order);
			model.addAttribute("successMessage", "Order created successfully. Order id: " + orderId);
		} catch (OutOfStockException e) {
			BookEntity book = bookRepository.findById(e.getBookId()).get();
			String errorMessage = String.format("The book \"%s\" is out of stock.", book.getTitle());
			model.addAttribute("errorMessage", errorMessage);
		}
		return template("create");
	}

}
