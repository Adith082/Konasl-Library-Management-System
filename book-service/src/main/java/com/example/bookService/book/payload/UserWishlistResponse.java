package com.example.bookService.book.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UserWishlistResponse {
    int bookId;
    String title;
    String author;
}
