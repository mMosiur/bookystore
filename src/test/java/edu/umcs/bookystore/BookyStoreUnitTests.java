package edu.umcs.bookystore;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import edu.umcs.bookystore.entities.AuthorEntity;
import edu.umcs.bookystore.entities.BookEntity;
import edu.umcs.bookystore.entities.CategoryEntity;
import edu.umcs.bookystore.entities.PublisherEntity;
import edu.umcs.bookystore.entities.RoleEntity;

@SpringBootTest
public class BookyStoreUnitTests {

	@Test
	void AuthorEntity_should_throw_when_argument_is_invalid() {
		assertThrows(IllegalArgumentException.class, () -> {
			new AuthorEntity(
					null, "lastName");
		});
		assertThrows(IllegalArgumentException.class, () -> {
			new AuthorEntity(
					"firstName", "");
		});
		assertThrows(IllegalArgumentException.class, () -> {
			new AuthorEntity(
					"\t ", null);
		});
		assertThrows(IllegalArgumentException.class, () -> {
			AuthorEntity author = new AuthorEntity();
			author.setFirstName(null);
		});
		assertThrows(IllegalArgumentException.class, () -> {
			AuthorEntity author = new AuthorEntity();
			author.setFirstName(" ");
		});
	}

	@Test
	void AuthorEntity_should_not_throw_when_arguments_are_valid() {
		assertDoesNotThrow(() -> {
			AuthorEntity author = new AuthorEntity(
					"firstName", "lastName");
			assertEquals("firstName", author.getFirstName());
			assertEquals("lastName", author.getLastName());
		});
		assertDoesNotThrow(() -> {
			AuthorEntity author = new AuthorEntity(
					"veryLongFirstNameThatWillProbablyEndSomeDay and has whitespace inside",
					"anotherVeryLongNameThis time this is aLastNAME");
			assertEquals("veryLongFirstNameThatWillProbablyEndSomeDay and has whitespace inside",
					author.getFirstName());
			assertEquals("anotherVeryLongNameThis time this is aLastNAME",
					author.getLastName());
		});
		assertDoesNotThrow(() -> {
			AuthorEntity author = new AuthorEntity(
					"f", "l");
			assertEquals("f", author.getFirstName());
			assertEquals("l", author.getLastName());
		});
	}

	@Test
	void AuthorEntity_should_not_throw_when_argument_is_valid_with_whitespace_and_trim_result() {
		assertDoesNotThrow(() -> {
			AuthorEntity author = new AuthorEntity(
					"firstName ", " lastName");
			assertEquals("firstName", author.getFirstName());
			assertEquals("lastName", author.getLastName());
		});
		assertDoesNotThrow(() -> {
			AuthorEntity author = new AuthorEntity(
					"\tveryLongFirstNameThatWillProbablyEndSomeDay and has whitespace inside",
					"anotherVeryLongNameThis time this is aLastNAME\t \t");
			assertEquals("veryLongFirstNameThatWillProbablyEndSomeDay and has whitespace inside",
					author.getFirstName());
			assertEquals("anotherVeryLongNameThis time this is aLastNAME",
					author.getLastName());
		});
		assertDoesNotThrow(() -> {
			AuthorEntity author = new AuthorEntity(
					"f\n\n", " l");
			assertEquals("f", author.getFirstName());
			assertEquals("l", author.getLastName());
		});
	}

