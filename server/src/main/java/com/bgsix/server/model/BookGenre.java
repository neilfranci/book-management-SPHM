package com.bgsix.server.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "BOOK_GENRE")
public class BookGenre {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "BOOK_GENRE_ID")
	private Long bookGenreId;

	@ManyToOne
	@JoinColumn(name = "BOOK_ID", nullable = false)
	private Book book;

	@ManyToOne
	@JoinColumn(name = "GENRE_ID", nullable = false)
	private Genre genre;
}
