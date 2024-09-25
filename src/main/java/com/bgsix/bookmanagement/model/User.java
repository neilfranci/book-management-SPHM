package com.bgsix.bookmanagement.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

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

	// Admin, Member
	String role;

	// Active, Inactive, Suspended
	String status;

	LocalDate dateOfBirth;

	LocalDate dateJoined;
}
