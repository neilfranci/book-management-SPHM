package com.bgsix.bookmanagement.dto.genre;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TopGenreDTO {
	private String genreName;

	private Integer numberOfGenres;
}
