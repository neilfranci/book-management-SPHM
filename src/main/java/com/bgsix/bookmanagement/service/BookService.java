package com.bgsix.bookmanagement.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.bgsix.bookmanagement.dto.BookForm;
import com.bgsix.bookmanagement.interfaces.IBookService;
import com.bgsix.bookmanagement.model.Book;
import com.bgsix.bookmanagement.model.Genre;
import com.bgsix.bookmanagement.repository.BookRepository;

@Service
public class BookService implements IBookService {

	@Autowired
	private GenreService genreService;

	@Override
	public Book addBook(BookForm addBookForm) {
		Book book = new Book(addBookForm);

		List<Genre> genres = genreService.getGenresByName(addBookForm.getGenres());

		book.setGenres(genres);

		return bookRepository.save(book);
	}

	@Autowired
	private BookRepository bookRepository;
	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(BookService.class);

	@Override
	public Book saveBook(Book book) {
		return bookRepository.save(book);
	}

	@Override
	public void deleteBook(Long id) {
		bookRepository.deleteById(id);
	}

	@Override
	public Book findById(Long id) {
		Book book = bookRepository.findById(id).orElseThrow(() -> new RuntimeException("Book not found"));
		return book;
	}

	@Override
	public Page<Book> findByTitleAndGenres(String title, List<String> genres, Pageable pageable) {
		return bookRepository.findByTitleAndGenres(title, genres, genres.size(), pageable);
	}

	@Override
	public BookForm updateBook(Long bookId, BookForm bookForm) {
		Book book = bookRepository.findById(bookId).orElseThrow(() -> new RuntimeException("Book not found"));
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

	@Override
	public Page<Book> searchBooks(String searchInput, String searchBy, List<String> selectedGenres, String sortBy,
			int page, int size) {
		Sort sort = Sort.by(Sort.Direction.ASC, "title"); // Default sort

		// Set sort based on sortBy parameter
		if (sortBy != null) {
			switch (sortBy) {
			case "relevance_asc":
				sort = Sort.by(Sort.Direction.ASC, "title");
				break;
			case "relevance_desc":
				sort = Sort.by(Sort.Direction.DESC, "title");
				break;
			case "rating_asc":
				sort = Sort.by(Sort.Direction.ASC, "rating");
				break;
			case "rating_desc":
				sort = Sort.by(Sort.Direction.DESC, "rating");
				break;
			default:
				logger.info("Invalid sort option, using default sorting by title.");
			}
		}

		Pageable pageable = PageRequest.of(page, size, sort);

		// Handle search conditions based on searchBy parameter
		if (searchInput != null && !searchInput.trim().isEmpty()) {
			switch (searchBy) {
			case "title":
				// Search by title with optional genre filtering
				if (selectedGenres != null && !selectedGenres.isEmpty()) {
					logger.info("Searching by title and genres");
					return findByTitleAndGenres(searchInput, selectedGenres, pageable);
				}
				logger.info("Searching by title");
				return bookRepository.findByTitleContainingIgnoreCase(searchInput, pageable);

			case "author":
				// Search by author with optional genre filtering
				if (selectedGenres != null && !selectedGenres.isEmpty()) {
					logger.info("Searching by author and genres");
					return findByAuthorAndGenres(searchInput, selectedGenres, pageable);
				}
				logger.info("Searching by author");
				return bookRepository.findByAuthorContainingIgnoreCase(searchInput, pageable);

			case "isbn":
				// Search by ISBN
				logger.info("Searching by ISBN");
				return bookRepository.findByIsbnContainingIgnoreCase(searchInput, pageable);

			default:
				logger.warn("Unknown searchBy parameter, defaulting to title search.");
				return bookRepository.findByTitleContainingIgnoreCase(searchInput, pageable);
			}
		}

		// If only genres are provided, filter by genres
		if (selectedGenres != null && !selectedGenres.isEmpty()) {
			logger.info("Searching by genres");
			return bookRepository.findByGenres(selectedGenres, pageable);
		}

		// If no searchInput or genres are provided, return all books sorted by the
		// specified sort order
		logger.info("No search criteria provided, returning all books with sort: " + sort);
		return bookRepository.findAll(pageable);
	}

	private Page<Book> findByAuthorAndGenres(String searchInput, List<String> selectedGenres, Pageable pageable) {
		return bookRepository.findByAuthorAndGenres(searchInput, selectedGenres, selectedGenres.size(), pageable);
	}

}
