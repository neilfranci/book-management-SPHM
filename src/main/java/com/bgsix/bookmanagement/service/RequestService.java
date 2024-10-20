package com.bgsix.bookmanagement.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bgsix.bookmanagement.enums.BorrowStatus;
import com.bgsix.bookmanagement.enums.RequestStatus;
import com.bgsix.bookmanagement.model.Book;
import com.bgsix.bookmanagement.model.BookRequest;
import com.bgsix.bookmanagement.model.Borrow;
import com.bgsix.bookmanagement.repository.BookRepository;
import com.bgsix.bookmanagement.repository.BorrowRepository;
import com.bgsix.bookmanagement.repository.RequestRepository;

@Service
public class RequestService {

	@Autowired
	private BorrowRepository borrowRepository;

	@Autowired
	private RequestRepository requestRepository;

	@Autowired
	private BookRepository bookRepository;

	// @Autowired
	// private UserService userService;

	// REQUEST METHODS
	public Map<Integer, String> createRequest(Long bookId, Long userId) {

		String message = "";

		Optional<Book> book = bookRepository.findById(bookId);

		if (!book.isPresent()) {
			message = "The book does not exist.";
			return Map.of(0, message);
		}

		if (book.get().getQuantity() == 0) {
			message = "The book is out of stock.";
			return Map.of(0, message);
		}

		if (requestRepository.findByBookIdAndUserIdAndStatus(bookId, userId, RequestStatus.PENDING) != null) {
			message = "You have already requested this book.";
			return Map.of(0, message);
		}

		if (borrowRepository.findByBookIdAndUserIdAndStatus(bookId, userId, BorrowStatus.BORROWED) != null) {
			message = "You have already borrowed this book. Please return it first.";
			return Map.of(0, message);
		}

		BookRequest request = new BookRequest();
		request.setBookId(bookId);
		request.setUserId(userId);
		request.setRequestDate(LocalDate.now());
		request.setStatus(RequestStatus.PENDING);

		requestRepository.save(request);

		book.get().setQuantity(book.get().getQuantity() - 1);

		bookRepository.save(book.get());

		message = "Successfully request the book!";

		return Map.of(1, message);
	}

	// Testing
	public void approveRequest(Long requestId, Long userId) {
		Optional<BookRequest> request = requestRepository.findById(requestId);

		if (request.isPresent()) {
			BookRequest req = request.get();
			req.setLibrarianId(userId);
			req.setStatus(RequestStatus.APPROVED);
			req.setApprovalDate(LocalDate.now());

			Borrow borrow = new Borrow();
			borrow.setBookId(req.getBookId());
			borrow.setUserId(req.getUserId());
			borrow.setBorrowDate(LocalDate.now());
			borrow.setDueDate(LocalDate.now().plusDays(14)); // Testing purposes

			borrowRepository.save(borrow);
			requestRepository.save(req);
		} else {
			throw new RuntimeException("The request does not exist to approve.");
		}
	}

	public BookRequest getRequestById(Long requestId) {
		Optional<BookRequest> request = requestRepository.findById(requestId);

		if (request.isPresent()) {
			return request.get();
		} else {
			throw new RuntimeException("The request does not exist.");
		}
	}

	public List<BookRequest> getRequests() {
		List<BookRequest> requests = requestRepository.findAll();

		// sort descending by request id
		requests.sort((r1, r2) -> r2.getRequestId().compareTo(r1.getRequestId()));

		return requests;
	}

}
