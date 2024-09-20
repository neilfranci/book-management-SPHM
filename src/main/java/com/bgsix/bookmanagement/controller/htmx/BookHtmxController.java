package com.bgsix.bookmanagement.controller.htmx;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bgsix.bookmanagement.dto.BookDTO;
import com.bgsix.bookmanagement.dto.BookResponse;
import com.bgsix.bookmanagement.dto.genre.TopGenreDTO;
import com.bgsix.bookmanagement.service.BookService;
import com.bgsix.bookmanagement.service.GenreService;

@Controller
@RequestMapping("/book")
public class BookHtmxController {

    private GenreService genreService;
    private BookService bookService;

    public BookHtmxController(GenreService genreService, BookService bookService) {
        this.genreService = genreService;
        this.bookService = bookService;
    }

    @GetMapping("/search")
    public String memberSearchPage(Model model) {
        // Load Genre options
        List<TopGenreDTO> genres = genreService.getTopGenres();

        Pageable pageable = PageRequest.of(0, 20);

        Page<BookDTO> books = bookService.getTopRateBooks(pageable);

        model.addAttribute("books", books.getContent());
        model.addAttribute("totalPages", books.getTotalPages());
        model.addAttribute("totalElements", books.getTotalElements());
        model.addAttribute("genres", genres);

        return "search/index"; // Loads the index.html template
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
            return "search/fragments :: noBookFound";
        }
        // Return the fragment with book rows
        return "search/fragments :: bookRow";
    }

}