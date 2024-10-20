package com.bgsix.bookmanagement.model;

import java.time.LocalDate;
import com.bgsix.bookmanagement.enums.UserRole;
import com.bgsix.bookmanagement.enums.UserStatus;
import jakarta.persistence.*;

@Entity
@DiscriminatorValue("LIBRARIAN")
public class Librarian extends User {

    public Librarian() {
        this.setRole(UserRole.LIBRARIAN);
        this.setGender("Other");
        this.setStatus(UserStatus.INACTIVE);
        this.setDateJoined(LocalDate.now());
    }

	@Override
	public void displayInfo() {
		System.out.println("Librarian name: " + this.getName());
		System.out.println("Email: " + this.getEmail());
	}
}
