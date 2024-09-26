package com.bgsix.bookmanagement.service;

import java.util.*;
import java.time.LocalDate;
import java.sql.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bgsix.bookmanagement.dto.BorrowedBookDTO;
import com.bgsix.bookmanagement.model.Book;
import com.bgsix.bookmanagement.model.Borrow;
import com.bgsix.bookmanagement.repository.BookRepository;
import com.bgsix.bookmanagement.repository.BorrowRepository;

@Service
public class BorrowService {

    @Autowired
    private BorrowRepository borrowRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserService userService;

    public BorrowedBookDTO returnBook(Long borrowId) {

        Object result = borrowRepository.findBorrowedBookWithBorrowId(borrowId);

        Borrow borrow = borrowRepository.findByBorrowId(borrowId);


        Object[] row = (Object[]) result;
       
        Long userId = ((Integer) row[1]).longValue();
        
        LocalDate borrowDate = ((Date) row[3]).toLocalDate();
        LocalDate returnDate = (row[4] != null ? ((Date) row[4]).toLocalDate() : null);
        String title = (String) row[5];
        String author = (String) row[6];

        BorrowedBookDTO borrowedBookDTO = new BorrowedBookDTO(borrowId, userId, true, borrowDate, returnDate, title, author);

        borrow.setReturned(true);

        borrowRepository.save(
            borrow
        );

        return borrowedBookDTO;
      
    }

    public void borrowBook(Long bookId) {
        Optional<Book> book = bookRepository.findById(bookId);
        Long userId = userService.getCurrentUser().getUserId();

        // Check if both the book and user exist
        if (book.isPresent()) {

            Borrow borrow = new Borrow();
            borrow.setBookId(bookId);
            borrow.setUserId(userId);
            borrow.setBorrowDate(LocalDate.now());
            borrow.setReturnDate(LocalDate.now().plusDays(14)); // Testing purposes

            borrowRepository.save(borrow);

        } else {
            throw new RuntimeException("The book does not exist.");
        }
    }

    public List<BorrowedBookDTO> getBorrowedBooks() {
        Long userId = userService.getCurrentUser().getUserId();
        List<Object[]> results = borrowRepository.findBorrowedBooksWithDetails(userId);

        List<BorrowedBookDTO> borrowedBooks = new ArrayList<>();
        for (Object[] row : results) {

            Long borrowId = ((Integer) row[0]).longValue();
            Long userIdFromQuery = ((Integer) row[1]).longValue();
            boolean returned = (boolean) row[2];
            LocalDate borrowDate = ((Date) row[3]).toLocalDate();
            LocalDate returnDate = (row[4] != null ? ((Date) row[4]).toLocalDate() : null);
            String title = (String) row[5];
            String author = (String) row[6];

            BorrowedBookDTO dto = new BorrowedBookDTO(borrowId, userIdFromQuery, returned, borrowDate, returnDate,
                    title, author);

            borrowedBooks.add(dto);
        }
        return borrowedBooks;
    }
}
