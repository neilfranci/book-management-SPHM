package com.bgsix.bookmanagement.model;

import java.time.LocalDate;

import com.bgsix.bookmanagement.enums.*;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("MEMBER")
public class Member extends User {

	public Member() {
		this.role = UserRole.MEMBER;
		this.gender = "Other";
		this.status = UserStatus.INACTIVE;
		this.dateJoined = LocalDate.now();
	}
}
