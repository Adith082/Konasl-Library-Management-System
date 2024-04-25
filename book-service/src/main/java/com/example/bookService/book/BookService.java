package com.example.bookService.book;

import com.example.bookService.book.payload.LentBook;
import com.example.bookService.book.payload.Message;
import com.example.bookService.book.payload.UserWishlistResponse;
import com.example.bookService.book.payload.WishlistRequest;
import com.example.bookService.exceptions.CustomException;
import com.example.bookService.lendRecord.LendRecord;
import com.example.bookService.lendRecord.LendRecordRepository;
import com.example.bookService.wishlist.WishList;
import com.example.bookService.wishlist.WishlistRepository;
import lombok.Builder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class BookService {

    @Autowired
    BookRepository bookRepository;

    @Autowired
    WishlistRepository wishlistRepository;

    @Autowired
    LendRecordRepository lendRecordRepository;


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



// lend

    public Message lendBookToUser(int user_id, int book_id){
        System.out.println("inside book-service lend method");
        LendRecord lendRecord = lendRecordRepository.findByUserIdAndBookId(user_id, book_id);
        if(lendRecord != null){
            return new Message("already lent before and have not returned it yet");
        }   else{
            Book book = bookRepository.findById(book_id).get();

            if(book.isAvailable()){

                if(book.getCount() == 1){
                    book.setAvailable(false);
                }
                book.setCount(book.getCount() - 1);
                updateBook(book, book_id);
                lendRecord = LendRecord.builder().userId(user_id).bookId(book_id).build();
                lendRecordRepository.save(lendRecord);
                return new Message("Book successfully lent to user " + user_id);
            } else{
                return new Message("book is not available for lending");
            }
        }
    }

    public Message returnBookToLibrary(int user_id, int book_id){


        System.out.println("inside return BootToLibrary ");
        Book book = bookRepository.findById(book_id).get();
        book.setCount(book.getCount() + 1);
        if(book.getCount() == 1){
            book.setAvailable(true);
        }
        updateBook(book, book_id);
        System.out.println("Book Updated");
        LendRecord lendRecord = lendRecordRepository.findByUserIdAndBookId(user_id, book_id);
        lendRecordRepository.deleteById(lendRecord.getId());
        System.out.println("delete method is completed");
        return new Message("Book is returned to library successfully");

    }

    public List<LentBook> getAllLentBooksByUid(int user_id){
        List<LendRecord> lendRecordsOfUser = lendRecordRepository.findByUserId(user_id);
        List<Integer> bookIdListOfUser = new ArrayList<>();
        List<LentBook> lentBooks = new ArrayList<>();
        for (LendRecord lendRecord : lendRecordsOfUser) {
            int bookId = lendRecord.getBookId(); // Assuming there's a getter method for bookId in LendRecord class
            bookIdListOfUser.add(bookId);
        }

        for(Integer bookId : bookIdListOfUser){
            Book book = getBookById(bookId);
            LentBook lentBook = LentBook.builder().book_id(book.getId()).title(book.getTitle()).author(book.getAuthor()).build();
            lentBooks.add(lentBook);
        }
            return lentBooks;
    }

}
