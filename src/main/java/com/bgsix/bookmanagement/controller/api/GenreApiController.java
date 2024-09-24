package com.bgsix.bookmanagement.controller.api;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bgsix.bookmanagement.dto.TopGenreDTO;
import com.bgsix.bookmanagement.service.GenreService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api/genre")
public class GenreApiController {
	GenreService genreService;

	private static final Logger logger = LoggerFactory.getLogger(BookApiController.class);

	public GenreApiController(GenreService genreService) {
		this.genreService = genreService;
	}

	@Operation(summary = "Get all genres", description = "Get all genres", tags = { "genre" })
	@GetMapping("/getTopGenres")
	public ResponseEntity<List<TopGenreDTO>> getAllGenres() {
		List<TopGenreDTO> topGenreDTOs = genreService.getTopGenres();

		if (topGenreDTOs.isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		return ResponseEntity.ok(topGenreDTOs);
	}
}
