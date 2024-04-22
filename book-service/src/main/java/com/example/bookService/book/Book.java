package com.example.bookService.book;


import com.example.bookService.type.Type;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String title;
    private String author;
    private boolean isBookApproved;
  //  private boolean borrowed;
    @ManyToOne
    Type type;



    public Book(){}

    public Book(String title, String author) {
       // this.id = id;
        this.title = title;
        this.author = author;
        this.isBookApproved = false;
    }
}
