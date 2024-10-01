package com.bgsix.bookmanagement.dto;

import java.time.LocalDate;

@lombok.Data
public class UserForm {
	private String name;
	private String email;
	private String gender;
	private String role;
	private String status;
	private LocalDate dateOfBirth;
}
