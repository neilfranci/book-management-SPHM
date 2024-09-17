package com.bgsix.bookmanagement.controller.htmx;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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

    @GetMapping("")
    public String memberSearchPage(Model model) {
        // Load Genre options
        List<TopGenreDTO> genres = genreService.getTopGenres();

        BookResponse books = bookService.getTopRateBooks(0, 20);

        model.addAttribute("books", books.getBooks());
        model.addAttribute("totalPages", books.getTotalPages());
        model.addAttribute("totalElements", books.getTotalElements());
        model.addAttribute("genres", genres);

        return "searchPage/index"; // Loads the index.html template
    }
}