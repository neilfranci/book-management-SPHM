package com.bgsix.server.dto;

import java.util.List;

import com.bgsix.server.model.Book;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BookDTO {
	private Long bookId;

	private String title;

	private String language;

	private String author;

	private Float rating;

	private String isbn;

	private String bookFormat;

	private Integer pages;

	private Integer numRatings;

	private Short likedPercent;

	private Double price;

	private String coverImg;

	private Integer publicationYear;

	private List<String> genres;

	public BookDTO(Book book, List<String> genres) {
		this.bookId = book.getBookId();
		this.title = book.getTitle();
		this.language = book.getLanguage();
		this.author = book.getAuthor();
		this.rating = book.getRating();
		this.isbn = book.getIsbn();
		this.bookFormat = book.getBookFormat();
		this.pages = book.getPages();
		this.numRatings = book.getNumRatings();
		this.likedPercent = book.getLikedPercent();
		this.price = book.getPrice();
		this.coverImg = book.getCoverImg();
		this.publicationYear = book.getPublicationYear();
		this.genres = genres;
	}

}
