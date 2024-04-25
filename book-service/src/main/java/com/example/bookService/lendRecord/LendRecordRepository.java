package com.example.bookService.lendRecord;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LendRecordRepository extends CrudRepository<LendRecord, Integer> {
    LendRecord findByUserIdAndBookId(int userId, int bookId);
    List<LendRecord> findByUserId(int userId);
}
