package edu.umcs.bookystore.controllers;

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.umcs.bookystore.components.Cart;
import edu.umcs.bookystore.dtos.payu.OrderCreationNotifyDto;
import edu.umcs.bookystore.repositories.BookRepository;
import edu.umcs.bookystore.entities.BookEntity;

@Controller
@RequestMapping("/cart")
public class CartController {

	private static final String TEMPLATES_DIRECTORY = "cart";
	private static final Logger logger = LoggerFactory.getLogger(CartController.class);

	private Cart cart;
	private BookRepository bookRepository;

	public CartController(Cart cart, BookRepository bookRepository) {
		this.cart = cart;
		this.bookRepository = bookRepository;
	}

	private String template(String name) {
		return String.format("%s/%s", TEMPLATES_DIRECTORY, name);
	}

	@GetMapping
	public String getCart(Model model) {
		logger.debug("GET cart endpoint called");
		Set<Long> bookIds = this.cart.getBookIds();
		List<BookEntity> books = this.bookRepository.findAllById(bookIds);
		model.addAttribute("books", books);
		boolean isEmpty = this.cart.getSize() == 0;
		model.addAttribute("isEmpty", isEmpty);
		return template("index");
	}

	@PostMapping(params = "addBook")
	public String postAddToCart(Model model, @RequestParam(name = "addBook", required = true) long addBook) {
		logger.debug("POST add to cart endpoint called");
		this.cart.add(addBook);
		return getCart(model);
	}

	@PostMapping(params = "removeBook")
	public String postRemoveFromCart(Model model, @RequestParam(name = "removeBook", required = true) long removeBook) {
		logger.debug("POST remove from cart endpoint called");
		this.cart.remove(removeBook);
		return getCart(model);
	}

	@PostMapping("/test")
	@ResponseBody
	public String postTest(Model model, @RequestBody OrderCreationNotifyDto response) {
		logger.debug("POST test endpoint called");
		return response.getOrder().getStatus();
	}
}
