package edu.umcs.bookystore.controllers;

import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import edu.umcs.bookystore.dtos.AuthorDto;
import edu.umcs.bookystore.entities.AuthorEntity;
import edu.umcs.bookystore.repositories.AuthorRepository;

@Controller
@RequestMapping("/library/manage/authors")
public class ManageAuthorsController {

	private static final String TEMPLATES_DIRECTORY = "library/manage";
	private static final Logger logger = LoggerFactory.getLogger(ManageAuthorsController.class);

	private final AuthorRepository authorRepository;

	public ManageAuthorsController(AuthorRepository authorRepository) {
		this.authorRepository = authorRepository;
	}

	private String template(String name) {
		return String.format("%s/%s", TEMPLATES_DIRECTORY, name);
	}

	@GetMapping
	public String getManageAuthors(Model model, @RequestParam(required = false) Long delete) {
		logger.debug("GET manage authors endpoint called");
		AuthorDto newAuthor = new AuthorDto();
		model.addAttribute("newAuthor", newAuthor);
		if (delete != null) {
			try {
				AuthorEntity author = this.authorRepository.findById(delete).get();
				this.authorRepository.delete(author);
				String successMessage = String.format("Author %s deleted", author.getFullName());
				model.addAttribute("successMessage", successMessage);
			} catch (NoSuchElementException e) {
				String errorMessage = "Error deleting author, given author does not exist";
				model.addAttribute("errorMessage", errorMessage);
			} catch (DataIntegrityViolationException e) {
				String errorMessage = "Cannot delete given author, it is used by some books";
				model.addAttribute("errorMessage", errorMessage);
			} catch (Exception e) {
				logger.error("Error deleting author", e);
				String errorMessage = String.format("Error while deleting an author: %s", e.getMessage());
				model.addAttribute("errorMessage", errorMessage);
			}
		}
		model.addAttribute("authors", this.authorRepository.findAll());
		return template("authors");
	}

	@PostMapping
	public String postManageAuthors(Model model, @ModelAttribute AuthorDto newAuthor) {
		logger.debug("POST manage authors endpoint called");
		try {
			AuthorEntity authorEntity = new AuthorEntity(
					newAuthor.getFirstName(),
					newAuthor.getLastName());
			authorEntity = this.authorRepository.save(authorEntity);
			newAuthor.setFirstName(authorEntity.getFirstName());
			newAuthor.setLastName(authorEntity.getLastName());
			String successMessage = String.format("Author \"%s\" created", authorEntity.getFullName());
			model.addAttribute("successMessage", successMessage);
		} catch (Exception e) {
			logger.error("Error creating author", e);
			String errorMessage = String.format("Error creating author: %s", e.getMessage());
			model.addAttribute("errorMessage", errorMessage);
		}
		model.addAttribute("newAuthor", newAuthor);
		model.addAttribute("authors", this.authorRepository.findAll());
		return template("authors");
	}

	@GetMapping("/{id}/details")
	public String getAuthorDetails(Model model, @PathVariable long id) {
		logger.debug("GET author details endpoint called");
		model.addAttribute("id", id);
		AuthorEntity authorEntity = this.authorRepository.findById(id).orElse(null);
		AuthorDto author = null;
		if (authorEntity != null) {
			author = new AuthorDto(
					authorEntity.getFirstName(),
					authorEntity.getLastName());
		}
		model.addAttribute("author", author);
		return template("author-details");
	}

	@PostMapping("/{id}/details")
	public String postAuthorDetails(Model model, @PathVariable long id, @ModelAttribute AuthorDto author) {
		logger.debug("POST author details endpoint called");
		model.addAttribute("id", id);
		try {
			AuthorEntity authorEntity = this.authorRepository.findById(id).get();
			authorEntity.setFirstName(author.getFirstName());
			authorEntity.setLastName(author.getLastName());
			authorEntity = this.authorRepository.save(authorEntity);
			author.setFirstName(authorEntity.getFirstName());
			author.setLastName(authorEntity.getLastName());
			String successMessage = "Author successfully updated";
			model.addAttribute("successMessage", successMessage);
		} catch (NoSuchElementException e) {
			author = null;
		} catch (IllegalArgumentException e) {
			String errorMessage = String.format("Error updating author: %s", e.getMessage());
			model.addAttribute("errorMessage", errorMessage);
		} catch (Exception e) {
			logger.error("Error updating author", e);
			String errorMessage = String.format("Error updating author: %s", e.getMessage());
			model.addAttribute("errorMessage", errorMessage);
		}
		model.addAttribute("author", author);
		return template("author-details");
	}

}
