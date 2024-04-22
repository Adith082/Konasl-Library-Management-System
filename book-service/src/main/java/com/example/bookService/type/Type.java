package com.example.bookService.type;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Type {
    @Id
    private String id;
    private String name;
    private String description;

    public Type(){

    }
    public Type(String id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }
}

