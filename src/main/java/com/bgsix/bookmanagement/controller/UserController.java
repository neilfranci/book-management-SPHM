package com.bgsix.bookmanagement.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import com.bgsix.bookmanagement.dto.UserForm;
import com.bgsix.bookmanagement.enums.UserRole;
import com.bgsix.bookmanagement.model.User;
import com.bgsix.bookmanagement.service.*;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/user")
public class UserController {
	@Autowired
	private UserService userService;

	@Autowired
	private BorrowService borrowService;

	@Autowired
	private RequestService requestService;

	private static final Logger logger = LoggerFactory.getLogger(UserController.class);

	@GetMapping("/details")
	public String getUserDetails(Model model) {
		User user = userService.getCurrentUser();

		model.addAttribute("user", user);
		model.addAttribute("requests", requestService.getRequests());

		if (user.getRole() == UserRole.ADMIN || user.getRole() == UserRole.LIBRARIAN) {
			model.addAttribute("members", userService.getAllUsers());
			model.addAttribute("borrowedBooks", borrowService.getBorrowedBooks());
		} else {
			Long userId = userService.getCurrentUser().getUserId();

			model.addAttribute("borrowedBooks", borrowService.getBorrowedBooksForUser(userId));
		}

		return "user/details";
	}

	@GetMapping("/{userId}")
	public String getUserDetails(@PathVariable Long userId, Model model) {
		User user = userService.getUserById(userId);

		UserForm userForm = new UserForm();
		userForm.setName(user.getName());
		userForm.setEmail(user.getEmail());
		userForm.setGender(user.getGender());
		userForm.setDateOfBirth(user.getDateOfBirth());
		userForm.setStatus(user.getStatus());
		userForm.setRole(user.getRole());

		model.addAttribute("currentUser", userService.getCurrentUser());
		model.addAttribute("userId", userId);
		model.addAttribute("userForm", userForm);

		return "fragments/user/edit-user";
	}

	@PostMapping("/update/{userId}")
	public String updateUser(@PathVariable Long userId, @ModelAttribute UserForm userForm, Model model) {

		User currentUser = userService.getCurrentUser();

		if (currentUser.getRole() == UserRole.ADMIN || currentUser.getUserId() == userId) {

			userService.updateUser(userId, userForm, currentUser);

			model.addAttribute("message", "User updated successfully");
			model.addAttribute("redirectUrl", "/user/details");
			return "fragments/message-modal";
		}

		model.addAttribute("message", "You are not allowed to update this user");
		return "fragments/message-modal";
	}

	@GetMapping("/add")
	public String getAddUserForm(Model model) {
		model.addAttribute("userForm", new UserForm());

		model.addAttribute("user", userService.getCurrentUser());

		// The flash message from the previous request will automatically be added to
		// the model if exists (POST /add request)

		return "user/add-user";
	}

	@PostMapping("/add")
	public String addUser(@ModelAttribute UserForm userForm, RedirectAttributes redirectAttributes) {
		User currentUser = userService.getCurrentUser();

		// Ensure only admins can add new users
		if (currentUser.getRole() != UserRole.ADMIN) {
			redirectAttributes.addFlashAttribute("message", "You are not allowed to add users");
			return "redirect:/user/add";
		}

		Map<Integer, String> result = userService.addUser(userForm);
		String message = result.values().stream().findFirst().orElse("Unknown error occurred");

		// Add the message to redirect attributes
		redirectAttributes.addFlashAttribute("message", message);

		return "redirect:/user/add";
	}

	@GetMapping("/delete/{userId}")
	public String getUserInfoBeforeDelete(@PathVariable Long userId, Model model) {
		User currentUser = userService.getCurrentUser();
		// Ensure only admins can delete users
		User user = userService.getUserById(userId);
		if (currentUser.getRole() == UserRole.ADMIN) {
			model.addAttribute("user", user);
			return "fragments/user/confirm-delete";
		}
		
		return "redirect:/user/details";
	}

	@PostMapping("/delete/{userId}")
	public String deleteUser(@PathVariable Long userId, Model model) {
		userService.deleteUser(userId);

		model.addAttribute("message", "User deleted successfully");
		model.addAttribute("redirectUrl", "/user/details");
		
		// If user is deleting themself, use hard redirect
		if (userService.getCurrentUser().getUserId() == userId) {
			model.addAttribute("hardRedirect", true);
		}

		return "fragments/message-modal";
	}

}
