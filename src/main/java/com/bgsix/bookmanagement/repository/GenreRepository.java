package com.bgsix.bookmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

import com.bgsix.bookmanagement.model.Genre;

public interface GenreRepository extends JpaRepository<Genre, Long> {

	@Query(value = """
			SELECT
				G.NAME as genreName,
				COUNT(BG.BOOK_ID) AS numberOfBooks
			FROM
				BOOK_GENRE BG
				JOIN GENRE G ON BG.GENRE_ID = G.GENRE_ID
			GROUP BY
				G.NAME
			ORDER BY
				numberOfBooks DESC
			LIMIT 20;
			""", nativeQuery = true)
	List<Object[]> findTopGenres();

}
