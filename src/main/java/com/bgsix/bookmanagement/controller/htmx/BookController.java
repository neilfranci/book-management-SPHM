package com.bgsix.bookmanagement.controller.htmx;

import java.util.List;

import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.bgsix.bookmanagement.dto.BookDTO;
import com.bgsix.bookmanagement.dto.TopGenreDTO;
import com.bgsix.bookmanagement.model.Borrow;
import com.bgsix.bookmanagement.service.BookService;
import com.bgsix.bookmanagement.service.BorrowService;
import com.bgsix.bookmanagement.service.GenreService;

@Controller
@RequestMapping("/book")
public class BookController {

    private GenreService genreService;
    private BookService bookService;
    private BorrowService borrowService;

    public BookController(GenreService genreService, BookService bookService, BorrowService borrowService) {
        this.genreService = genreService;
        this.bookService = bookService;
        this.borrowService = borrowService;
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

        return "search/index";
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
    @ResponseBody
    public ResponseEntity<Void> returnBook(@PathVariable Long borrowId) {
        Borrow borrow = borrowService.returnBook(borrowId);

        if (borrow.isReturned()) {
            // If successful, return HTTP 200 OK
            return ResponseEntity.ok().build();
        } else {
            // If there was an issue, return HTTP 404 Not Found or other status
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

}