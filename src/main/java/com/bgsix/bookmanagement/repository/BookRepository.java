package com.bgsix.bookmanagement.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.bgsix.bookmanagement.model.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
	Page<Book> findByTitleContainingIgnoreCase(String title, Pageable pageable);

	@Query("SELECT b FROM Book b JOIN b.genres g WHERE g.name IN :genres")
	Page<Book> findByGenres(@Param("genres") List<String> genres, Pageable pageable);

	@Query("""
			    SELECT b
			    FROM Book b
			    JOIN b.genres g
			    WHERE LOWER(b.title) LIKE LOWER(CONCAT('%', :title, '%'))
			    AND g.name IN :genres
			    GROUP BY b
			    HAVING COUNT(DISTINCT g.name) = :genreCount
			""")
	Page<Book> findByTitleAndGenres(@Param("title") String title, @Param("genres") List<String> genres,
			@Param("genreCount") long genreCount, Pageable pageable);

	Page<Book> findByAuthorContainingIgnoreCase(String author, Pageable pageable);

	Page<Book> findByIsbnContainingIgnoreCase(String isbn, Pageable pageable);

	Page<Book> findByRatingGreaterThanEqual(Float rating, Pageable pageable);

	@Query("""
			SELECT b
			FROM Book b
			JOIN b.genres g
			WHERE LOWER(b.author) LIKE LOWER(CONCAT('%', :author, '%'))
			AND g.name IN :genres
			GROUP BY b
			HAVING COUNT(DISTINCT g.name) = :genreCount
			""")
	Page<Book> findByAuthorAndGenres(@Param("author") String author, @Param("genres") List<String> selectedGenres,
			@Param("genreCount") long genreCount, Pageable pageable);
}
