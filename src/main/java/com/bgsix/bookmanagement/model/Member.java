package com.bgsix.bookmanagement.model;

import java.time.LocalDate;
import com.bgsix.bookmanagement.enums.*;
import jakarta.persistence.*;

@Entity
@DiscriminatorValue("MEMBER")
public class Member extends User {

	public Member() {
		this.setRole(UserRole.MEMBER);
		this.setGender("Other");
		this.setStatus(UserStatus.INACTIVE);
		this.setDateJoined(LocalDate.now());
	}

	@Override
	public void displayInfo() {
		System.out.println("Member name: " + this.getName());
		System.out.println("Email: " + this.getEmail());
	}
}