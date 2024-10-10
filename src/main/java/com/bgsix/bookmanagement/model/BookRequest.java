package com.bgsix.bookmanagement.model;

import lombok.*;

import java.time.LocalDate;

import com.bgsix.bookmanagement.enums.RequestStatus;

import jakarta.persistence.*;

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
	LocalDate approvalDate;
	RequestStatus status;
}
