package com.bgsix.bookmanagement.interfaces;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.bgsix.bookmanagement.dto.BookDTO;
import com.bgsix.bookmanagement.dto.BookForm;
import com.bgsix.bookmanagement.model.Book;

public interface IBookService {
	BookDTO getBookById(Long id);
	Page<BookDTO> getBooksByTitle(String title, Pageable pageable);
	Page<BookDTO> getBooksByGenre(List<String> genreList, Pageable pageable);
	Page<BookDTO> getBooksByAuthor(String author, Pageable pageable);
	Page<BookDTO> getBooksByIsbn(String isbn, Pageable pageable);
	Page<BookDTO> getBooksByRating(Float rating, Pageable pageable);
	Page<BookDTO> getTopRateBooks(Pageable pageable);
	Book addBook(Book book);
	Book saveBook(Book book);
	void deleteBook(Long id);
	BookForm updateBook(Long bookId, BookForm bookForm);
}
