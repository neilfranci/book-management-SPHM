package com.bgsix.bookmanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.bgsix.bookmanagement.dto.*;
import com.bgsix.bookmanagement.enums.*;
import com.bgsix.bookmanagement.model.*;
import com.bgsix.bookmanagement.repository.UserRepository;

import java.util.*;

@Service
public class UserService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public List<User> findAll() {
		return userRepository.findAll();
	}

	public Optional<User> findById(Long id) {
		return userRepository.findById(id);
	}

	public Member save(Member member) {
		return userRepository.save(member);
	}

	public void deleteById(Long id) {
		userRepository.deleteById(id);
	}

	public boolean authenticate(SignInForm signInForm) {
		Optional<User> user = userRepository.findByEmail(signInForm.getEmail());
		if (user.isPresent()) {
			return passwordEncoder.matches(signInForm.getPassword(), user.get().getPasswordHash());
		}
		return false;
	}

	public Member registerMember(SignUpForm signupForm) {
		Member member = new Member();

		member.setName(signupForm.getName());
		member.setEmail(signupForm.getEmail());
		member.setPasswordHash(passwordEncoder.encode(signupForm.getPassword()));
		return save(member);
	}

	public Optional<User> findByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	public User getCurrentUser() {
		return (User) userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName())
				.get();
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		// Find user by email (or username) from the database
		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("User not found"));
		// Convert User to Spring Security's UserDetails
		return org.springframework.security.core.userdetails.User.withUsername(user.getEmail())
				.password(user.getPasswordHash()).roles(user.getRole().name()).build();
	}

	public List<User> getAllUsers() {
		return userRepository.findAll();
	}

	public User getUserById(Long userId) {
		return userRepository.findById(userId).orElse(null);
	}



	public Map<Integer, String> addUser(UserForm userForm) {
		if (userRepository.findByEmail(userForm.getEmail()).isPresent()) {
			return Map.of(0, "This email is already registered.");
		}
	
		User user;
		String successMessage;
	
		switch (userForm.getRole()) {
			case MEMBER:
				user = new Member();
				successMessage = "Member added successfully.";
				break;
			case LIBRARIAN:
				user = new Librarian();
				successMessage = "Librarian added successfully.";
				break;
			case ADMIN:
				user = new Admin();
				successMessage = "Admin added successfully.";
				break;
			default:
				return Map.of(2, "Invalid user role.");
		}
	
		setUserDetail(user, userForm);
		userRepository.save(user);
	
		return Map.of(1, successMessage);
	}
	
	private void setUserDetail(User user, UserForm userForm) {
		user.setName(userForm.getName());
		user.setEmail(userForm.getEmail());
		user.setPasswordHash(passwordEncoder.encode(userForm.getPassword()));
		user.setGender(userForm.getGender());
		user.setDateOfBirth(userForm.getDateOfBirth());
		user.setStatus(UserStatus.ACTIVE);
	}
	

	public void updateUser(Long userId, UserForm userForm) {
		User user = userRepository.findById(userId).orElse(null);
		if (user != null) {
			user.setName(userForm.getName());
			user.setEmail(userForm.getEmail());
			user.setGender(userForm.getGender());
			user.setStatus(userForm.getStatus());
			user.setDateOfBirth(userForm.getDateOfBirth());
			userRepository.save(user);
		}
	}

	public void deleteUser(Long userId) {
		userRepository.deleteById(userId);
	}
}
