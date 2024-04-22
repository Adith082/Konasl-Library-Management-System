package com.example.bookService.book.payload;

import lombok.Data;

@Data
public class WishlistRequest {
    private int userId;
    private int bookId;
}
