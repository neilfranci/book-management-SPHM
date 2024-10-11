package com.bgsix.bookmanagement.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import com.bgsix.bookmanagement.dto.UserForm;
import com.bgsix.bookmanagement.enums.UserRole;
import com.bgsix.bookmanagement.model.User;
import com.bgsix.bookmanagement.service.BorrowService;
import com.bgsix.bookmanagement.service.RequestService;
import com.bgsix.bookmanagement.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.*;

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
	public String getUserDetails(@RequestParam(defaultValue = "0", name = "frag") boolean useFragment, Model model) {
		User user = userService.getCurrentUser();

		model.addAttribute("user", user);
		model.addAttribute("userRole", user.getRole());
		model.addAttribute("requests", requestService.getRequests());

		if (user.getRole() == UserRole.Admin || user.getRole() == UserRole.Librarian) {
			model.addAttribute("members", userService.getAllUsers());
			model.addAttribute("borrowedBooks", borrowService.getBorrowedBooks());
		} else {
			Long userId = userService.getCurrentUser().getUserId();

			model.addAttribute("borrowedBooks", borrowService.getBorrowedBooksForUser(userId));
		}

		if (useFragment) {
			if (user.getRole() == UserRole.Admin || user.getRole() == UserRole.Librarian) {
				return "fragments/admin-detail";
			} else {
				return "fragments/member-detail";
			}

		} else {
			return "user/details";
		}
	}

	@GetMapping("/{userId}")
	public String getUserDetails(@PathVariable Long userId, Model model) {
		User user = userService.getUserById(userId);

		model.addAttribute("user", user);

		return "fragments/edit-user";
	}

	@PostMapping("/update/{userId}")
	public String updateUser(@PathVariable Long userId, @ModelAttribute UserForm userForm, Model model,
			HttpServletRequest request, HttpServletResponse response) {
		UserRole oldUserRole = userService.getCurrentUser().getRole();
		Long oldUserId = userService.getCurrentUser().getUserId();

		userService.updateUser(userId, userForm);

		if (oldUserId.equals(userId) && oldUserRole != userForm.getRole()) {
			logger.info("Changing user role from {} to {}", oldUserRole, userForm.getRole());

			SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
			logoutHandler.logout(request, response, SecurityContextHolder.getContext().getAuthentication());

			return "redirect:/login?logout";
		}

		if (oldUserId.equals(userId)) {
			return "redirect:/user/details";
		}

		return "redirect:/user/details";
	}

}
