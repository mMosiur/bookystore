package edu.umcs.bookystore.controllers;

import java.util.List;
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
import edu.umcs.bookystore.dtos.BookDto;
import edu.umcs.bookystore.dtos.CategoryDto;
import edu.umcs.bookystore.dtos.PublisherDto;
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

	@GetMapping("/manage")
	public String getManage(Model model) {
		logger.debug("Manage books endpoint called");
		model.addAttribute("authors", this.authorRepository.findAll());
		model.addAttribute("publishers", this.publisherRepository.findAll());
		model.addAttribute("categories", this.categoryRepository.findAll());
		model.addAttribute("books", this.bookRepository.findAll());
		return template("manage/index");
	}

	@GetMapping("/manage/authors")
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
				logger.error("Error deleting author", e);
				String errorMessage = "Error deleting author, given author does not exist";
				model.addAttribute("errorMessage", errorMessage);
			} catch (DataIntegrityViolationException e) {
				logger.error("Error deleting author", e);
				String errorMessage = "Cannot delete given author, it is used by some books";
				model.addAttribute("errorMessage", errorMessage);
			} catch (Exception e) {
				logger.error("Error deleting author", e);
				String errorMessage = String.format("Error while deleting an author: %s", e.getMessage());
				model.addAttribute("errorMessage", errorMessage);
			}
		}
		model.addAttribute("authors", this.authorRepository.findAll());
		return template("manage/authors");
	}

	@PostMapping("/manage/authors")
	public String postManageAuthors(Model model, @ModelAttribute AuthorDto newAuthor) {
		logger.info("POST manage authors endpoint called");
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
		return template("manage/authors");
	}

	@GetMapping("/manage/publishers")
	public String getManagePublishers(Model model, @RequestParam(required = false) Long delete) {
		logger.debug("GET manage publishers endpoint called");
		PublisherDto newPublisher = new PublisherDto();
		model.addAttribute("newPublisher", newPublisher);
		if (delete != null) {
			try {
				PublisherEntity publisher = this.publisherRepository.findById(delete).get();
				this.publisherRepository.delete(publisher);
				String successMessage = String.format("Publisher %s deleted", publisher.getName());
				model.addAttribute("successMessage", successMessage);
			} catch (NoSuchElementException e) {
				logger.error("Error deleting publisher", e);
				String errorMessage = "Error deleting publisher, given publisher does not exist";
				model.addAttribute("errorMessage", errorMessage);
			} catch (DataIntegrityViolationException e) {
				logger.error("Error deleting publisher", e);
				String errorMessage = "Cannot delete given publisher, it is used by some books";
				model.addAttribute("errorMessage", errorMessage);
			} catch (Exception e) {
				logger.error("Error deleting publisher", e);
				String errorMessage = String.format("Error while deleting a publisher: %s", e.getMessage());
				model.addAttribute("errorMessage", errorMessage);
			}
		}
		model.addAttribute("publishers", this.publisherRepository.findAll());
		return template("manage/publishers");
	}

	@PostMapping("/manage/publishers")
	public String postManagePublishers(Model model, @ModelAttribute PublisherDto newPublisher) {
		logger.info("POST manage publishers endpoint called");
		try {
			PublisherEntity publisherEntity = new PublisherEntity(
					newPublisher.getName());
			publisherEntity = this.publisherRepository.save(publisherEntity);
			newPublisher.setName(publisherEntity.getName());
			String successMessage = String.format("Publisher \"%s\" created", publisherEntity.getName());
			model.addAttribute("successMessage", successMessage);
		} catch (Exception e) {
			logger.error("Error creating publisher", e);
			String errorMessage = String.format("Error creating author: %s", e.getMessage());
			model.addAttribute("errorMessage", errorMessage);
		}
		model.addAttribute("newPublisher", newPublisher);
		model.addAttribute("publishers", this.publisherRepository.findAll());
		return template("manage/publishers");
	}

	@GetMapping("/manage/categories")
	public String getManageCategories(Model model, @RequestParam(required = false) Long delete) {
		logger.debug("GET manage categories endpoint called");
		CategoryDto newCategory = new CategoryDto();
		model.addAttribute("newCategory", newCategory);
		if (delete != null) {
			try {
				CategoryEntity category = this.categoryRepository.findById(delete).get();
				this.categoryRepository.delete(category);
				String successMessage = String.format("Category %s deleted", category.getName());
				model.addAttribute("successMessage", successMessage);
			} catch (NoSuchElementException e) {
				logger.error("Error deleting category", e);
				String errorMessage = "Error deleting category, given category does not exist";
				model.addAttribute("errorMessage", errorMessage);
			} catch (DataIntegrityViolationException e) {
				logger.error("Error deleting category", e);
				String errorMessage = "Cannot delete given category, it is used by some books";
				model.addAttribute("errorMessage", errorMessage);
			} catch (Exception e) {
				logger.error("Error deleting category", e);
				String errorMessage = String.format("Error while deleting a category: %s", e.getMessage());
				model.addAttribute("errorMessage", errorMessage);
			}
		}
		model.addAttribute("categories", this.categoryRepository.findAll());
		return template("manage/categories");
	}

	@PostMapping("/manage/categories")
	public String postManageCategories(Model model, @ModelAttribute CategoryDto newCategory) {
		logger.info("POST manage categories endpoint called");
		try {
			CategoryEntity categoryEntity = new CategoryEntity(
					newCategory.getName());
			categoryEntity = this.categoryRepository.save(categoryEntity);
			newCategory.setName(categoryEntity.getName());
			String successMessage = String.format("Category \"%s\" created", categoryEntity.getName());
			model.addAttribute("successMessage", successMessage);
		} catch (Exception e) {
			logger.error("Error creating category", e);
			String errorMessage = String.format("Error creating category: %s", e.getMessage());
			model.addAttribute("errorMessage", errorMessage);
		}
		model.addAttribute("newCategory", newCategory);
		model.addAttribute("categories", this.categoryRepository.findAll());
		return template("manage/categories");
	}

	@GetMapping("/manage/books")
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
				logger.error("Error deleting book", e);
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
		return template("manage/books");
	}

	@PostMapping("/manage/books")
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
		return template("manage/books");
	}

	@GetMapping("/manage/update-book/{id}")
	public String getUpdateBook(Model model, @PathVariable long id) {
		logger.info("GET update book endpoint called");
		model.addAttribute("operationType", "update");
		model.addAttribute("entityType", "book");
		BookEntity book = this.bookRepository.findById(id).orElse(null);
		if (book == null) {
			model.addAttribute("error", "Book not found");
			return template("operation-failed");
		}
		BookDto bookDto = new BookDto(
				book.getTitle(),
				book.getAuthor().getId(),
				book.getPublisher().getId(),
				book.getCategory().getId(),
				book.getPrice(),
				book.getStock());
		model.addAttribute("book", bookDto);
		model.addAttribute("authors", this.authorRepository.findAll());
		model.addAttribute("publishers", this.publisherRepository.findAll());
		model.addAttribute("categories", this.categoryRepository.findAll());
		return template("update");
	}

	@PostMapping("/manage/update-book/{id}")
	public String postUpdateBook(Model model, @PathVariable long id, @ModelAttribute BookDto book) {
		logger.info("POST update book endpoint called");
		BookEntity bookEntity = this.bookRepository.findById(id).orElse(null);
		if (bookEntity == null) {
			model.addAttribute("error", "Book not found");
			return template("operation-failed");
		}
		AuthorEntity authorEntity = this.authorRepository.findById(book.getAuthorId()).orElse(null);
		if (authorEntity == null) {
			model.addAttribute("error", "Author not found");
			return template("operation-failed");
		}
		PublisherEntity publisherEntity = this.publisherRepository.findById(book.getPublisherId()).orElse(null);
		if (publisherEntity == null) {
			model.addAttribute("error", "Publisher not found");
			return template("operation-failed");
		}
		CategoryEntity categoryEntity = this.categoryRepository.findById(book.getCategoryId()).orElse(null);
		if (categoryEntity == null) {
			model.addAttribute("error", "Category not found");
			return template("operation-failed");
		}
		bookEntity.setTitle(book.getTitle());
		bookEntity.setAuthor(authorEntity);
		bookEntity.setPublisher(publisherEntity);
		bookEntity.setCategory(categoryEntity);
		bookEntity.setPrice(book.getPrice());
		bookEntity.setStock(book.getStock());
		model.addAttribute("book", book);
		model.addAttribute("authors", this.authorRepository.findAll());
		model.addAttribute("publishers", this.publisherRepository.findAll());
		model.addAttribute("categories", this.categoryRepository.findAll());
		this.bookRepository.flush();
		model.addAttribute("successMessage", "Book updated successfully");
		return template("update");
	}

}
