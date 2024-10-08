package com.bgsix.bookmanagement.dto;

import java.util.Collections;
import java.util.List;

import com.bgsix.bookmanagement.model.Book;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class BookDTO extends Book {
	private List<String> genres;

	public BookDTO(Book book, List<String> genres) {
		this.setBookId(book.getBookId());
		this.setTitle(book.getTitle());
		this.setAuthor(book.getAuthor());
		this.setIsbn(book.getIsbn());
		this.setLanguage(book.getLanguage());
		this.setBookFormat(book.getBookFormat());
		this.setRating(book.getRating());
		this.setLikedPercent(book.getLikedPercent());
		this.setNumRatings(book.getNumRatings());
		this.setPages(book.getPages());
		this.setPrice(book.getPrice());
		this.setCoverImg(book.getCoverImg());
		this.setPublicationYear(book.getPublicationYear());
		this.setQuantity(book.getQuantity());

		this.genres = genres != null ? genres : Collections.emptyList();
	}
}
