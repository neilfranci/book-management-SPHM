package com.bgsix.bookmanagement.model;

import java.time.LocalDate;

import com.bgsix.bookmanagement.enums.UserRole;
import com.bgsix.bookmanagement.enums.UserStatus;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("LIBRARIAN")
public class Librarian extends User {

	public Librarian() {
		this.role = UserRole.LIBRARIAN;
		this.gender = "Other";
		this.status = UserStatus.INACTIVE;
		this.dateJoined = LocalDate.now();
	}
}
