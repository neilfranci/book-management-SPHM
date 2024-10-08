package com.bgsix.bookmanagement.dto;

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

	@Override
	public String toString() {
		return "BookForm [title=" + title + ", author=" + author + ", isbn=" + isbn + ", language=" + language
				+ ", bookFormat=" + bookFormat + ", pages=" + pages + ", price=" + price + ", coverImg=" + coverImg
				+ ", publicationYear=" + publicationYear + ", quantity=" + quantity + "]";
	}
}
