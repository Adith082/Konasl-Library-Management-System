package com.example.bookService.book;

import com.example.bookService.book.payload.Message;
import com.example.bookService.book.payload.WishlistRequest;
import com.example.bookService.exceptions.CustomException;
import com.example.bookService.type.Type;
import com.example.bookService.wishlist.WishList;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class BookController {

    @Autowired
    BookService bookService;

    @RequestMapping("/admin/types/{id}/books")
    public List<Book> getAllBooks(@PathVariable String id){
        return bookService.getAllBooks(id);
    }


    @RequestMapping("/types/{id}/books")
    public List<Book> getAllApprovedBooks(@PathVariable String id){
        return bookService.getAllApprovedBooks(id);
    }


    @RequestMapping("/types/{typeId}/books/{id}")
    public Book getBookById(@PathVariable String typeId, @PathVariable int id) {
        return bookService.getBookById(id);
    }

    @RequestMapping(method=RequestMethod.POST, value="/types/{typeId}/books")
    public void addBook(@PathVariable String typeId, @RequestBody Book book){
        book.setType(new Type(typeId, "", ""));
        bookService.addBook(book);
    }

    @RequestMapping(method=RequestMethod.PUT, value="/admin/types/{typeId}/books/{id}")
    public void updateBook(@PathVariable String typeId, @PathVariable int id, @RequestBody Book book){
        book.setType(new Type(typeId, "", ""));
        bookService.updateBook(book, id);
    }

    @RequestMapping(method=RequestMethod.DELETE, value="/admin/types/{typeId}/books/{id}")
    public void deleteBook(@PathVariable String typeId, @PathVariable int id){
        bookService.deleteBook(id);
    }






    @RequestMapping(method=RequestMethod.GET, value="/admin/unapprovedBooks")
    public List<Book> getAllUnapprovedBooks(){

        return bookService.getAllUnapprovedBooks();
    }


    @RequestMapping(method=RequestMethod.PUT, value="/admin/approveBook/{id}")
    public Message approveBook(@PathVariable int id){
        Book book = bookService.getBookById(id);
        //  System.out.println(user);
        book.setBookApproved(true);
        bookService.updateBook(book, id);
       // System.out.println(book);
        return new Message("bookid " + id + " is approved");
    }


   // @PostMapping("/user-wishlist/add")
    @RequestMapping(method=RequestMethod.POST, value="/user-wishlist/add")
    public ResponseEntity<?> addUserWishlist(@RequestBody WishlistRequest wishlistRequest) {
        System.out.println(wishlistRequest);
        try {
            return ResponseEntity.ok(bookService.addBookToWishlist(wishlistRequest));
        } catch (CustomException e) {
            return new ResponseEntity<>(e.getErrorMessage(), e.getStatus());
        }
    }


    @PostMapping("/user-wishlist/remove")
    public ResponseEntity<?> removeBookFromWishlist(@RequestBody WishlistRequest wishlistRequest) {
        try {
            System.out.println("in book service: " + wishlistRequest);
            return ResponseEntity.ok(bookService.removeBookFromWishlist(wishlistRequest));
        } catch (CustomException e) {
            return ResponseEntity.status(e.getStatus()).body(e.getErrorMessage());
        }
    }


    @GetMapping("/user-wishlist/{id}")
    public ResponseEntity<?> getWishlistByUser(@PathVariable(name = "id") int userId) {
        try {
            return ResponseEntity.ok(bookService.getWishlistByUser(userId));
        } catch (CustomException e) {
            return ResponseEntity.status(e.getStatus()).body(e.getErrorMessage());
        }
    }


















}
