package edu.umcs.bookystore.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequestMapping("/")
public class IndexController {

	private static final Logger logger = LoggerFactory.getLogger(IndexController.class);

	@GetMapping(value = "/")
	public String getIndex(Model model) {
		logger.debug("Index endpoint called");
		var auth = SecurityContextHolder.getContext().getAuthentication();
		model.addAttribute("customAttr", auth.getName());
		return "index";
	}

}
