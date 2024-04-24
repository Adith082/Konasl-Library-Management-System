package com.example.bookService.lendRecord;

import com.example.bookService.book.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service

public class LendRecordService {
    @Autowired
    LendRecordRepository lendRecordRepository;

    public List<LendRecord> getAllLendingRecords(){
        List <LendRecord> lendRecords = new ArrayList<>();
        lendRecordRepository.findAll().forEach(lendRecords::add);
        return lendRecords;
    }

}
