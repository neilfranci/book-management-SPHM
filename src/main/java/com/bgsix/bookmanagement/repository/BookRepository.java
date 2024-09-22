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

	@Query(value = """
			SELECT B.*
			FROM BOOK B
			JOIN BOOK_GENRE BG ON B.BOOK_ID = BG.BOOK_ID
			JOIN GENRE G ON BG.GENRE_ID = G.GENRE_ID
			WHERE LOWER(G.NAME) IN (:genres)
			GROUP BY B.BOOK_ID
			HAVING COUNT(DISTINCT G.NAME) = :genreCount
			""", nativeQuery = true)
	Page<Book> findByGenre(@Param("genres") List<String> genres, @Param("genreCount") long genreCount,
			Pageable pageable);

	@Query(value = """
			SELECT B.*
			FROM BOOK B
			JOIN BOOK_GENRE BG ON B.BOOK_ID = BG.BOOK_ID
			JOIN GENRE G ON BG.GENRE_ID = G.GENRE_ID
			WHERE LOWER(G.NAME) IN (:genres)
			GROUP BY B.BOOK_ID
			HAVING COUNT(DISTINCT G.NAME) = :genreCount
			""", nativeQuery = true)
	List<Book> getBooksByGenres(List<String> genres, long genreCount);

	@Query(value = """
			SELECT B.BOOK_ID, array_agg(DISTINCT G.NAME) AS genres
			FROM BOOK B
			JOIN BOOK_GENRE BG ON B.BOOK_ID = BG.BOOK_ID
			JOIN GENRE G ON BG.GENRE_ID = G.GENRE_ID
			WHERE B.BOOK_ID IN (:bookIds)
			GROUP BY B.BOOK_ID
			""", nativeQuery = true)
	List<Object[]> findGenresForBookIds(@Param("bookIds") List<Long> bookIds);

	@Query(value = """
			SELECT array_agg(DISTINCT G.NAME) AS genres
			     FROM BOOK B
			     JOIN BOOK_GENRE BG ON B.BOOK_ID = BG.BOOK_ID
			     JOIN GENRE G ON BG.GENRE_ID = G.GENRE_ID
			     WHERE B.BOOK_ID = :bookId
			     """, nativeQuery = true)
	String[] findGenresForBookId(@Param("bookId") Long bookId);

	Page<Book> findByAuthorContainingIgnoreCase(String author, Pageable pageable);

	Page<Book> findByIsbnContainingIgnoreCase(String isbn, Pageable pageable);

	Page<Book> findByRatingGreaterThanEqual(Float rating, Pageable pageable);

	// @Query(value = """
	// SELECT B.*
	// FROM BOOK B
	// JOIN BOOK_GENRE BG ON B.BOOK_ID = BG.BOOK_ID
	// JOIN GENRE G ON BG.GENRE_ID = G.GENRE_ID
	// WHERE LOWER(G.NAME) IN (:genres)
	// AND LOWER(B.TITLE) LIKE LOWER(CONCAT('%', :title, '%'))
	// GROUP BY B.BOOK_ID
	// HAVING COUNT(DISTINCT G.NAME) = :genreCount
	// """, nativeQuery = true)
	// Page<Book> findByGenreAndTitle(@Param("genres") List<String> genres,
	// @Param("genreCount") long genreCount,
	// @Param("title") String title, Pageable pageable);

	@Query(value = """
			SELECT
				*
			FROM
				BOOK
			WHERE
				BOOK_ID = :id
			""", nativeQuery = true)
	Book findBookById(long id);

	@Query(value = """
			SELECT
				*
			FROM
				BOOK
			ORDER BY
				RATING DESC
			""", nativeQuery = true)
	Page<Book> findTopRate(Pageable pageable);
}
