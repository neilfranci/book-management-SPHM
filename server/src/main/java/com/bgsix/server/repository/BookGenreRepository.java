package com.bgsix.server.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.bgsix.server.model.Book;
import com.bgsix.server.model.BookGenre;
import com.bgsix.server.model.Genre;

@Repository
public interface BookGenreRepository extends JpaRepository<BookGenre, Long> {

	@Query("SELECT bg.genre FROM BookGenre bg WHERE bg.book.bookId = :bookId")
	List<Genre> findGenresByBookId(@Param("bookId") Long bookId);

	@Query("SELECT bg.genre FROM BookGenre bg WHERE bg.book.bookId IN :bookIds")
	List<Genre> findGenresByBookIds(@Param("bookIds") List<Long> bookIds);

	List<BookGenre> findByBookIn(List<Book> books);
}
