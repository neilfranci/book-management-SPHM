package com.bgsix.bookmanagement.controller.htmx;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import com.bgsix.bookmanagement.model.User;
import com.bgsix.bookmanagement.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping("/user")
public class UserController {
	@Autowired
	private UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

	@GetMapping("/details")
    public String getUserDetails(@RequestParam(defaultValue = "0", name = "frag") boolean useFragment, Model model) {
        User user = userService.getCurrentUser();
        
        // Adding user and borrowed books data to the model
        model.addAttribute("user", user);
        // model.addAttribute("borrowedBooks", user.getBorrowedBooks());

        if (useFragment) {
            return "fragments/user-details";
        }else{
            return "user/details";
        }
    }
	
}
