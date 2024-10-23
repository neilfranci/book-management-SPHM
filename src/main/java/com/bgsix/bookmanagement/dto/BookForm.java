package com.bgsix.bookmanagement.dto;

import java.util.List;

import lombok.*;

@Data
@NoArgsConstructor
public class BookForm {
	String title;
	String author;
	String isbn;
	String language;
	String bookFormat;
	Integer pages;
	Double price;
	String coverImg;
	Integer publicationYear;
	Integer quantity;
	List<String> genres;

	@Override
	public String toString() {
		return "BookForm [title=" + title + ", author=" + author + ", isbn=" + isbn + ", language=" + language
				+ ", bookFormat=" + bookFormat + ", pages=" + pages + ", price=" + price + ", coverImg=" + coverImg
				+ ", publicationYear=" + publicationYear + ", quantity=" + quantity + ", genres=" + genres + "]";
	}

	public boolean validate() {
		if (title == null || title.isEmpty()) {
			return false;
		}
		if (author == null || author.isEmpty()) {
			return false;
		}
		if (isbn == null || isbn.isEmpty()) {
			return false;
		}
		if (language == null || language.isEmpty()) {
			return false;
		}
		if (bookFormat == null || bookFormat.isEmpty()) {
			return false;
		}
		if (pages == null) {
			return false;
		}
		if (price == null) {
			return false;
		}
		return true;
	}
}
