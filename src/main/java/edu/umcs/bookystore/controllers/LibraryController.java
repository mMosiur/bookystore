package edu.umcs.bookystore.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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
@RequestMapping("/library")
public class LibraryController {

	private static final String TEMPLATES_DIRECTORY = "library";
	private static final Logger logger = LoggerFactory.getLogger(LibraryController.class);

	private final BookRepository bookRepository;
	private final AuthorRepository authorRepository;
	private final PublisherRepository publisherRepository;
	private final CategoryRepository categoryRepository;

	public LibraryController(
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

	@GetMapping("/browse")
	public String getBrowse(Model model,
			@RequestParam(defaultValue = "all") String filterAuthor,
			@RequestParam(defaultValue = "all") String filterPublisher,
			@RequestParam(defaultValue = "all") String filterCategory) {
		logger.info("GET browse library endpoint called");
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

	@GetMapping("/manage")
	public String getManage(Model model) {
		logger.info("GET manage library endpoint called");
		return template("manage/index");
	}

}
