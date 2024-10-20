package com.bgsix.bookmanagement.model;

import java.util.List;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Genre {
	@Id
	@Column(name = "GENRE_ID")
	private Long genreId;
	private String name;

	@ManyToMany(mappedBy = "genres")
    private List<Book> books;

	@Override
	public String toString() {
		return "Genre [genreId=" + genreId + ", name=" + name + "]";
	}
}
