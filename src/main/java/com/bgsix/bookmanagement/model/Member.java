package com.bgsix.bookmanagement.model;

import java.time.LocalDate;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("Member")
public class Member extends User {

	public void borrowBook(Book book) {
		// Example logic for borrowing a book
	}

	public Member() {
		this.role = "Member";
		this.gender = "Other";
		this.status = "Active";
		this.dateJoined = LocalDate.now();
	}
}
