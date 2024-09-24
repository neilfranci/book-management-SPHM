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
	private Long userId;

	private String name;

	// Male, Female, Other
	private String gender;

	private String email;

	private String passwordHash;

	// Admin, Member
	String role;

	// Active, Inactive, Suspended
	private String membershipStatus;

	private LocalDate dateOfBirth;

	LocalDate dateJoined;
}
