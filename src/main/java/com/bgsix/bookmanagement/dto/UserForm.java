package com.bgsix.bookmanagement.dto;

import java.time.LocalDate;

import com.bgsix.bookmanagement.enums.*;
import lombok.Data;

@Data
public class UserForm {
	private String name;
	private String email;
	private String gender;
	private LocalDate dateOfBirth;
	private UserRole role;
	private UserStatus status;
	private String password;
}
