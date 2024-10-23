package com.bgsix.bookmanagement.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.bgsix.bookmanagement.model.Genre;
import com.bgsix.bookmanagement.repository.GenreRepository;

@Service
public class GenreService {

	private GenreRepository genreRepository;

	public GenreService(GenreRepository genreRepository) {
		this.genreRepository = genreRepository;
	}

	public List<String> getTopGenres() {
		List<Genre> genres = genreRepository.findTopGenres();

		int numberOfGenres = Math.min(200, genres.size());

		// Convert List<Genre> to List<String>
		List<String> genreNames = new ArrayList<>();
		for (int i = 0; i < genres.size() && i < numberOfGenres; i++) {
			genreNames.add(genres.get(i).getName());
		}

		return genreNames;
	}

	public List<Genre> getGenresByName(List<String> genres) {
		List<Genre> genreList = new ArrayList<>();
		for (String genreName : genres) {
			Genre genre = genreRepository.findByName(genreName);
			genreList.add(genre);
		}
		return genreList;
	}

}
