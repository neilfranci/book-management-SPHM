package com.bgsix.bookmanagement.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

import com.bgsix.bookmanagement.enums.UserRole;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "user_type", discriminatorType = DiscriminatorType.STRING)
@Entity
@Table(name = "users")
public abstract class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long userId;

	String name;

	// Male, Female, Other
	String gender;

	String email;

	String passwordHash;
	
	UserRole role;
	// Active, Inactive, Suspended
	String status;

	LocalDate dateOfBirth;

	LocalDate dateJoined;
}
