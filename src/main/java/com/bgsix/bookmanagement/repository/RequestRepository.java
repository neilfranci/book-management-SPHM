package com.bgsix.bookmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bgsix.bookmanagement.enums.RequestStatus;
import com.bgsix.bookmanagement.model.BookRequest;

@Repository
public interface RequestRepository extends JpaRepository<BookRequest, Long> {

	BookRequest findByBookIdAndUserId(Long bookId, Long userId);

	BookRequest findByBookId(Long bookId);

	BookRequest findByBookIdAndUserIdAndStatus(Long bookId, Long userId, RequestStatus status);
}
