package com.bgsix.bookmanagement.model;

import java.time.LocalDate;

import com.bgsix.bookmanagement.enums.UserRole;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("Librarian")
public class Librarian extends User {


	public Librarian() {
		this.role = UserRole.Librarian;
		this.gender = "Other";
		this.status = "Active";
		this.dateJoined = LocalDate.now();
	}
}
