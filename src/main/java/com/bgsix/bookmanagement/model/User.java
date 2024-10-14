package com.bgsix.bookmanagement.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

import com.bgsix.bookmanagement.enums.UserRole;
import com.bgsix.bookmanagement.enums.UserStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "role", discriminatorType = DiscriminatorType.STRING)
@Entity
@Table(name = "users")
public abstract class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long userId;

	private String name;
	
	private String email;
	
	private String passwordHash;

	private LocalDate dateOfBirth;
	// Male, Female, Other
	String gender;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "role", insertable = false, updatable = false)
	UserRole role;
	
	@Enumerated(EnumType.STRING)
	UserStatus status;
	
	LocalDate dateJoined;
}