	@Test
	void BookEntity_should_throw_when_argument_is_invalid() {
		AuthorEntity author = new AuthorEntity();
		PublisherEntity publisher = new PublisherEntity();
		CategoryEntity category = new CategoryEntity();
		assertThrows(IllegalArgumentException.class, () -> {
			new BookEntity(null, author, publisher, category, 10.00, 2);
		});
		assertThrows(IllegalArgumentException.class, () -> {
			new BookEntity("\t \n", author, publisher, category, 10.00, 2);
		});
		assertThrows(IllegalArgumentException.class, () -> {
			new BookEntity("TITLE", author, publisher, null, 10.00, 2);
		});
		assertThrows(IllegalArgumentException.class, () -> {
			new BookEntity("TITLE", null, publisher, category, 10.00, 2);
		});
		assertThrows(IllegalArgumentException.class, () -> {
			BookEntity book = new BookEntity();
			book.setTitle(" ");
		});
		assertThrows(IllegalArgumentException.class, () -> {
			BookEntity book = new BookEntity();
			book.setTitle("OKEY");
			book.setAuthor(null);
		});
		assertThrows(IllegalArgumentException.class, () -> {
			BookEntity book = new BookEntity();
			book.setTitle("OKEY");
			book.setTitle(null);
		});
	}

	@Test
	void BookEntity_should_not_throw_when_arguments_are_valid() {
		AuthorEntity author = new AuthorEntity();
		PublisherEntity publisher = new PublisherEntity();
		CategoryEntity category = new CategoryEntity();
		assertDoesNotThrow(() -> {
			BookEntity book = new BookEntity(
					"TITLE", author, publisher, category, 10.00, 0);
			assertEquals("TITLE", book.getTitle());
			assertEquals(author, book.getAuthor());
			assertEquals(publisher, book.getPublisher());
			assertEquals(category, book.getCategory());
			assertEquals(10.00, book.getPrice(), 0.001);
			assertEquals(0, book.getStock());
		});
		assertDoesNotThrow(() -> {
			BookEntity book = new BookEntity(
					"Very long title that keeps on going, even though it should end a while ago",
					author, publisher, category, 0.00, 2);
			assertEquals("Very long title that keeps on going, even though it should end a while ago",
					book.getTitle());
			assertEquals(author, book.getAuthor());
			assertEquals(publisher, book.getPublisher());
			assertEquals(category, book.getCategory());
			assertEquals(0.00, book.getPrice(), 0.001);
			assertEquals(2, book.getStock());
		});
	}

	@Test
	void BookEntity_should_trim_string_arguments() {
		AuthorEntity author = new AuthorEntity();
		PublisherEntity publisher = new PublisherEntity();
		CategoryEntity category = new CategoryEntity();
		BookEntity book = new BookEntity(
				"TITLE ", author, publisher, category, 10.00, 0);
		assertEquals("TITLE", book.getTitle());
		assertEquals(author, book.getAuthor());
		assertEquals(publisher, book.getPublisher());
		assertEquals(category, book.getCategory());
		assertEquals(10.00, book.getPrice(), 0.001);
		assertEquals(0, book.getStock());
		book.setTitle("\n\tVery long title that keeps on going, even though it should end a while ago");
		assertEquals("Very long title that keeps on going, even though it should end a while ago",
				book.getTitle());
	}

	@Test
	void CategoryEntity_should_throw_when_argument_is_invalid() {
		assertThrows(IllegalArgumentException.class, () -> {
			new CategoryEntity(null);
		});
		assertThrows(IllegalArgumentException.class, () -> {
			new CategoryEntity("");
		});
		assertThrows(IllegalArgumentException.class, () -> {
			new CategoryEntity(" \t\n ");
		});
		assertThrows(IllegalArgumentException.class, () -> {
			CategoryEntity category = new CategoryEntity();
			category.setName(null);
		});
		assertThrows(IllegalArgumentException.class, () -> {
			CategoryEntity category = new CategoryEntity();
			category.setName("");
		});
		assertThrows(IllegalArgumentException.class, () -> {
			CategoryEntity category = new CategoryEntity();
			category.setName(" \t\n ");
		});
	}

	@Test
	void CategoryEntity_should_not_throw_when_argument_are_valid() {
		assertDoesNotThrow(() -> {
			new CategoryEntity("NAME");
		});
		assertDoesNotThrow(() -> {
			CategoryEntity category = new CategoryEntity();
			category.setName("OTHER nAME");
		});
		assertDoesNotThrow(() -> {
			CategoryEntity category = new CategoryEntity("one name is enough");
			category.setName("it is not");
		});
	}

