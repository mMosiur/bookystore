package edu.umcs.bookystore.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.umcs.bookystore.dtos.UserDto;
import edu.umcs.bookystore.exceptions.UsernameAlreadyExistsException;
import edu.umcs.bookystore.services.UserService;

@Controller
@RequestMapping("/user")
public class UserController {

	private static final String TEMPLATES_DIRECTORY = "user";
	private static final Logger logger = LoggerFactory.getLogger(UserController.class);

	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	private String template(String name) {
		return String.format("%s/%s", TEMPLATES_DIRECTORY, name);
	}

	@GetMapping("/login")
	public String getLogin(Model model) {
		logger.info("GET Login endpoint called");
		return template("login");
	}

	// @PostMapping("/login") handled by the default implementation of Spring Security

	@GetMapping("/register")
	public String getRegister(Model model) {
		logger.info("GET Register endpoint called");
		model.addAttribute("user", new UserDto());
		return template("register");
	}

	@PostMapping("/register")
	public String postRegister(Model model, @ModelAttribute UserDto user) {
		logger.info("POST Register endpoint called");
		boolean userCreatedSuccessfully = false;
		boolean usernameAlreadyExists = false;
		boolean error = false;
		try {
			this.userService.createUser(user);
			userCreatedSuccessfully = true;
		} catch (UsernameAlreadyExistsException e) {
			usernameAlreadyExists = true;
		} catch (Exception e) {
			logger.error("Error creating user", e);
			error = true;
		}
		model.addAttribute("userCreatedSuccessfully", userCreatedSuccessfully);
		model.addAttribute("usernameAlreadyExists", usernameAlreadyExists);
		model.addAttribute("error", error);
		model.addAttribute("user", user);
		return template("register");
	}

}
