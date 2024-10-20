package com.bgsix.bookmanagement.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.bgsix.bookmanagement.dto.BookForm;
import com.bgsix.bookmanagement.model.Book;
import com.bgsix.bookmanagement.repository.BookRepository;

@Service
public class BookService {
	@Autowired
	private BookRepository bookRepository;

	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(BookService.class);

	public Book addBook(Book book) {
		return bookRepository.save(book);
	}

	public Book saveBook(Book book) {
		return bookRepository.save(book);
	}

	public void deleteBook(Long id) {
		bookRepository.deleteById(id);
	}

	public Page<Book> findAll(Pageable pageable) {
		Page<Book> booksPage = bookRepository.findAll(pageable);
		return booksPage;
	}

	public Book findById(Long id) {
		Book book = bookRepository.findById(id).orElseThrow(() -> new RuntimeException("Book not found"));

		return book;
	}

	public Page<Book> findByTitle(String title, Pageable pageable) {
		Page<Book> booksPage = bookRepository.findByTitleContainingIgnoreCase(title, pageable);

		return booksPage;
	}

	public Page<Book> findByGenres(List<String> genres, Pageable pageable) {
		Page<Book> booksPage = bookRepository.findByGenres(genres, pageable);

		return booksPage;
	}

	public Page<Book> getBooksByAuthor(String author, Pageable pageable) {
		Page<Book> booksPage = bookRepository.findByAuthorContainingIgnoreCase(author, pageable);
		return booksPage;
	}

	public Page<Book> getBooksByIsbn(String isbn, Pageable pageable) {
		Page<Book> booksPage = bookRepository.findByIsbnContainingIgnoreCase(isbn, pageable);
		return booksPage;
	}

	public Page<Book> getBooksByRating(Float rating, Pageable pageable) {
		Page<Book> booksPage = bookRepository.findByRatingGreaterThanEqual(rating, pageable);
		return booksPage;
	}

	public Page<Book> getTopRateBooks(Pageable pageable) {
		Page<Book> booksPage = bookRepository.findTopRate(pageable);
		return booksPage;
	}

	public Page<Book> findByTitleAndGenres(String title, List<String> genres, Pageable pageable) {
		return bookRepository.findByTitleAndGenres(title, genres, genres.size(), pageable);
	}

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

	public Page<Book> searchBooks(String searchInput, List<String> selectedGenres, String sortBy, int page, int size) {
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
			}
		}

		Pageable pageable = PageRequest.of(page, size, sort);

		// If both searchInput and selectedGenres are provided, search by title and
		// genres
		if (!searchInput.isEmpty() && !selectedGenres.isEmpty()) {
			logger.info("Searching by title and genres");
			return findByTitleAndGenres(searchInput, selectedGenres, pageable);
		}

		// If only searchInput is provided, search by title
		if (searchInput != null && !searchInput.isEmpty()) {
			logger.info("Searching by title");
			return findByTitle(searchInput, pageable);
		}

		// If no searchInput is provided, genre is provided, search by genres
		if (!selectedGenres.isEmpty()) {
			logger.info("Searching by genres");
			return findByGenres(selectedGenres, pageable);
		}

		// If only sortBy is provided, sort by rating
		if (sortBy != null) {
			logger.info("Sorting by rating");
			return findTopRate(pageable);
		}

		// If no criteria are provided, return all books or a default sorted list
		return findAll(pageable);
	}

	public Page<Book> findTopRate(Pageable pageable) {
		Page<Book> booksPage = bookRepository.findTopRate(pageable);

		// for (Book book : booksPage.getContent()) {
		// logger.info("Book : " + book.getRating());
		// }
		return booksPage;
	}

}
