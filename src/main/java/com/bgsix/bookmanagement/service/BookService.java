package com.bgsix.bookmanagement.service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

// import com.bgsix.bookmanagement.controller.api.BookApiController;
import com.bgsix.bookmanagement.dto.BookDTO;
import com.bgsix.bookmanagement.dto.BookForm;
import com.bgsix.bookmanagement.interfaces.IBookService;
import com.bgsix.bookmanagement.model.Book;
import com.bgsix.bookmanagement.repository.BookRepository;

@Service
public class BookService implements IBookService {
	@Autowired
	private BookRepository bookRepository;

	private static final Logger logger = LoggerFactory.getLogger(BookService.class);

	public Page<BookDTO> getAllBooks(Pageable pageable) {
		Page<Book> booksPage = bookRepository.findAll(pageable);
		return convertToBookDTOPage(booksPage, pageable);
	}

	public BookDTO getBookById(Long id) {
		Book book = bookRepository.findBookById(id);

		// Temporary fix to get genres for a book by ID instead of using
		// findGenresForBookId
		List<Object[]> genres = bookRepository.findGenresForBookIds(Arrays.asList(id));

		Map<Long, List<String>> genresMap = genres.stream()
				.collect(Collectors.toMap(result -> ((Number) result[0]).longValue(),
						result -> (List<String>) Arrays.asList((String[]) result[1])));

		List<String> genresList = genresMap.get(id);

		return new BookDTO(book, genresList);
	}

	// Convert a Page<Book> to a Page<BookDTO>
	private Page<BookDTO> convertToBookDTOPage(Page<Book> booksPage, Pageable pageable) {
		List<Long> bookIds = booksPage.stream().map(Book::getBookId).collect(Collectors.toList());

		List<Object[]> genresResult = bookRepository.findGenresForBookIds(bookIds);

		Map<Long, List<String>> genresMap = genresResult.stream()
				.collect(Collectors.toMap(result -> ((Number) result[0]).longValue(),
						result -> (List<String>) Arrays.asList((String[]) result[1])));

		List<BookDTO> bookDTOs = booksPage.stream().map(book -> new BookDTO(book, genresMap.get(book.getBookId())))
				.collect(Collectors.toList());

		return new PageImpl<>(bookDTOs, pageable, booksPage.getTotalElements());
	}

	public Page<BookDTO> getBooksByTitle(String title, Pageable pageable) {
		Page<Book> booksPage = bookRepository.findByTitleContainingIgnoreCase(title, pageable);

		return convertToBookDTOPage(booksPage, pageable);
	}

	public Page<BookDTO> getBooksByGenre(List<String> genreList, Pageable pageable) {
		// long startTime = System.currentTimeMillis();

		// genreList =
		// genreList.stream().map(String::toLowerCase).collect(Collectors.toList());

		long genreCount = genreList.size();

		logger.info("Searching for books with genres: {} and genreCount: {}", genreList, genreCount);

		// Find books by genres
		Page<Book> booksPage = bookRepository.findByGenre(genreList, genreCount, pageable);

		// long endTime = System.currentTimeMillis();
		// long executionTime = endTime - startTime;
		// logger.info("Execution time: {} ms", executionTime);

		return convertToBookDTOPage(booksPage, pageable);
	}

	public Page<BookDTO> getBooksByAuthor(String author, Pageable pageable) {
		Page<Book> booksPage = bookRepository.findByAuthorContainingIgnoreCase(author, pageable);
		return convertToBookDTOPage(booksPage, pageable);
	}

	public Page<BookDTO> getBooksByIsbn(String isbn, Pageable pageable) {
		Page<Book> booksPage = bookRepository.findByIsbnContainingIgnoreCase(isbn, pageable);
		return convertToBookDTOPage(booksPage, pageable);
	}

	public Page<BookDTO> getBooksByRating(Float rating, Pageable pageable) {
		Page<Book> booksPage = bookRepository.findByRatingGreaterThanEqual(rating, pageable);
		return convertToBookDTOPage(booksPage, pageable);
	}

	public Page<BookDTO> getTopRateBooks(Pageable pageable) {
		Page<Book> booksPage = bookRepository.findTopRate(pageable);
		return convertToBookDTOPage(booksPage, pageable);
	}

