package edu.umcs.bookystore.controllers;

import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import edu.umcs.bookystore.dtos.BookDto;
import edu.umcs.bookystore.entities.AuthorEntity;
import edu.umcs.bookystore.entities.BookEntity;
import edu.umcs.bookystore.entities.CategoryEntity;
import edu.umcs.bookystore.entities.PublisherEntity;
import edu.umcs.bookystore.repositories.AuthorRepository;
import edu.umcs.bookystore.repositories.BookRepository;
import edu.umcs.bookystore.repositories.CategoryRepository;
import edu.umcs.bookystore.repositories.PublisherRepository;

@Controller
@RequestMapping("/library/manage/books")
public class ManageBooksController {

	private static final String TEMPLATES_DIRECTORY = "library/manage";
	private static final Logger logger = LoggerFactory.getLogger(ManageBooksController.class);

	private final BookRepository bookRepository;
	private final AuthorRepository authorRepository;
	private final PublisherRepository publisherRepository;
	private final CategoryRepository categoryRepository;

	public ManageBooksController(BookRepository bookRepository, AuthorRepository authorRepository,
			PublisherRepository publisherRepository, CategoryRepository categoryRepository) {
		this.bookRepository = bookRepository;
		this.authorRepository = authorRepository;
		this.publisherRepository = publisherRepository;
		this.categoryRepository = categoryRepository;
	}

	private String template(String name) {
		return String.format("%s/%s", TEMPLATES_DIRECTORY, name);
	}

	@GetMapping
	public String getManageBooks(Model model, @RequestParam(required = false) Long delete) {
		logger.debug("GET manage books endpoint called");
		BookDto newBook = new BookDto();
		model.addAttribute("newBook", newBook);
		if (delete != null) {
			try {
				BookEntity book = this.bookRepository.findById(delete).get();
				this.bookRepository.delete(book);
				String successMessage = String.format("Book \"%s\" deleted", book.getTitle());
				model.addAttribute("successMessage", successMessage);
			} catch (NoSuchElementException e) {
				String errorMessage = "Error deleting book, given book does not exist";
				model.addAttribute("errorMessage", errorMessage);
			} catch (Exception e) {
				logger.error("Error deleting book", e);
				String errorMessage = String.format("Error while deleting a book: %s", e.getMessage());
				model.addAttribute("errorMessage", errorMessage);
			}
		}
		model.addAttribute("books", this.bookRepository.findAll());
		model.addAttribute("authors", this.authorRepository.findAll());
		model.addAttribute("publishers", this.publisherRepository.findAll());
		model.addAttribute("categories", this.categoryRepository.findAll());
		return template("books");
	}

	@PostMapping
	public String postManageBooks(Model model, @ModelAttribute BookDto newBook) {
		logger.debug("POST manage books endpoint called");
		try {
			AuthorEntity authorEntity = this.authorRepository.findById(newBook.getAuthorId()).orElse(null);
			if (authorEntity == null) {
				throw new NoSuchElementException("Author not found");
			}
			PublisherEntity publisherEntity = this.publisherRepository.findById(newBook.getPublisherId()).orElse(null);
			if (publisherEntity == null) {
				throw new NoSuchElementException("Publisher not found");
			}
			CategoryEntity categoryEntity = this.categoryRepository.findById(newBook.getCategoryId()).orElse(null);
			if (categoryEntity == null) {
				throw new NoSuchElementException("Category not found");
			}
			BookEntity bookEntity = new BookEntity(
					newBook.getTitle(),
					authorEntity,
					publisherEntity,
					categoryEntity,
					newBook.getPrice(),
					newBook.getStock());
			bookEntity = this.bookRepository.save(bookEntity);
			newBook.setTitle(bookEntity.getTitle());
			newBook.setAuthorId(bookEntity.getAuthor().getId());
			newBook.setPublisherId(bookEntity.getPublisher().getId());
			newBook.setCategoryId(bookEntity.getCategory().getId());
			newBook.setPrice(bookEntity.getPrice());
			newBook.setStock(bookEntity.getStock());
			String successMessage = String.format("Book \"%s\" by \"%s\" created",
					bookEntity.getTitle(), authorEntity.getFullName());
			model.addAttribute("successMessage", successMessage);
		} catch (Exception e) {
			logger.error("Error creating book", e);
			String errorMessage = String.format("Error creating book: %s", e.getMessage());
			model.addAttribute("errorMessage", errorMessage);
		}
		model.addAttribute("newBook", newBook);
		model.addAttribute("books", this.bookRepository.findAll());
		model.addAttribute("authors", this.authorRepository.findAll());
		model.addAttribute("publishers", this.publisherRepository.findAll());
		model.addAttribute("categories", this.categoryRepository.findAll());
		return template("books");
	}
}
