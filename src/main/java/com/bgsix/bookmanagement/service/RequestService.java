package com.bgsix.bookmanagement.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bgsix.bookmanagement.model.Book;
import com.bgsix.bookmanagement.model.BookRequest;
import com.bgsix.bookmanagement.model.Borrow;
import com.bgsix.bookmanagement.model.User;
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

	@Autowired
	private UserService userService;

	// REQUEST METHODS
	public void createRequest(Long bookId) {
		Optional<Book> book = bookRepository.findById(bookId);
		Long userId = userService.getCurrentUser().getUserId();

		// Check if both the book
		if (book.isPresent()) {

			BookRequest request = new BookRequest();
			request.setBookId(bookId);
			request.setUserId(userId);
			request.setRequestDate(LocalDate.now());

			requestRepository.save(request);

		} else {
			throw new RuntimeException("The book does not exist to request.");
		}
	}

	// Testing
	public void approveRequest(Long requestId) {
		Optional<BookRequest> request = requestRepository.findById(requestId);

		User user = userService.getCurrentUser();

		if (request.isPresent()) {
			BookRequest req = request.get();
			req.setLibrarianId(user.getUserId());
			req.setRequestStatus("Approved");
			req.setApprovalDate(LocalDate.now());

			Borrow borrow = new Borrow();
			borrow.setBookId(req.getBookId());
			borrow.setUserId(req.getUserId());
			borrow.setBorrowDate(LocalDate.now());
			borrow.setReturnDate(LocalDate.now().plusDays(14)); // Testing purposes

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
		return requestRepository.findAll();
	}

}
