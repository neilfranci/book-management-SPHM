package com.bgsix.bookmanagement.service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.bgsix.bookmanagement.controller.api.BookApiController;
import com.bgsix.bookmanagement.dto.BookDTO;
import com.bgsix.bookmanagement.dto.BookResponse;
import com.bgsix.bookmanagement.model.Book;
import com.bgsix.bookmanagement.repository.BookRepository;

@Service
public class BookService {
	private final BookRepository bookRepository;

	private static final Logger logger = LoggerFactory.getLogger(BookApiController.class);

	public BookService(BookRepository bookRepository) {
		this.bookRepository = bookRepository;
	}

	public BookDTO getBookById(Long id) {
		Book book = bookRepository.findBookById(id);

		BookDTO bookDTO = new BookDTO(book, null);

		return bookDTO;
	}

	private BookResponse getBooks(Page<Book> booksPage) {
		List<BookDTO> bookDTOs = booksPage.getContent().stream().map(book -> new BookDTO(book, Collections.emptyList()))
				.collect(Collectors.toList());
		BookResponse bookResponse = new BookResponse();
		bookResponse.setBooks(bookDTOs);
		bookResponse.setTotalPages(booksPage.getTotalPages());
		bookResponse.setTotalElements(booksPage.getTotalElements());
		return bookResponse;
	}

	public BookResponse getBooksByTitle(String title, int page, int size) {
		Pageable pageable = PageRequest.of(page, size);
		Page<Book> booksPage = bookRepository.findByTitleContainingIgnoreCase(title, pageable);
		return getBooks(booksPage);
	}

	public BookResponse getBooksByGenre(String genres, Pageable pageable) {
		// long startTime = System.currentTimeMillis();

		// lowercase the genres and split them by comma
		List<String> genreList = Arrays.asList(genres.toLowerCase().split(","));

		logger.info("Searching for books with genres: {}", genreList);

		long genreCount = genreList.size();

		// Find books by genres
		Page<Book> booksPage = bookRepository.findByGenre(genreList, genreCount, pageable);

		// Extract book ids to find genres
		List<Long> bookIds = booksPage.stream().map(Book::getBookId).collect(Collectors.toList());

		// Fetch genres for the list of book IDs
		List<Object[]> genresResult = bookRepository.findGenresForBooks(bookIds);

		// Map genres to the corresponding book
		Map<Long, List<String>> genresMap = genresResult.stream()
				.collect(Collectors.toMap(result -> ((Number) result[0]).longValue(),
						result -> (List<String>) Arrays.asList((String[]) result[1])));

		// Convert Book entities to BookDTO
		List<BookDTO> bookDTOs = booksPage.stream().map(book -> new BookDTO(book, genresMap.get(book.getBookId())))
				.collect(Collectors.toList());

		BookResponse bookResponse = new BookResponse();
		bookResponse.setBooks(bookDTOs);
		bookResponse.setTotalPages(booksPage.getTotalPages());
		bookResponse.setTotalElements(booksPage.getTotalElements());

		// long endTime = System.currentTimeMillis();
		// long executionTime = endTime - startTime;
		// logger.info("Execution time: {} ms", executionTime);

		return bookResponse;
	}

	public BookResponse getBooksByAuthor(String author, int page, int size) {
		Pageable pageable = PageRequest.of(page, size);
		Page<Book> booksPage = bookRepository.findByAuthorContainingIgnoreCase(author, pageable);
		return getBooks(booksPage);
	}

	public BookResponse getBooksByIsbn(String isbn, int page, int size) {
		Pageable pageable = PageRequest.of(page, size);
		Page<Book> booksPage = bookRepository.findByIsbnContainingIgnoreCase(isbn, pageable);
		return getBooks(booksPage);
	}

	public BookResponse getBooksByRating(Float rating, int page, int size) {
		Pageable pageable = PageRequest.of(page, size);
		Page<Book> booksPage = bookRepository.findByRatingGreaterThanEqual(rating, pageable);
		return getBooks(booksPage);
	}

	public BookResponse getTopRateBooks(int page, int size) {
		Pageable pageable = PageRequest.of(page, size);
		Page<Book> booksPage = bookRepository.findTopRate(pageable);
		return getBooks(booksPage);
	}

	// public Book saveBook(Book book) {
	// return bookRepository.save(book);
	// }

	// public void deleteBook(Long id) {
	// bookRepository.deleteById(id);
	// }
}
