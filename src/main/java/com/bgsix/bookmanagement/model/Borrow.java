package com.bgsix.bookmanagement.model;

import java.time.LocalDate;

import com.bgsix.bookmanagement.enums.BorrowStatus;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "borrow_book")
public class Borrow {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long borrowId;
	Long bookId;
	Long userId;
	LocalDate borrowDate;
	LocalDate dueDate;
	LocalDate returnedDate;
	Double fine = 0.0;
	BorrowStatus status = BorrowStatus.BORROWED;
}
