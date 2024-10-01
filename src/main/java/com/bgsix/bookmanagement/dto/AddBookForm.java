package com.bgsix.bookmanagement.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class AddBookForm {
	String title;
	String author;
	String isbn;
	String language;
	String bookFormat;
	Integer pages;
	Double price;
	String coverImg;
	Integer publicationYear;

	@Override
	public String toString() {
		return "AddBookForm [title=" + title + ", author=" + author + ", isbn=" + isbn + ", language=" + language
				+ ", bookFormat=" + bookFormat + ", pages=" + pages + ", price=" + price + ", coverImg=" + coverImg
				+ ", publicationYear=" + publicationYear + "]";
	}
}
