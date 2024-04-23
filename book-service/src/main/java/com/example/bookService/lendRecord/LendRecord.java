package com.example.bookService.lendRecord;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class LendRecord {
    private int user_id;
    private int book_id;
}
