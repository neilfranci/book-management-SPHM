package com.bgsix.bookmanagement.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("ADMIN")
public class Admin extends User {

	public void manageMember(Member member) {
		// Example business logic for managing a member
	}
}
