package com.example.bookService.wishlist;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WishlistRepository extends JpaRepository<WishList, Integer> {
    WishList findByBookId(int bookId);

    WishList findByBookIdAndUserId(int bookId, int userId);

    List<WishList> findAllByUserId(int userId);
}

