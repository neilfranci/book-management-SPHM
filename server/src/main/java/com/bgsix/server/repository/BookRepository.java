package com.bgsix.server.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.bgsix.server.model.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
	Page<Book> findByTitleContainingIgnoreCase(String title, Pageable pageable);

	@Query(value = """
			SELECT
			    B.*
			FROM
			    BOOK B
			    JOIN BOOK_GENRE BG ON B.BOOK_ID = BG.BOOK_ID
			    JOIN GENRE G ON BG.GENRE_ID = G.GENRE_ID
			WHERE
				LOWER(G.NAME) IN (:genres)
			GROUP BY
			    B.BOOK_ID
			HAVING
			    COUNT(DISTINCT G.GENRE_ID) = :genreCount
			""", nativeQuery = true)
	Page<Book> findByGenres(@Param("genres") List<String> genres, @Param("genreCount") long genreCount,
			Pageable pageable);

	Page<Book> findByAuthorContainingIgnoreCase(String author, Pageable pageable);

	Page<Book> findByIsbnContainingIgnoreCase(String isbn, Pageable pageable);

	Page<Book> findByRatingGreaterThanEqual(Float rating, Pageable pageable);
}
