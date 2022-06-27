package edu.umcs.bookystore.controllers;

import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
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
	public String getManageBooks(Model model) {
		logger.debug("GET manage books endpoint called");
		BookDto newBook = new BookDto();
		model.addAttribute("newBook", newBook);
		model.addAttribute("books", this.bookRepository.findAll());
		model.addAttribute("authors", this.authorRepository.findAll());
		model.addAttribute("publishers", this.publisherRepository.findAll());
		model.addAttribute("categories", this.categoryRepository.findAll());
		return template("books");
	}

	@PostMapping(params = "delete")
	public String postManageBooksDelete(Model model, @RequestParam(required = true) long delete) {
		logger.debug("POST manage books (DELETE variant) endpoint called");
		BookDto newBook = new BookDto();
		model.addAttribute("newBook", newBook);
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
			String successMessage = String.format("Book \"%s\" by \"%s\" created",
					bookEntity.getTitle(), authorEntity.getFullName());
			model.addAttribute("successMessage", successMessage);
			newBook = new BookDto();
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

	@GetMapping("/{id}/details")
	public String getBookDetails(Model model, @PathVariable long id) {
		logger.debug("GET book details endpoint called");
		model.addAttribute("id", id);
		BookEntity bookEntity = this.bookRepository.findById(id).orElse(null);
		BookDto book = null;
		if (bookEntity != null) {
			book = new BookDto(
					bookEntity.getTitle(),
					bookEntity.getAuthor().getId(),
					bookEntity.getPublisher().getId(),
					bookEntity.getCategory().getId(),
					bookEntity.getPrice(),
					bookEntity.getStock());
			model.addAttribute("authors", this.authorRepository.findAll());
			model.addAttribute("publishers", this.publisherRepository.findAll());
			model.addAttribute("categories", this.categoryRepository.findAll());
		}
		model.addAttribute("book", book);
		return template("book-details");
	}

	@PostMapping("/{id}/details")
	public String postBookDetails(Model model, @PathVariable long id, @ModelAttribute BookDto book) {
		logger.debug("POST book details endpoint called");
		model.addAttribute("id", id);
		try {
			BookEntity bookEntity = this.bookRepository.findById(id).get();
			bookEntity.setTitle(book.getTitle());
			bookEntity.setAuthor(this.authorRepository.findById(book.getAuthorId()).get());
			bookEntity.setPublisher(this.publisherRepository.findById(book.getPublisherId()).get());
			bookEntity.setCategory(this.categoryRepository.findById(book.getCategoryId()).get());
			bookEntity.setPrice(book.getPrice());
			bookEntity.setStock(book.getStock());
			bookEntity = this.bookRepository.save(bookEntity);
			book.setTitle(bookEntity.getTitle());
			book.setAuthorId(bookEntity.getAuthor().getId());
			book.setPublisherId(bookEntity.getPublisher().getId());
			book.setCategoryId(bookEntity.getCategory().getId());
			book.setPrice(bookEntity.getPrice());
			book.setStock(bookEntity.getStock());
			String successMessage = "Book successfully updated";
			model.addAttribute("successMessage", successMessage);
		} catch (NoSuchElementException e) {
			book = null;
		} catch (IllegalArgumentException e) {
			String errorMessage = String.format("Error updating book: %s", e.getMessage());
			model.addAttribute("errorMessage", errorMessage);
		} catch (Exception e) {
			logger.error("Error updating book", e);
			String errorMessage = String.format("Error updating book: %s", e.getMessage());
			model.addAttribute("errorMessage", errorMessage);
		}
		model.addAttribute("book", book);
		model.addAttribute("authors", this.authorRepository.findAll());
		model.addAttribute("publishers", this.publisherRepository.findAll());
		model.addAttribute("categories", this.categoryRepository.findAll());
		return template("book-details");
	}
}
