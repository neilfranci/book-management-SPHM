package com.bgsix.bookmanagement.controller;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.bgsix.bookmanagement.dto.*;
import com.bgsix.bookmanagement.enums.*;
import com.bgsix.bookmanagement.interfaces.IBookService;
import com.bgsix.bookmanagement.model.*;
import com.bgsix.bookmanagement.service.*;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/book")
public class BookController {
	@Autowired
	private IBookService bookService;

	@Autowired
	private GenreService genreService;

	@Autowired
	private BorrowService borrowService;

	@Autowired
	private UserService userService;

	@Autowired
	private RequestService requestService;

	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(BookController.class);

	@GetMapping("/search")
	public String searcPage(@RequestParam(required = false, name = "q", defaultValue = "") String searchInput,
			@RequestParam(required = false, name = "b", defaultValue = "title") String searchBy,
			@RequestParam(required = false, name = "s", defaultValue = "rating_desc") String sortBy,
			@RequestParam(required = false, name = "g", defaultValue = "") String selectedGenresStr,
			@RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "20") int size,
			HttpServletRequest request, Model model) {
		// Load Genre options
		logger.info("--------------------");

		List<String> topGenreDTOs = genreService.getTopGenres();
		model.addAttribute("genres", topGenreDTOs);
		
		// Convert comma-separated genres string to a List<String>
		List<String> selectedGenres = !selectedGenresStr.equals("") ? Arrays.asList(selectedGenresStr.split(",")) : Collections.emptyList();

		
		Page<Book> booksPage = bookService.searchBooks(searchInput, searchBy, selectedGenres, sortBy, page - 1, size);
		
		int totalPages = booksPage.getTotalPages();
		
		logger.info("Search Input: " + searchInput);
		logger.info("Search By: " + searchBy);
		logger.info("Sort By: " + sortBy);
		logger.info("Selected Genres: " + selectedGenresStr);
		logger.info("Page: " + page + ", Size: " + size);
		logger.info("Total Pages: " + totalPages);
		logger.info("Total Books: " + booksPage.getTotalElements());

		model.addAttribute("searchInput", searchInput);
		model.addAttribute("searchBy", searchBy);
		model.addAttribute("sortBy", sortBy);
		model.addAttribute("books", booksPage.getContent());
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", totalPages);
		model.addAttribute("selectedGenres", selectedGenresStr);

		// Get User Role
		User user = userService.getCurrentUser();
		model.addAttribute("user", user);
			
		// Redirect to the last page if the page number is greater than the total number of pages
		if (totalPages < page && totalPages > 0) {
			return "redirect:/book/search?q=" + URLEncoder.encode(searchInput, StandardCharsets.UTF_8) 
					+ "&g=" + String.join(",", selectedGenres)
					+ "&page=" + totalPages;
		}

		String referer = request.getHeader("Referer");
		logger.info("Request URI: " + referer);
	
		// Check if the request is made through HTMX
		boolean isHtmxRequest = "true".equals(request.getHeader("HX-Request"));
	
		if (isHtmxRequest) {
			return "book/search :: searchResults";
		} else if (referer != null && referer.contains("/book/search")) {
			return "book/search";
		}
	
		return "book/search"; // Default case, return full page
	}

	@GetMapping("/details/{id}")
	public String getBookDetails(@PathVariable Long id, Model model) {
		Book book = bookService.findById(id);
		User user = userService.getCurrentUser();

		model.addAttribute("userRole", user.getRole());
		model.addAttribute("userStatus", user.getStatus());
		model.addAttribute("book", book);

		return "fragments/book/book-detail :: detailModal";
	}

	@PostMapping("/request-borrow/{bookId}")
	public String requestBorrowBook(@PathVariable Long bookId, Model model) {

		User user = userService.getCurrentUser();

		if (user.getStatus() == UserStatus.INACTIVE) {
			model.addAttribute("message", "Please activate your account first.");
			return "fragments/request/message";
		} else if (user.getStatus() == UserStatus.SUSPENDED) {
			model.addAttribute("message", "Your account is suspended. Please contact the librarian.");
			return "fragments/request/message";
		}

		Map<Integer, String> result = requestService.createRequest(bookId, user.getUserId());

		if (result.get(0) != null) {
			model.addAttribute("message", result.get(0));
			return "fragments/request/message";
		} else if (result.get(1) != null) {
			model.addAttribute("message", result.get(1));
			return "fragments/request/message";
		}

		return "fragments/request/message";
	}

	@GetMapping("/request-approve/{requestId}")
	public String getRequestApprove(@PathVariable Long requestId, Model model) {

		BookRequest request = requestService.getRequestById(requestId);
		model.addAttribute("req", request);

		return "fragments/request/approve-modal";
	}

	@PutMapping("/request-approve/{requestId}")
	public String approveRequest(@PathVariable Long requestId, Model model) {
		// Check user who can approve request
		User user = userService.getCurrentUser();

		if (user.getRole() != UserRole.LIBRARIAN && user.getRole() != UserRole.ADMIN) {
			model.addAttribute("message", "You are not allowed to approve this request.");
			return "fragments/message-modal";
		}

		requestService.approveRequest(requestId, user.getUserId());

		model.addAttribute("message", "Request approved successfully.");
		model.addAttribute("redirectUrl", "/user/details");

		return "fragments/message-modal";
	}

	@PutMapping("/borrow/return/{borrowId}")
	public String returnBook(@PathVariable Long borrowId, Model model) {

		BorrowedBookDTO borrowedBookDTO = borrowService.returnBook(borrowId);

		if (borrowedBookDTO.getFine() > 0) {
			model.addAttribute("borrow", borrowedBookDTO);
			return "fragments/borrow :: returnModal";
		}

		model.addAttribute("borrow", borrowedBookDTO);
		return "fragments/borrow :: borrowBookRow";
	}

	@PostMapping("/borrow/pay/{borrowId}")
	public String payFine(@PathVariable Long borrowId, Model model) {
		borrowService.payFine(borrowId);

		model.addAttribute("borrowId", borrowId);

		return "fragments/borrow :: paymentCompletedModal";
	}

	@GetMapping("/borrow/{borrowId}")
	public String getBorrowedBook(@PathVariable Long borrowId, Model model) {
		BorrowedBookDTO borrowedBookDTO = borrowService.getBorrowedBook(borrowId);

		model.addAttribute("borrow", borrowedBookDTO);
		return "fragments/borrow :: borrowBookRow";
	}

	// Admin routes
	@GetMapping("/add")
	public String getBookAddForm(Model model) {
		model.addAttribute("addBookForm", new BookForm());
		model.addAttribute("user", userService.getCurrentUser());
		
		List<String> topGenreDTOs = genreService.getTopGenres();
		model.addAttribute("genres", topGenreDTOs);

		return "book/add-book";
	}

	@PostMapping("/add")
	public String addBook(@ModelAttribute BookForm addBookForm, Model model) {
		// Check if addBookForm is valid
		if (!addBookForm.validate()) {
			logger.error("Invalid book form: {}", addBookForm.toString());
			return getBookAddForm(model);
		}

		logger.info("Adding book: {}", addBookForm.toString());

		bookService.addBook(addBookForm);

		return "redirect:/book/add?success";
	}

	@PostMapping("/update/{bookId}")
	public String updateBook(@PathVariable Long bookId, @ModelAttribute BookForm bookForm, Model model) {
		bookService.updateBook(bookId, bookForm);

		// Add a success message to the model
		model.addAttribute("book", bookService.findById(bookId)); // Reload the updated book
		model.addAttribute("message", "Book updated successfully!");

		return "fragments/book/edit-book";
	}

	@GetMapping("/edit/{bookId}")
	public String editBook(@PathVariable Long bookId, Model model) {
		Book book = bookService.findById(bookId);
		model.addAttribute("book", book);
		return "fragments/book/edit-book";
	}

	@GetMapping("/delete/{bookId}")
	public String getDeleteModal(@PathVariable Long bookId, Model model) {
		Book book = bookService.findById(bookId);
		model.addAttribute("book", book);
		return "fragments/book/book-detail :: confirmDeleteModal";
	}

	@PostMapping("/delete/{bookId}")
	public String deleteBook(@PathVariable Long bookId, Model model) {
		bookService.deleteBook(bookId);
		return "fragments/book/book-detail :: deleteSuccessModal";
	}
}