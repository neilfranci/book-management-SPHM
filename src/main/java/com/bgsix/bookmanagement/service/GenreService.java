package com.bgsix.bookmanagement.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

import com.bgsix.bookmanagement.dto.TopGenreDTO;
import com.bgsix.bookmanagement.repository.GenreRepository;

@Service
public class GenreService {

	private GenreRepository genreRepository;

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
