package com.bgsix.bookmanagement.model;

import java.time.LocalDate;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "borrow_book")
public class Borrow {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long borrowId;
	Long bookId;
	Long userId;
	boolean returned = false;
	LocalDate borrowDate;
	LocalDate returnDate;

}
