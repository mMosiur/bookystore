package edu.umcs.bookystore;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import edu.umcs.bookystore.dtos.UserDto;
import edu.umcs.bookystore.entities.AuthorEntity;
import edu.umcs.bookystore.entities.BookEntity;
import edu.umcs.bookystore.entities.CategoryEntity;
import edu.umcs.bookystore.entities.PublisherEntity;
import edu.umcs.bookystore.entities.RoleEntity;
import edu.umcs.bookystore.exceptions.UsernameAlreadyExistsException;
import edu.umcs.bookystore.repositories.AuthorRepository;
import edu.umcs.bookystore.repositories.BookRepository;
import edu.umcs.bookystore.repositories.CategoryRepository;
import edu.umcs.bookystore.repositories.PublisherRepository;
import edu.umcs.bookystore.repositories.RoleRepository;
import edu.umcs.bookystore.services.UserService;

@Component
public class CommandLineAppStartupRunner implements CommandLineRunner {

	private static final Logger logger = LoggerFactory.getLogger(CommandLineAppStartupRunner.class);

	private final UserService userService;
	private final RoleRepository roleRepository;
	private final BookRepository bookRepository;
	private final AuthorRepository authorRepository;
	private final PublisherRepository publisherRepository;
	private final CategoryRepository categoryRepository;

	@Value("${spring.security.user.name}")
	public String DEFAULT_ADMIN_EMAIL;

	@Value("${spring.security.user.password}")
	public String DEFAULT_ADMIN_PASSWORD;

	@Value("${spring.security.user.roles}")
	public List<String> DEFAULT_ADMIN_ROLES;

	public CommandLineAppStartupRunner(RoleRepository roleRepository, UserService userService,
			BookRepository bookRepository, AuthorRepository authorRepository, PublisherRepository publisherRepository,
			CategoryRepository categoryRepository) {
		this.roleRepository = roleRepository;
		this.userService = userService;
		this.bookRepository = bookRepository;
		this.authorRepository = authorRepository;
		this.publisherRepository = publisherRepository;
		this.categoryRepository = categoryRepository;
	}

	private RoleEntity assertRoleExists(String roleName, String roleDescription) {
		if (roleName == null || roleName.isBlank()) {
			throw new IllegalArgumentException("Role name cannot be null or blank");
		}
		Optional<RoleEntity> roleSearch = this.roleRepository.findByName(roleName);
		if (roleSearch.isPresent()) {
			return roleSearch.get();
		}
		RoleEntity role = new RoleEntity(roleName, roleDescription);
		role = this.roleRepository.save(role);
		return role;
	}

	private RoleEntity assertRoleExists(String roleName) {
		return assertRoleExists(roleName, null);
	}

	private void assertDefaultRolesExist() {
		assertRoleExists("ADMIN", "The site administrator");
		assertRoleExists("USER", "The site ordinary user");
		for (String role : DEFAULT_ADMIN_ROLES) {
			role = role.toUpperCase();
			assertRoleExists(role);
		}
	}

	private void assertAdminExists() {
		if (userService.userExists(DEFAULT_ADMIN_EMAIL)) {
			return;
		}
		UserDto admin = new UserDto(
				DEFAULT_ADMIN_EMAIL, DEFAULT_ADMIN_PASSWORD,
				null, null);
		Set<String> roles = DEFAULT_ADMIN_ROLES
				.stream()
				.map(role -> role.toUpperCase())
				.collect(Collectors.toSet());
		try {
			this.userService.createUser(admin, roles);
		} catch (UsernameAlreadyExistsException e) {
			throw new RuntimeException("Unexpected behavior", e);
		}
	}

	private void assertDefaultAssortmentExists() {
		String title = "The Witcher: Last Wish";
		AuthorEntity author = new AuthorEntity("Andrzej", "Sapkowski");
		author = this.authorRepository.save(author);
		PublisherEntity publisher = new PublisherEntity("SuperNowa");
		publisher = this.publisherRepository.save(publisher);
		CategoryEntity category = new CategoryEntity("Fantasy");
		category = this.categoryRepository.save(category);
		double price = 35.38;
		BookEntity book = new BookEntity(
				title,
				author,
				publisher,
				category,
				price);
		book = this.bookRepository.save(book);
		logger.info("Book " + book.getTitle() + " saved.");
		title = "The Lord of the Rings";
		author = new AuthorEntity("J.R.R.", "Tolkien");
		author = this.authorRepository.save(author);
		price = 39.99;
		book = new BookEntity(
				title,
				author,
				publisher,
				category,
				price);
		book = this.bookRepository.save(book);
		logger.info("Book " + book.getTitle() + " saved.");
	}

	@Override
	public void run(String... args) throws Exception {
		assertDefaultRolesExist();
		assertAdminExists();
		assertDefaultAssortmentExists();
	}
}
