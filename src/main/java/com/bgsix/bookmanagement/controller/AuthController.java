package com.bgsix.bookmanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.bgsix.bookmanagement.dto.SignInForm;
import com.bgsix.bookmanagement.dto.SignUpForm;
import com.bgsix.bookmanagement.service.UserService;

import org.slf4j.*;

@Controller
public class AuthController {

	private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

	@Autowired
	private UserService userService;

	@GetMapping("")
	public String getBooks(Model model) {
		return "redirect:/book/home";
	}

	@GetMapping("/login")
	public String showLoginForm(Model model) {
		model.addAttribute("signInForm", new SignInForm());

		return "auth/login";
	}

	@PostMapping("/login")
	public String submitLogin(@ModelAttribute SignInForm signInForm, Model model) {

		logger.info("Email: " + signInForm.getEmail());
		logger.info("Password: " + signInForm.getPassword());

		boolean isAuthenticated = userService.authenticate(signInForm);

		if (isAuthenticated) {
			return "auth/fragments :: login-success";
		} else {
			return "auth/fragments :: login-failed";
		}

	}

	@GetMapping("/signup")
	public String showSignupForm(Model model) {
		model.addAttribute("signUpForm", new SignUpForm());
		return "auth/signup";
	}

	@PostMapping("/signup")
	public String signupMember(@ModelAttribute SignUpForm signUpForm, Model model) {

		if (userService.findByEmail(signUpForm.getEmail()).isPresent()) {
			model.addAttribute("error", "Email already exists");
			return "auth/signup";
		}

		userService.registerMember(signUpForm);
		return "redirect:/login?signupSuccess";
	}
}