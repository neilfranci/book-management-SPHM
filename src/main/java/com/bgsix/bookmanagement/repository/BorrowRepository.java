package com.bgsix.bookmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bgsix.bookmanagement.enums.BorrowStatus;
import com.bgsix.bookmanagement.model.Borrow;
import java.util.List;

@Repository
public interface BorrowRepository extends JpaRepository<Borrow, Long> {

	Borrow findByBorrowId(Long borrowId);

	List<Borrow> findByUserId(Long userId);

	Borrow findByBookIdAndUserId(Long bookId, Long userId);

	Borrow findByBookIdAndUserIdAndStatus(Long bookId, Long userId, BorrowStatus status);
}
