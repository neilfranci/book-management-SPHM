package com.bgsix.bookmanagement.model;

import java.time.LocalDate;

import com.bgsix.bookmanagement.enums.*;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("ADMIN")
public class Admin extends User {

	public Admin() {
		this.role = UserRole.ADMIN;
		this.gender = "Other";
		this.status = UserStatus.INACTIVE;
		this.dateJoined = LocalDate.now();
	}

}
