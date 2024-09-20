package com.bgsix.bookmanagement.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookResponse {

	private List<BookDTO> books;
	private int totalPages;
	private long totalElements;

}
