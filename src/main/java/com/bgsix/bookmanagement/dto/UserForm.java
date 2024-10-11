package com.bgsix.bookmanagement.dto;

import java.time.LocalDate;

import com.bgsix.bookmanagement.enums.UserRole;

@lombok.Data
public class UserForm {
	private String name;
	private String email;
	private String gender;
	private UserRole role;
	private String status;
	private LocalDate dateOfBirth;
}
