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

	// Male, Female, Other
	protected String gender;

	private String email;

	private String passwordHash;

	@Enumerated(EnumType.STRING)
	@Column(name = "role", insertable = false, updatable = false)
	protected UserRole role;

	@Enumerated(EnumType.STRING)
	protected UserStatus status;

	private LocalDate dateOfBirth;

	protected LocalDate dateJoined;
}
