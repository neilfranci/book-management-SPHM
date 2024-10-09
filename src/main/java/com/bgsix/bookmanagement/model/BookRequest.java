package com.bgsix.bookmanagement.model;

import lombok.*;

import java.time.LocalDate;

import jakarta.persistence.*;
import jakarta.persistence.GenerationType;

@Data
@Entity
@Table(name = "book_request")
public class BookRequest {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long requestId;
	Long bookId;
	Long userId;
	Long librarianId;
	LocalDate requestDate;
	String requestStatus; // Pending, Approved, Rejected
	LocalDate approvalDate;

	public BookRequest() {
		this.requestStatus = "Pending";
	}
}
