package com.bgsix.bookmanagement.controller.api;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/book")
public class BookApiController {
	private final BookService bookService;

	private static final Logger logger = LoggerFactory.getLogger(BookApiController.class);

	public BookApiController(BookService bookService) {
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

		Pageable pageable = PageRequest.of(page - 1, size);

		Page<BookDTO> bookDTOs = bookService.getBooksByTitle(title, pageable);

		if (bookDTOs.getContent().isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		BookResponse bookResponse = new BookResponse(bookDTOs.getContent(), bookDTOs.getTotalPages(),
				bookDTOs.getTotalElements());

		return ResponseEntity.ok(bookResponse);
	}

	@Operation(summary = "Get books by genre", description = "Get books by genre", tags = { "book" })
	@GetMapping("/genres")
	public ResponseEntity<BookResponse> getBooksByGenre(
			@Parameter(description = "Genres to filter by (separated by comma , )") @RequestParam("q") String genres,
			@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "20") int size) {

		Pageable pageable = PageRequest.of(page - 1, size);

		List<String> genreList = Arrays.asList(genres.toLowerCase().split(","));

		Page<BookDTO> bookDTOs = bookService.getBooksByGenre(genreList, pageable);

		if (bookDTOs.getContent().isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		BookResponse bookResponse = new BookResponse(bookDTOs.getContent(), bookDTOs.getTotalPages(),
				bookDTOs.getTotalElements());

		return ResponseEntity.ok(bookResponse);
	}

	@Operation(summary = "Get books by author", description = "Get books by author", tags = { "book" })
	@GetMapping("/author")
	public ResponseEntity<BookResponse> getBooksByAuthor(@RequestParam("q") String author,
			@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "20") int size) {

		logger.info("Getting books by author: {}", author);

		Pageable pageable = PageRequest.of(page - 1, size);
		Page<BookDTO> bookDTOs = bookService.getBooksByAuthor(author, pageable);

		if (bookDTOs.getContent().isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		BookResponse bookResponse = new BookResponse(bookDTOs.getContent(), bookDTOs.getTotalPages(),
				bookDTOs.getTotalElements());

		return ResponseEntity.ok(bookResponse);
	}

	@Operation(summary = "Get books by ISBN", description = "Get books by ISBN", tags = { "book" })
	@GetMapping("/isbn")
	public ResponseEntity<BookResponse> getBooksByIsbn(@RequestParam("q") String isbn,
			@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "20") int size) {

		logger.info("Getting books by ISBN: {}", isbn);

		Pageable pageable = PageRequest.of(page - 1, size);

		Page<BookDTO> bookDTOs = bookService.getBooksByIsbn(isbn, pageable);

		if (bookDTOs.getContent().isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		BookResponse bookResponse = new BookResponse(bookDTOs.getContent(), bookDTOs.getTotalPages(),
				bookDTOs.getTotalElements());
		return ResponseEntity.ok(bookResponse);
	}

	@Operation(summary = "Get books by rating", description = "Get books by rating", tags = { "book" })
	@GetMapping("/rating")
	public ResponseEntity<BookResponse> getBooksByRating(@RequestParam("q") Float rating,
			@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "20") int size) {

		logger.info("Getting books by rating: {}", rating);

		Pageable pageable = PageRequest.of(page - 1, size);

		Page<BookDTO> bookDTOs = bookService.getBooksByRating(rating, pageable);

		if (bookDTOs.getContent().isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		BookResponse bookResponse = new BookResponse(bookDTOs.getContent(), bookDTOs.getTotalPages(),
				bookDTOs.getTotalElements());

		return ResponseEntity.ok(bookResponse);
	}

	@Operation(summary = "Get top rated books", description = "Get top rated books", tags = { "book" })
	@GetMapping("/top-rated")
	public ResponseEntity<BookResponse> getTopRatedBooks(@RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "20") int size) {

		logger.info("Getting top rated books");

		Pageable pageable = PageRequest.of(page - 1, size);

		Page<BookDTO> bookDTOs = bookService.getTopRateBooks(pageable);

		if (bookDTOs.getContent().isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		BookResponse bookResponse = new BookResponse(bookDTOs.getContent(), bookDTOs.getTotalPages(),
				bookDTOs.getTotalElements());

		return ResponseEntity.ok(bookResponse);
	}

}
