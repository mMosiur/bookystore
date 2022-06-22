package edu.umcs.bookystore.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import edu.umcs.bookystore.entities.AuthorEntity;
import edu.umcs.bookystore.entities.BookEntity;
import edu.umcs.bookystore.entities.CategoryEntity;
import edu.umcs.bookystore.entities.PublisherEntity;
import edu.umcs.bookystore.repositories.AuthorRepository;
import edu.umcs.bookystore.repositories.BookRepository;
import edu.umcs.bookystore.repositories.CategoryRepository;
import edu.umcs.bookystore.repositories.PublisherRepository;

@Controller
@RequestMapping("/books")
public class BooksController {

	private static final String TEMPLATES_DIRECTORY = "books";
	private static final Logger logger = LoggerFactory.getLogger(BooksController.class);

	private final BookRepository bookRepository;
	private final AuthorRepository authorRepository;
	private final PublisherRepository publisherRepository;
	private final CategoryRepository categoryRepository;

	public BooksController(
			BookRepository bookRepository,
			AuthorRepository authorRepository,
			PublisherRepository publisherRepository,
			CategoryRepository categoryRepository) {
		this.bookRepository = bookRepository;
		this.authorRepository = authorRepository;
		this.publisherRepository = publisherRepository;
		this.categoryRepository = categoryRepository;
	}

	private String template(String name) {
		return String.format("%s/%s", TEMPLATES_DIRECTORY, name);
	}

	@GetMapping(value = "/browse")
	public String getBrowse(Model model,
			@RequestParam(defaultValue = "all") String filterAuthor,
			@RequestParam(defaultValue = "all") String filterPublisher,
			@RequestParam(defaultValue = "all") String filterCategory) {
		logger.info("GET Browse books endpoint called");

		AuthorEntity author = null;
		if (!"all".equals(filterAuthor)) {
			try {
				Long id = Long.parseLong(filterAuthor);
				author = authorRepository.findById(id).orElse(null);
			} catch (NumberFormatException e) {
			}
		}
		PublisherEntity publisher = null;
		if (!"all".equals(filterPublisher)) {
			try {
				Long id = Long.parseLong(filterPublisher);
				publisher = publisherRepository.findById(id).orElse(null);
			} catch (NumberFormatException e) {
			}
		}
		CategoryEntity category = null;
		if (!"all".equals(filterCategory)) {
			try {
				Long id = Long.parseLong(filterCategory);
				category = categoryRepository.findById(id).orElse(null);
			} catch (NumberFormatException e) {
			}
		}

		model.addAttribute("authors", this.authorRepository.findAll());
		model.addAttribute("publishers", this.publisherRepository.findAll());
		model.addAttribute("categories", this.categoryRepository.findAll());

		List<BookEntity> books = this.bookRepository.findAll();
		final AuthorEntity authorFilter = author;
		final PublisherEntity publisherFilter = publisher;
		final CategoryEntity categoryFilter = category;
		model.addAttribute("authorFilter", authorFilter);
		model.addAttribute("publisherFilter", publisherFilter);
		model.addAttribute("categoryFilter", categoryFilter);
		books.removeIf(book -> !MatchesFilters(book, authorFilter, publisherFilter, categoryFilter));
		model.addAttribute("books", books);

		return template("browse");
	}

	private boolean MatchesFilters(BookEntity book,
			AuthorEntity authorFilter, PublisherEntity publisherFilter, CategoryEntity categoryFilter) {
		if (authorFilter != null && book.getAuthor().getId() != authorFilter.getId()) {
			return false;
		}
		if (publisherFilter != null && book.getPublisher().getId() != publisherFilter.getId()) {
			return false;
		}
		if (categoryFilter != null && book.getCategory().getId() != categoryFilter.getId()) {
			return false;
		}
		return true;
	}

	@GetMapping(value = "/manage")
	public String getManage(Model model) {
		logger.debug("Manage books endpoint called");
		model.addAttribute("authors", this.authorRepository.findAll());
		model.addAttribute("publishers", this.publisherRepository.findAll());
		model.addAttribute("categories", this.categoryRepository.findAll());
		return template("manage");
	}

	@PostMapping(value = "/manage/create-author")
	public String postCreateAuthor(Model model,
			@RequestParam String firstName,
			@RequestParam String lastName) {
		logger.info("POST create author endpoint called");
		model.addAttribute("entityType", "author");
		AuthorEntity author = new AuthorEntity(firstName, lastName);
		author = this.authorRepository.save(author);
		model.addAttribute("author", author);
		return template("create-successful");
	}

	@PostMapping(value = "/manage/create-publisher")
	public String postCreatePublisher(Model model,
			@RequestParam String name) {
		logger.info("POST create publisher endpoint called");
		model.addAttribute("entityType", "publisher");
		PublisherEntity publisher = new PublisherEntity(name);
		publisher = this.publisherRepository.save(publisher);
		model.addAttribute("publisher", publisher);
		return template("create-successful");
	}

	@PostMapping(value = "/manage/create-category")
	public String postCreateCategory(Model model,
			@RequestParam String name) {
		logger.info("POST create category endpoint called");
		model.addAttribute("entityType", "category");
		CategoryEntity category = new CategoryEntity(name);
		category = this.categoryRepository.save(category);
		model.addAttribute("category", category);
		return template("create-successful");
	}

	@PostMapping(value = "/manage/create-book")
	public String postCreateBook(Model model,
			@RequestParam String title,
			@RequestParam long author,
			@RequestParam long publisher,
			@RequestParam long category,
			@RequestParam double price) {
		logger.info("POST create book endpoint called");
		model.addAttribute("entityType", "book");
		AuthorEntity authorEntity = this.authorRepository.findById(author).orElse(null);
		if (authorEntity == null) {
			model.addAttribute("error", "Author not found");
			return template("create-failed");
		}
		PublisherEntity publisherEntity = this.publisherRepository.findById(publisher).orElse(null);
		if (publisherEntity == null) {
			model.addAttribute("error", "Publisher not found");
			return template("create-failed");
		}
		CategoryEntity categoryEntity = this.categoryRepository.findById(category).orElse(null);
		if (categoryEntity == null) {
			model.addAttribute("error", "Category not found");
			return template("create-failed");
		}
		BookEntity book = new BookEntity(title, authorEntity, publisherEntity, categoryEntity, price);
		book = this.bookRepository.save(book);
		model.addAttribute("book", book);
		return template("create-successful");
	}

}