	// public BookResponse searchBooks(String searchInput, String searchBy,
	// List<String> genreList, Pageable pageable) {
	// Page<BookDTO> booksPage = Page.empty(); // Initialize an empty page

	// // If searchInput is blank, return top-rated books
	// if (searchInput == null || searchInput.isBlank()) {
	// booksPage = getTopRateBooks(pageable);

	// } else if (genreList != null && !genreList.isEmpty()) {

	// long genreCount = genreList.size();
	// // Fetch all books by genre without pagination
	// List<BookDTO> booksByGenre = bookRepository.getBooksByGenres(genreList,
	// genreCount).stream()
	// .map(book -> new BookDTO(book, null)) // Set null genres for now, will
	// populate later
	// .collect(Collectors.toList());

	// // Apply search input filtering
	// String input = searchInput.toLowerCase().trim();
	// List<BookDTO> filteredBooks = booksByGenre.stream().filter(book -> {
	// switch (searchBy.toLowerCase().trim()) {
	// case "title":
	// logger.info("Searching for books by title: {}", book.getTitle());
	// return book.getTitle().toLowerCase().contains(input);
	// case "author":
	// return book.getAuthor().toLowerCase().contains(input);
	// case "isbn":
	// return book.getIsbn().toLowerCase().contains(input);
	// default:
	// return false;
	// }
	// }).collect(Collectors.toList());

	// // Get book IDs for the filtered books
	// List<Long> bookIds =
	// filteredBooks.stream().map(BookDTO::getBookId).collect(Collectors.toList());

	// // Fetch genres for the filtered books
	// List<Object[]> genresResult = bookRepository.findGenresForBooks(bookIds);
	// Map<Long, List<String>> genresMap = genresResult.stream()
	// .collect(Collectors.toMap(result -> ((Number) result[0]).longValue(),
	// result -> (List<String>) Arrays.asList((String[]) result[1])));

	// // Map genres back to the books
	// filteredBooks.forEach(book ->
	// book.setGenres(genresMap.get(book.getBookId())));

	// // Convert filtered and genre-mapped list to Page object with pagination
	// booksPage = createPaginatedResult(filteredBooks, pageable);

	// } else {
	// // Search directly by searchBy criteria
	// switch (searchBy.toLowerCase()) {
	// case "title":
	// logger.info("Searching for books by title: {}", searchInput);
	// booksPage = getBooksByTitle(searchInput, pageable);
	// break;
	// case "author":
	// booksPage = getBooksByAuthor(searchInput, pageable);
	// break;
	// case "isbn":
	// booksPage = getBooksByIsbn(searchInput, pageable);
	// break;
	// default:
	// booksPage = Page.empty(); // Return empty page for invalid searchBy
	// break;
	// }
	// }

	// logger.info("Search results: {} books found",
	// booksPage.getNumberOfElements());

	// return new BookResponse(booksPage.getContent(), booksPage.getTotalPages(),
	// booksPage.getTotalElements());
	// }

	// private Page<BookDTO> createPaginatedResult(List<BookDTO> books, Pageable
	// pageable) {
	// // Find the subset of books for the current page
	// int start = (int) pageable.getOffset();
	// int end = Math.min((start + pageable.getPageSize()), books.size());

	// List<BookDTO> paginatedBooks = books.subList(start, end);
	// return new PageImpl<>(paginatedBooks, pageable, books.size());
	// }

	public Book addBook(Book book) {
		return bookRepository.save(book);
	}

	public Book saveBook(Book book) {
		return bookRepository.save(book);
	}

	public void deleteBook(Long id) {
		bookRepository.deleteById(id);
	}

	public BookForm updateBook(Long bookId, BookForm bookForm) {
		Book book = bookRepository.findBookById(bookId);
		book.setTitle(bookForm.getTitle());
		book.setAuthor(bookForm.getAuthor());
		book.setIsbn(bookForm.getIsbn());
		book.setLanguage(bookForm.getLanguage());
		book.setBookFormat(bookForm.getBookFormat());
		book.setPages(bookForm.getPages());
		book.setPrice(bookForm.getPrice());
		book.setCoverImg(bookForm.getCoverImg());
		book.setPublicationYear(bookForm.getPublicationYear());
		bookRepository.save(book);

		return bookForm;
	}
}
