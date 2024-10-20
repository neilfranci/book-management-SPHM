package com.bgsix.bookmanagement.model;

import java.time.LocalDate;
import com.bgsix.bookmanagement.enums.*;
import jakarta.persistence.*;

@Entity
@DiscriminatorValue("ADMIN")
public class Admin extends User {

    public Admin() {
        this.setRole(UserRole.ADMIN);
        this.setGender("Other");
        this.setStatus(UserStatus.INACTIVE);
        this.setDateJoined(LocalDate.now());
    }

	
	@Override
	public void displayInfo() {
		System.out.println("Admin name: " + this.getName());
		System.out.println("Email: " + this.getEmail());
	}
}