package com.example.bookService.exceptions;

import com.example.bookService.book.payload.Message;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
public class CustomException extends RuntimeException {
    HttpStatus status;
    Message errorMessage;
}