	@Test
	void CategoryEntity_should_trim_string_arguments() {
		CategoryEntity category = new CategoryEntity("\tNaMe ");
		assertEquals("NaMe", category.getName());
		category.setName("\tA category to remember \n\t");
		assertEquals("A category to remember", category.getName());
	}

	@Test
	void PublisherEntity_should_throw_when_argument_is_invalid() {
		assertThrows(IllegalArgumentException.class, () -> {
			new PublisherEntity(null);
		});
		assertThrows(IllegalArgumentException.class, () -> {
			new PublisherEntity("");
		});
		assertThrows(IllegalArgumentException.class, () -> {
			new PublisherEntity(" \n\t");
		});
		assertThrows(IllegalArgumentException.class, () -> {
			PublisherEntity publisher = new PublisherEntity();
			publisher.setName(null);
		});
		assertThrows(IllegalArgumentException.class, () -> {
			PublisherEntity publisher = new PublisherEntity();
			publisher.setName("");
		});
		assertThrows(IllegalArgumentException.class, () -> {
			PublisherEntity publisher = new PublisherEntity();
			publisher.setName(" \n\t");
		});
	}

	@Test
	void PublisherEntity_should_not_throw_when_argument_are_valid() {
		assertDoesNotThrow(() -> {
			new PublisherEntity("NAME");
		});
		assertDoesNotThrow(() -> {
			PublisherEntity publisher = new PublisherEntity();
			publisher.setName("OTHER nAME");
		});
		assertDoesNotThrow(() -> {
			PublisherEntity publisher = new PublisherEntity("one name is enough");
			publisher.setName("it is not");
		});
	}

	@Test
	void PublisherEntity_should_trim_string_arguments() {
		PublisherEntity publisher = new PublisherEntity("\tNaMe ");
		assertEquals("NaMe", publisher.getName());
		publisher.setName("\tA publisher to remember \n\t");
		assertEquals("A publisher to remember", publisher.getName());
	}

	@Test
	void RoleEntity_should_throw_when_argument_is_invalid() {
		assertThrows(IllegalArgumentException.class, () -> {
			new RoleEntity(null);
		});
		assertThrows(IllegalArgumentException.class, () -> {
			new RoleEntity("", "\t ");
		});
		assertThrows(IllegalArgumentException.class, () -> {
			new RoleEntity(" \n\t", null);
		});
		assertThrows(IllegalArgumentException.class, () -> {
			RoleEntity role = new RoleEntity();
			role.setName(null);
		});
		assertThrows(IllegalArgumentException.class, () -> {
			RoleEntity role = new RoleEntity();
			role.setDescription("");
		});
		assertThrows(IllegalArgumentException.class, () -> {
			RoleEntity role = new RoleEntity();
			role.setName(" \n\t");
		});
	}

	@Test
	void RoleEntity_should_not_throw_when_argument_are_valid() {
		assertDoesNotThrow(() -> {
			RoleEntity role = new RoleEntity("NAME", null);
			assertEquals("NAME", role.getName());
			assertNull(role.getDescription());
		});
		assertDoesNotThrow(() -> {
			RoleEntity role = new RoleEntity("NAME", "DESCRIPTION");
			assertEquals("NAME", role.getName());
			assertEquals("DESCRIPTION", role.getDescription());
		});
		assertDoesNotThrow(() -> {
			RoleEntity role = new RoleEntity("One name");
			role.setName("OTHER nAME");
			assertEquals("OTHER nAME", role.getName());
		});
	}

	@Test
	void RoleEntity_should_trim_string_arguments() {
		RoleEntity role = new RoleEntity("\tNaMe ", null);
		assertEquals("NaMe", role.getName());
		role.setName("\tA role to remember \n\t");
		assertEquals("A role to remember", role.getName());
	}

}
