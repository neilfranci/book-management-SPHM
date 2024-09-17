package com.bgsix.bookmanagement.service;

import java.util.ArrayList;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.bgsix.bookmanagement.controller.api.BookApiController;
import com.bgsix.bookmanagement.dto.genre.TopGenreDTO;
import com.bgsix.bookmanagement.repository.GenreRepository;

@Service
public class GenreService {

	private GenreRepository genreRepository;

	private static final Logger logger = LoggerFactory.getLogger(BookApiController.class);

	public GenreService(GenreRepository genreRepository) {
		this.genreRepository = genreRepository;
	}

	public List<TopGenreDTO> getTopGenres() {
		List<Object[]> results = genreRepository.findTopGenres();

		List<TopGenreDTO> topGenres = new ArrayList<TopGenreDTO>();

		for (Object[] result : results) {
			String genreName = (String) result[0];
			Long numberOfBooks = ((Number) result[1]).longValue(); // Casting to Long

			TopGenreDTO dto = new TopGenreDTO(genreName, numberOfBooks.intValue());
			topGenres.add(dto);
		}

		return topGenres;
	}

}
