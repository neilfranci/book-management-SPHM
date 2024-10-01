package com.bgsix.bookmanagement.controller.htmx;

import java.util.List;

import org.springframework.data.domain.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.bgsix.bookmanagement.dto.AddBookForm;
import com.bgsix.bookmanagement.dto.BookDTO;
import com.bgsix.bookmanagement.dto.BorrowedBookDTO;
import com.bgsix.bookmanagement.dto.TopGenreDTO;
import com.bgsix.bookmanagement.model.Book;
import com.bgsix.bookmanagement.model.User;
import com.bgsix.bookmanagement.service.BookService;
import com.bgsix.bookmanagement.service.BorrowService;
import com.bgsix.bookmanagement.service.GenreService;
import com.bgsix.bookmanagement.service.UserService;

@Controller
@RequestMapping("/book")
public class BookController {

    private GenreService genreService;
    private BookService bookService;
    private BorrowService borrowService;
    private UserService userService;

    public BookController(GenreService genreService, BookService bookService, BorrowService borrowService,
            UserService userService) {
        this.genreService = genreService;
        this.bookService = bookService;
        this.borrowService = borrowService;
        this.userService = userService;
    }

    @GetMapping("/search")
    public String searcPage(Model model) {
        // Load Genre options
        List<TopGenreDTO> genres = genreService.getTopGenres();

        Pageable pageable = PageRequest.of(0, 20);

        Page<BookDTO> books = bookService.getTopRateBooks(pageable);

        model.addAttribute("books", books.getContent());
        model.addAttribute("totalPages", books.getTotalPages());
        model.addAttribute("totalElements", books.getTotalElements());
        model.addAttribute("genres", genres);

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

        return "fragments/search :: bookDetailFragment";
    }

    @PostMapping("/borrow/{bookId}")
    public String borrowBook(@PathVariable Long bookId, Model model) {
        borrowService.borrowBook(bookId);

        model.addAttribute("message", "Successfully borrowed the book!");
        return "fragments/borrow :: successBorrowFragment";
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
        model.addAttribute("addBookForm", new AddBookForm());
        return "book/add-book";
    }

    @PostMapping("/add")
    public String addBook(@ModelAttribute AddBookForm addBookForm, Model model) {

        System.out.println(addBookForm.toString());

        Book bookToSave = new Book(addBookForm.getAuthor(), addBookForm.getTitle(), addBookForm.getIsbn(),
                addBookForm.getBookFormat(), addBookForm.getPages(), addBookForm.getPrice(), addBookForm.getCoverImg());

        bookService.addBook(bookToSave);

        return "redirect:/book/add?success";
    }
}