package com.bgsix.bookmanagement.dto;

import java.time.LocalDate;

import com.bgsix.bookmanagement.enums.BorrowStatus;
import com.bgsix.bookmanagement.model.Borrow;

import lombok.*;

@Data
public class BorrowedBookDTO {
	Long borrowId;
	Long userId;
	LocalDate borrowDate;
	LocalDate dueDate;
	LocalDate returnedDate;
	BorrowStatus status;
	String title;
	String author;
	Double fine;

	public BorrowedBookDTO(Borrow borrow, String title, String author) {
		this.borrowId = borrow.getBorrowId();
		this.userId = borrow.getUserId();
		this.borrowDate = borrow.getBorrowDate();
		this.dueDate = borrow.getDueDate();
		this.returnedDate = borrow.getReturnedDate();
		this.status = borrow.getStatus();
		this.fine = borrow.getFine();
		this.title = title;
		this.author = author;
	}
}
