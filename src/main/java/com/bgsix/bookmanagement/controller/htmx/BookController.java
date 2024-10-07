package com.bgsix.bookmanagement.controller.htmx;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.bgsix.bookmanagement.dto.BookDTO;
import com.bgsix.bookmanagement.dto.BookForm;
import com.bgsix.bookmanagement.dto.BorrowedBookDTO;
import com.bgsix.bookmanagement.dto.TopGenreDTO;
import com.bgsix.bookmanagement.model.Book;
import com.bgsix.bookmanagement.model.User;
import com.bgsix.bookmanagement.service.BookService;
import com.bgsix.bookmanagement.service.BorrowService;
import com.bgsix.bookmanagement.service.GenreService;
import com.bgsix.bookmanagement.service.RequestService;
import com.bgsix.bookmanagement.service.UserService;

@Controller
@RequestMapping("/book")
public class BookController {

    private GenreService genreService;
    @Autowired
    private BookService bookService;

    @Autowired
    private BorrowService borrowService;

    @Autowired
    private UserService userService;

    @Autowired
    private RequestService requestService;

    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(BookController.class);

    // public BookController(GenreService genreService, BookService bookService,
    // BorrowService borrowService,
    // UserService userService) {
    // this.genreService = genreService;
    // this.bookService = bookService;
    // this.borrowService = borrowService;
    // this.userService = userService;
    // }

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
        model.addAttribute("userRole", user.getRole());

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

        model.addAttribute("book", bookDTO);

        return "fragments/book-detail";
    }

    @PostMapping("/request-borrow/{bookId}")
    public String requestBorrowBook(@PathVariable Long bookId, Model model) {
        requestService.createRequest(bookId);

        model.addAttribute("message", "Successfully request the book! Now we are waiting for the admin to approve. :/");
        return "fragments/request :: requestMessage";
    }

    @PutMapping("/request-approve/{requestId}")
    public String approveRequest(@PathVariable Long requestId, Model model) {
        requestService.approveRequest(requestId);

        model.addAttribute("req", requestService.getRequestById(requestId));

        logger.info("Request: {}", requestService.getRequestById(requestId).getRequestStatus());

        return "fragments/request :: requestRow";
    }

    @PutMapping("/borrow/return/{borrowId}")
    public String returnBook(@PathVariable Long borrowId, Model model) {

        BorrowedBookDTO borrowedBookDTO = borrowService.returnBook(borrowId);

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

        Book bookToSave = new Book(addBookForm.getAuthor(), addBookForm.getTitle(), addBookForm.getIsbn(),
                addBookForm.getBookFormat(), addBookForm.getPages(), addBookForm.getPrice(), addBookForm.getCoverImg());

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
}