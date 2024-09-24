package com.bgsix.bookmanagement.model;

import java.time.LocalDate;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("MEMBER")
public class Member extends User {

	public void borrowBook(Book book) {
		// Example logic for borrowing a book
	}

	public Member() {
		this.role = "MEMBER";
		this.dateJoined = LocalDate.now();
	}
}
