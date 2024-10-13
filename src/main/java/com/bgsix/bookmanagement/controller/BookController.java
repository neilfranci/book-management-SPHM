package com.bgsix.bookmanagement.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.bgsix.bookmanagement.dto.BookDTO;
import com.bgsix.bookmanagement.dto.BookForm;
import com.bgsix.bookmanagement.dto.BorrowedBookDTO;
import com.bgsix.bookmanagement.enums.UserRole;
import com.bgsix.bookmanagement.enums.UserStatus;
// import com.bgsix.bookmanagement.dto.TopGenreDTO;
import com.bgsix.bookmanagement.model.Book;
import com.bgsix.bookmanagement.model.BookRequest;
import com.bgsix.bookmanagement.model.User;
import com.bgsix.bookmanagement.service.BookService;
import com.bgsix.bookmanagement.service.BorrowService;
// import com.bgsix.bookmanagement.service.GenreService;
import com.bgsix.bookmanagement.service.RequestService;
import com.bgsix.bookmanagement.service.UserService;

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

	@GetMapping("/search")
	public String searcPage(Model model) {
		// Load Genre options
		// List<TopGenreDTO> genres = genreService.getTopGenres();

		Pageable pageable = PageRequest.of(0, 20);

		Page<BookDTO> books = bookService.getTopRateBooks(pageable);

		model.addAttribute("books", books.getContent());
		model.addAttribute("totalPages", books.getTotalPages());
		model.addAttribute("totalElements", books.getTotalElements());
		// model.addAttribute("genres", genres);

		// Get User Role
		User user = userService.getCurrentUser();
		model.addAttribute("user", user);

		return "book/search";
	}

	@GetMapping("/search-request")
	public String searchRequest(@RequestParam String searchInput, @RequestParam(required = false) String searchBy,
			@RequestParam(required = false) List<String> genre, Model model) {

		System.out.println("Search Input: " + searchInput);
		System.out.println("Search By: " + searchBy);
		System.out.println("Genres: " + genre);

		Pageable pageable = PageRequest.of(0, 20);

		Page<BookDTO> bookDTOs = bookService.getBooksByTitle(searchInput, pageable);

		model.addAttribute("books", bookDTOs.getContent());

		if (bookDTOs.getContent().isEmpty()) {
			return "fragments/search :: noBookFound";
		}
		return "fragments/search :: bookRow";
	}

	@GetMapping("/details/{id}")
	public String getBookDetails(@PathVariable Long id, Model model) {
		BookDTO bookDTO = bookService.getBookById(id);
		User user = userService.getCurrentUser();
		
		model.addAttribute("userRole", user.getRole());
		model.addAttribute("userStatus", user.getStatus());
		model.addAttribute("book", bookDTO);

		return "fragments/book-detail :: detailModal";
	}

	@PostMapping("/request-borrow/{bookId}")
	public String requestBorrowBook(@PathVariable Long bookId, Model model) {

		User user = userService.getCurrentUser();

		if (user.getStatus() == UserStatus.INACTIVE) {
			model.addAttribute("message", "Please activate your account first.");
			return "fragments/request :: requestMessage";
		} else if (user.getStatus() == UserStatus.SUSPENDED) {
			model.addAttribute("message", "Your account is suspended. Please contact the librarian.");
			return "fragments/request :: requestMessage";
		}

		Map<Integer, String> result = requestService.createRequest(bookId, user.getUserId());

		if (result.get(0) != null) {
			model.addAttribute("message", result.get(0));
			return "fragments/request :: requestMessage";
		} else if (result.get(1) != null) {
			model.addAttribute("message", result.get(1));
			return "fragments/request :: requestMessage";
		}

		return "fragments/request :: requestMessage";
	}

	@GetMapping("/request-approve/{requestId}")
	public String getRequestApprove(@PathVariable Long requestId, Model model) {

		BookRequest request = requestService.getRequestById(requestId);
		model.addAttribute("req", request);
		
		return "fragments/request :: requestApproveModal";
	}

	@PutMapping("/request-approve/{requestId}")
	public String approveRequest(@PathVariable Long requestId, Model model) {
		//Check user who can approve request
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
		return "book/add-book";
	}

	@PostMapping("/add")
	public String addBook(@ModelAttribute BookForm addBookForm, Model model) {

		System.out.println(addBookForm.toString());

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

		return "fragments/edit-book";
	}

	@GetMapping("/edit/{bookId}")
	public String editBook(@PathVariable Long bookId, Model model) {
		Book book = bookService.getBookById(bookId);
		model.addAttribute("book", book);
		return "fragments/edit-book";
	}

	@GetMapping("/delete/{bookId}")
	public String getDeleteModal(@PathVariable Long bookId, Model model) {
		Book book = bookService.getBookById(bookId);
		model.addAttribute("book", book);
		return "fragments/book-detail :: confirmDeleteModal";
	}


	@PostMapping("/delete/{bookId}")
	public String deleteBook(@PathVariable Long bookId, Model model) {
		bookService.deleteBook(bookId);
		return "fragments/book-detail :: deleteSuccessModal";
	}
}