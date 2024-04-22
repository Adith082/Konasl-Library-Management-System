package com.example.bookService.type;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TypeController {
    @Autowired
    private TypeService typeService;

    @RequestMapping("/types")
    public List<Type> getAllTypes(){
        return typeService.getAllTypes();
    }

    @RequestMapping("/types/{id}")
    public Type getTypeById(@PathVariable String id){
        return typeService.getTypeById(id);
    }


    @RequestMapping(method = RequestMethod.POST, value="/types")
    public void addType(@RequestBody Type type){
        typeService.addType(type);
    }

    @RequestMapping(method = RequestMethod.PUT, value="/types/{id}")
    public void updateType(@RequestBody Type type, @PathVariable String id){
        typeService.updateType(type, id);
    }

    @RequestMapping(method = RequestMethod.DELETE, value="/types/{id}")
    public void deleteType(@RequestBody Type type, @PathVariable String id){
        typeService.deleteType(id);
    }






}
