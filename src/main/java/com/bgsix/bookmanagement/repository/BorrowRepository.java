package com.bgsix.bookmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.bgsix.bookmanagement.model.Borrow;
import java.util.List;

@Repository
public interface BorrowRepository extends JpaRepository<Borrow, Long> {
	
	Borrow findByBorrowId(Long borrowId);
	Borrow findByBookIdAndUserId(Long bookId, Long userId);

	// Native query to fetch borrowed books with their details
    @Query(value = "SELECT b.borrow_id, b.user_id, b.returned, b.borrow_date, b.return_date, " +
                   "bk.title, bk.author " +
                   "FROM borrow_book b " +
                   "JOIN book bk ON b.book_id = bk.book_id " +
                   "WHERE b.user_id = :userId " +
                   "ORDER BY b.borrow_id DESC"
                   , nativeQuery = true)
    List<Object[]> findBorrowedBooksWithDetails(Long userId);


    @Query(value = "SELECT b.borrow_id, b.user_id, b.returned, b.borrow_date, b.return_date, " +
                   "bk.title, bk.author " +
                   "FROM borrow_book b " +
                   "JOIN book bk ON b.book_id = bk.book_id " +
                   "WHERE b.borrow_id = :borrowId"
                   , nativeQuery = true)
    Object findBorrowedBookWithBorrowId(Long borrowId);
}
