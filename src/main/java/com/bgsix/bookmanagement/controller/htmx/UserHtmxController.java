package com.bgsix.bookmanagement.controller.htmx;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("")
public class UserHtmxController {

	@GetMapping("/login")
	public String login(Model model) {

		return "user/login";
	}

	@GetMapping("/signup")
	public String signup() {
		return "user/signup";
	}

	@PostMapping("/submit-login")
	public String submitLogin(@RequestParam String username, @RequestParam String password) {
		boolean isAuthenticated = true;// authenticationService.authenticate(username, password);

		if (isAuthenticated) {
			return "user/fragments :: login-success";
		} else {
			return "user/fragments :: login-failed";
		}

	}

}