package com.bgsix.bookmanagement.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// CREATE TABLE genre (
//     genre_id integer NOT NULL,
//     name character varying(50) NOT NULL
// );

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Genre {
	@Id
	@Column(name = "GENRE_ID")
	private Long genreId;
	private String name;

	@OneToMany(mappedBy = "genre", cascade = CascadeType.ALL)
	private List<BookGenre> bookGenres = new ArrayList<BookGenre>();
}
