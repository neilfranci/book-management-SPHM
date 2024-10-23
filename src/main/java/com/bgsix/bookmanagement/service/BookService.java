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

			return bookRepository.findByTitleContainingIgnoreCase(searchInput, pageable);
		}

		// If no searchInput is provided, genre is provided, search by genres
		if (!selectedGenres.isEmpty()) {
			logger.info("Searching by genres");
			return bookRepository.findByGenres(selectedGenres, pageable);
		}

		// If only sortBy is provided, sort by property based on sort
		if (sortBy != null) {
			for (Sort.Order order : sort) {
				logger.info("Sort by: " + order.getProperty() + " " + order.getDirection());
			}
			return bookRepository.findAll(pageable);
		}

		// If no criteria are provided, return all books or a default sorted list
		return bookRepository.findAll(pageable);
	}
}
