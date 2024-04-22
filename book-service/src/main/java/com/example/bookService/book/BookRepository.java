package com.example.bookService.book;


import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BookRepository extends CrudRepository<Book, Integer> {
     public List<Book> findByTypeId(String typeId);

     public List<Book> findByIsBookApproved(boolean isBookApproved);
}
