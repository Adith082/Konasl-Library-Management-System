package com.example.bookService.type;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TypeService {

    @Autowired
    private TypeRepository typeRepository;

    public List<Type> getAllTypes(){
        List<Type> types = new ArrayList<>();
        typeRepository.findAll().forEach(types::add);
        return types;
    }

    public Type getTypeById(String id){
        return typeRepository.findById(id).get();
    }

    public void addType(Type type){
        typeRepository.save(type);
    }

    public void updateType(Type type, String id){
        typeRepository.save(type);
    }

    public void deleteType(String id){
        typeRepository.deleteById(id);
    }


}
