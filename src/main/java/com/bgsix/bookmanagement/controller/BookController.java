package com.bgsix.bookmanagement.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bgsix.bookmanagement.dto.BookDTO;
import com.bgsix.bookmanagement.dto.BookResponse;
import com.bgsix.bookmanagement.service.BookService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/book")
public class BookController {
	private final BookService bookService;

	private static final Logger logger = LoggerFactory.getLogger(BookController.class);

	public BookController(BookService bookService) {
		this.bookService = bookService;
	}

	@Operation(summary = "Get book by ID", description = "Get book by ID", tags = { "book" })
	@GetMapping("/{id}")
	public ResponseEntity<BookDTO> getBookById(@PathVariable Long id) {
		logger.info("Getting book with id: {}", id);

		BookDTO book = bookService.getBookById(id);

		if (book == null) {
			return ResponseEntity.notFound().build();
		}

		return ResponseEntity.ok(book);
	}

	@Operation(summary = "Get books by title", description = "Get books by title", tags = { "book" })
	@GetMapping("/title")
	public ResponseEntity<BookResponse> getBooksByTitle(@RequestParam("q") String title,
			@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "20") int size) {

		logger.info("Getting books with title: {}", title);

		BookResponse bookResponse = bookService.getBooksByTitle(title, page - 1, size);

		if (bookResponse.getBooks().isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		return ResponseEntity.ok(bookResponse);
	}

	@Operation(summary = "Get books by genre", description = "Get books by genre", tags = { "book" })
	@GetMapping("/genres")
	public ResponseEntity<BookResponse> getBooksByGenre(
			@Parameter(description = "Genres to filter by (separated by comma , )") @RequestParam("q") String genres,
			@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "20") int size) {

		BookResponse bookResponse = bookService.getBooksByGenres(genres, page - 1, size);

		if (bookResponse.getBooks().isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		return ResponseEntity.ok(bookResponse);
	}

	@Operation(summary = "Get books by author", description = "Get books by author", tags = { "book" })
	@GetMapping("/author")
	public ResponseEntity<BookResponse> getBooksByAuthor(@RequestParam("q") String author,
			@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "20") int size) {

		logger.info("Getting books by author: {}", author);

		BookResponse bookResponse = bookService.getBooksByAuthor(author, page - 1, size);

		if (bookResponse.getBooks().isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		return ResponseEntity.ok(bookResponse);
	}

	@Operation(summary = "Get books by ISBN", description = "Get books by ISBN", tags = { "book" })
	@GetMapping("/isbn")
	public ResponseEntity<BookResponse> getBooksByIsbn(@RequestParam("q") String isbn,
			@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "20") int size) {

		logger.info("Getting books by ISBN: {}", isbn);

		BookResponse bookResponse = bookService.getBooksByIsbn(isbn, page - 1, size);

		if (bookResponse.getBooks().isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		return ResponseEntity.ok(bookResponse);
	}

	@Operation(summary = "Get books by rating", description = "Get books by rating", tags = { "book" })
	@GetMapping("/rating")
	public ResponseEntity<BookResponse> getBooksByRating(@RequestParam("q") Float rating,
			@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "20") int size) {

		logger.info("Getting books by rating: {}", rating);

		BookResponse bookResponse = bookService.getBooksByRating(rating, page - 1, size);

		if (bookResponse.getBooks().isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		return ResponseEntity.ok(bookResponse);
	}

}
