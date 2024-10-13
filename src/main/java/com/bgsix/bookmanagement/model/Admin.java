package com.bgsix.bookmanagement.model;

import com.bgsix.bookmanagement.enums.UserRole;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("ADMIN")
public class Admin extends User {

	public Admin() {
		this.role = UserRole.ADMIN;
	}

}
