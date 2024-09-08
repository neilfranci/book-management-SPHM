package com.bgsix.server;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {
	@GetMapping("/book")
	public String getBook() {
		return "Random Book :D";
	}
}
