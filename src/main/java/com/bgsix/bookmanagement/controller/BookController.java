package com.bgsix.bookmanagement.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.bgsix.bookmanagement.dto.*;
import com.bgsix.bookmanagement.enums.*;
import com.bgsix.bookmanagement.model.*;
import com.bgsix.bookmanagement.service.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping("/book")
public class BookController {

	// private GenreService genreService;
	@Autowired
	private BookService bookService;

	@Autowired
	private BorrowService borrowService;

	@Autowired
	private UserService userService;

	@Autowired
	private RequestService requestService;

	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(BookController.class);


	@GetMapping("home")
	public String getHomePage(
			@RequestParam(required = false, defaultValue = "top-rate") String searchBy,
			@RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "20") int size,
			Model model) {

		Pageable pageable = PageRequest.of(page - 1, size);

		Page<BookDTO> bookDTOs;

		if (searchBy.equals("top-rate")) {
			bookDTOs = bookService.getTopRateBooks(pageable);
		} else {
			bookDTOs = bookService.getAllBooks(pageable);
		}

		logger.info("Search By: " + searchBy);
		logger.info("Page: " + page + ", Size: " + size);
		logger.info("Total Pages: " + bookDTOs.getTotalPages());
		logger.info("--------------------");

		model.addAttribute("searchBy", searchBy);
		model.addAttribute("books", bookDTOs.getContent());
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", bookDTOs.getTotalPages());

		// model.addAttribute("genres", genres);

		// Get User Role
		User user = userService.getCurrentUser();
		model.addAttribute("user", user);

		return "book/search";
	}
	

	@GetMapping("/search")
	public String searcPage(
			@RequestParam(required = false, name = "query") String searchInput, 
			@RequestParam(required = false, name = "by") String searchBy,
			@RequestParam(required = false) List<String> genre,
			@RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "20") int size,
			Model model) {
		// Load Genre options
		// List<TopGenreDTO> genres = genreService.getTopGenres();

		if (searchInput == null || searchInput.isEmpty()) {
			return "redirect:/book/home";
		}

		Pageable pageable = PageRequest.of(page - 1, size);

		Page<BookDTO> bookDTOs = null;

		bookDTOs = bookService.getBooksByTitle(searchInput, pageable);

		int totalPages = bookDTOs.getTotalPages();

		logger.info("Search Input: " + searchInput);
		logger.info("Search By: " + searchBy);
		logger.info("Genres: " + genre);
		logger.info("Page: " + page + ", Size: " + size);
		logger.info("Total Pages: " + totalPages);
		logger.info("--------------------");

		model.addAttribute("searchInput", searchInput);
		model.addAttribute("searchBy", searchBy);
		model.addAttribute("books", bookDTOs.getContent());
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", totalPages);
		// model.addAttribute("genres", genres);

		// Get User Role
		User user = userService.getCurrentUser();
		model.addAttribute("user", user);
		
		if (totalPages < page  && totalPages > 0) {
			return "redirect:/book/search?searchInput=" + searchInput + "&page=" + totalPages;
		}
		
		return "book/search";
	}

	
	@GetMapping("/details/{id}")
	public String getBookDetails(@PathVariable Long id, Model model) {
		BookDTO bookDTO = bookService.getBookById(id);
		User user = userService.getCurrentUser();

		model.addAttribute("userRole", user.getRole());
		model.addAttribute("userStatus", user.getStatus());
		model.addAttribute("book", bookDTO);

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
	public String getMethodName(Model model) {
		model.addAttribute("addBookForm", new BookForm());
		model.addAttribute("user", userService.getCurrentUser());
		return "book/add-book";
	}

	@PostMapping("/add")
	public String addBook(@ModelAttribute BookForm addBookForm, Model model) {

		logger.info(addBookForm.toString());

		Book bookToSave = new Book(addBookForm);

		bookService.addBook(bookToSave);

		return "redirect:/book/add?success";
	}

	@PostMapping("/update/{bookId}")
	public String updateBook(@PathVariable Long bookId, @ModelAttribute BookForm bookForm, Model model) {
		bookService.updateBook(bookId, bookForm);

		// Add a success message to the model
		model.addAttribute("book", bookService.getBookById(bookId)); // Reload the updated book
		model.addAttribute("message", "Book updated successfully!");

		return "fragments/book/edit-book";
	}

	@GetMapping("/edit/{bookId}")
	public String editBook(@PathVariable Long bookId, Model model) {
		Book book = bookService.getBookById(bookId);
		model.addAttribute("book", book);
		return "fragments/book/edit-book";
	}

	@GetMapping("/delete/{bookId}")
	public String getDeleteModal(@PathVariable Long bookId, Model model) {
		Book book = bookService.getBookById(bookId);
		model.addAttribute("book", book);
		return "fragments/book/book-detail :: confirmDeleteModal";
	}

	@PostMapping("/delete/{bookId}")
	public String deleteBook(@PathVariable Long bookId, Model model) {
		bookService.deleteBook(bookId);
		return "fragments/book/book-detail :: deleteSuccessModal";
	}
}