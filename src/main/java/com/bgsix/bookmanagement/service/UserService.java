package com.bgsix.bookmanagement.service;

// MemberService.java

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.bgsix.bookmanagement.dto.SignInForm;
import com.bgsix.bookmanagement.dto.SignUpForm;
import com.bgsix.bookmanagement.model.Member;
import com.bgsix.bookmanagement.model.User;
import com.bgsix.bookmanagement.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

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
		return (User) userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).get();
	}
}
