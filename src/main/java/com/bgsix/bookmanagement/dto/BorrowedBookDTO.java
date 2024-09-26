package com.bgsix.bookmanagement.dto;

import java.time.LocalDate;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
public class BorrowedBookDTO {
    Long borrowId;
    Long userId;
    boolean returned;
    LocalDate borrowDate;
    LocalDate returnDate;
    String title;
    String author;
}