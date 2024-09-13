package com.bgsix.server.service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.bgsix.server.controller.BookController;
import com.bgsix.server.dto.BookDTO;
import com.bgsix.server.dto.BookResponse;
import com.bgsix.server.model.Book;
import com.bgsix.server.model.BookGenre;
import com.bgsix.server.model.Genre;
import com.bgsix.server.repository.BookGenreRepository;
import com.bgsix.server.repository.BookRepository;

@Service
public class BookService {
	private final BookRepository bookRepository;

	private final BookGenreRepository bookGenreRepository;

	private static final Logger logger = LoggerFactory.getLogger(BookController.class);

	public BookService(BookRepository bookRepository, BookGenreRepository bookGenreRepository) {
		this.bookRepository = bookRepository;
		this.bookGenreRepository = bookGenreRepository;
	}

	public List<Book> getAllBooks() {
		return bookRepository.findAll();
	}

	public BookDTO getBookById(Long id) {
		Optional<Book> book = bookRepository.findById(id);

		BookDTO bookDTO = new BookDTO(book.get(), Collections.emptyList());

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

	public BookResponse getBooksByGenres(String genres, int page, int size) {
		long startTime = System.currentTimeMillis();

		// lowercase the genres and split them by comma
		List<String> genreList = Arrays.asList(genres.toLowerCase().split(","));

		logger.info("Searching for books with genres: {}", genreList);

		long genreCount = genreList.size();

		Pageable pageable = PageRequest.of(page, size);

		Page<Book> booksPage = bookRepository.findByGenres(genreList, genreCount, pageable);

		List<BookGenre> bookGenresInPage = bookGenreRepository.findByBookIn(booksPage.getContent());

		logger.info("Found {} genres for {} books", bookGenresInPage.size(), booksPage.getSize());

		// Group genres by book ID
		Map<Long, List<Genre>> genresByBookId = bookGenresInPage.stream()
				.collect(Collectors.groupingBy(bookGenre -> bookGenre.getBook().getBookId(),
						Collectors.mapping(BookGenre::getGenre, Collectors.toList())));

		// Convert books to BookDTOs
		List<BookDTO> bookDTOs = booksPage.getContent().stream().map(book -> {
			// Get genres for the current book
			List<String> bookGenres = genresByBookId.getOrDefault(book.getBookId(), Collections.emptyList()).stream()
					.map(Genre::getName).collect(Collectors.toList());
			// Create a new BookDTO
			return new BookDTO(book, bookGenres);
		}).collect(Collectors.toList());

		BookResponse bookResponse = new BookResponse();
		bookResponse.setBooks(bookDTOs);
		bookResponse.setTotalPages(booksPage.getTotalPages());
		bookResponse.setTotalElements(booksPage.getTotalElements());

		long endTime = System.currentTimeMillis();
		long executionTime = endTime - startTime;
		logger.info("Execution time: {} ms", executionTime);

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

	// public Book saveBook(Book book) {
	// return bookRepository.save(book);
	// }

	// public void deleteBook(Long id) {
	// bookRepository.deleteById(id);
	// }
}
