package com.example.bookService.book;

import com.example.bookService.book.payload.Message;
import com.example.bookService.book.payload.UserWishlistResponse;
import com.example.bookService.book.payload.WishlistRequest;
import com.example.bookService.exceptions.CustomException;
import com.example.bookService.wishlist.WishList;
import com.example.bookService.wishlist.WishlistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookService {

    @Autowired
    BookRepository bookRepository;
    @Autowired
    WishlistRepository wishlistRepository;

    public List<Book> getAllBooks(String typeId){
        List <Book> books = new ArrayList<>();
        bookRepository.findByTypeId(typeId).forEach(books::add);
        return books;
    }
    public Book getBookById(int id){
       return  bookRepository.findById(id).get();
    }
    public void addBook(Book book){
        bookRepository.save(book);
    }
    public void updateBook(Book book, int id){
        bookRepository.save(book);
    }
    public void deleteBook(int id){
        bookRepository.deleteById(id);
    }




    public List<Book> getAllUnapprovedBooks(){
        return bookRepository.findByIsBookApproved(false);
    }
    public List<Book> getAllApprovedBooks(String typeId){

        List<Book> approvedBooks = new ArrayList<>();

        // Retrieve all books for the given typeId
        List<Book> allBooks = new ArrayList<>();
        bookRepository.findByTypeId(typeId).forEach(allBooks::add);

        // Filter approved books
        approvedBooks = allBooks.stream()
                .filter(Book::isBookApproved) // Filter by isBookApproved=true
                .toList();

        return approvedBooks;

    }


    public Message addBookToWishlist(WishlistRequest wishlistRequest) throws CustomException {


        int userId = wishlistRequest.getUserId();

        Book book = bookRepository.findById(wishlistRequest.getBookId()).
        orElseThrow(
               ()->   new CustomException(HttpStatus.NOT_FOUND, new Message("Book with id: " + wishlistRequest.getBookId() + " does not exists"))
        );
        WishList existingRecord = wishlistRepository.findByBookIdAndUserId(book.getId(), userId);
        WishList newRecord = WishList.builder().book(book).userId(userId).build();
        if(existingRecord != null){
            throw new CustomException(HttpStatus.CONFLICT, new Message("book already in the users wishlist"));
        }
        wishlistRepository.save(newRecord);
        return new Message(book.getTitle() + ", is added to the wishlist!");
    }
    public Message removeBookFromWishlist(WishlistRequest wishlistRequest) throws CustomException{
        int userId = wishlistRequest.getUserId();
        int bookId = wishlistRequest.getBookId();

        // Retrieve the Book object by its ID
        Book book = bookRepository.findById(bookId).get();

        WishList existingRecord = wishlistRepository.findByBookIdAndUserId(book.getId(), userId);
      //  if(existingRecord == null) {
        //    throw new CustomException(HttpStatus.NOT_FOUND, new Message("Book with id: " + bookId + " is not in users wishlist"));
        //}
        wishlistRepository.delete(existingRecord);
        return new Message(book.getTitle() + ", is removed from wishlist of userId" + userId);
    }





    public List<UserWishlistResponse> getWishlistByUser(int userId) throws CustomException {
        List<WishList> list = wishlistRepository.findAllByUserId(userId);
        List<UserWishlistResponse> userWishlist = new ArrayList<>();
        list.forEach(it -> {
          //  System.out.println(it);
            Book currentBook = it.getBook();
            userWishlist.add(UserWishlistResponse.builder().bookId(currentBook.getId()).title(currentBook.getTitle()).
                   author(currentBook.getAuthor()).build());
        });
        return userWishlist;
    }



















}
