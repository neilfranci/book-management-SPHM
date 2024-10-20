package com.bgsix.bookmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

import com.bgsix.bookmanagement.model.Genre;

public interface GenreRepository extends JpaRepository<Genre, Long> {

	@Query("""
			    SELECT g
			    FROM Genre g
			    LEFT JOIN g.books b
			    GROUP BY g.id, g.name
			    ORDER BY COUNT(b) DESC
			""")
	List<Genre> findTopGenres();

}
