package com.konasl.user;

import com.konasl.user.payload.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.konasl.user.payload.UserWishlistRequest;

import java.util.List;

@RestController
public class UserController {


    @Autowired
    UserService userService;

    @RequestMapping("/admin/users")
    public List<User> getAllUsers(){
        return userService.getAllUsers();
    }

    @RequestMapping("/users/{id}")
    public User getUserById(@PathVariable int id) {
        return userService.getUserById(id);
    }

    @RequestMapping(method= RequestMethod.POST, value="/users")
    public void addUser(@RequestBody User user){
        userService.addUser(user);
    }

    @RequestMapping(method=RequestMethod.PUT, value="/admin/users/{id}")
    public void updateUser( @PathVariable int id, @RequestBody User user){
        userService.updateUser(user, id);
    }

    @RequestMapping(method=RequestMethod.DELETE, value="/admin/users/{id}")
    public void deleteUser(@PathVariable int id){
        userService.deleteUser(id);
    }







    @RequestMapping(method=RequestMethod.GET, value="/admin/unapprovedUsers")
    public List<User> getAllUnapprovedUsers(){
        return userService.getAllUnapprovedUsers();
    }


    @RequestMapping(method=RequestMethod.PUT, value="/admin/approveUser/{id}")
    public Message approveUser(@PathVariable int id){
        User user = userService.getUserById(id);
      //  System.out.println(user);
        user.setApprovalOfAdmin(true);
        userService.updateUser(user, id);
        System.out.println(user);
        return new Message("User " + id + " is approved");
    }


    @RequestMapping(method= RequestMethod.POST, value="/register")
    public Message userRegister(@RequestBody User user){
        if(userService.registerUser(user)){
            return new Message("User is registered");
        }   return new Message("User already registered before");
    }


    @RequestMapping(method= RequestMethod.POST, value="/login")
    public Message loginUser(@RequestBody User user){
            return userService.loginUser(user);
    }


    @RequestMapping(method= RequestMethod.DELETE, value="/admin/types/{typeId}/books/{id}")
    public void deleteBookFromBookService(@PathVariable String typeId, @PathVariable String id) {
        // Call the UserService method to delete the book from the BookService
        userService.deleteBookFromBookService(typeId, id);
    }


    @RequestMapping(method=RequestMethod.POST, value="/users/{id}/wishlist")
    public Message addBookToWishlist(@RequestBody UserWishlistRequest wishlistRequest,
                                               @PathVariable int id) {

            return userService.addToWishlist(wishlistRequest, id);

    }



    @PostMapping("/{id}/wishlist/remove-book")
    public ResponseEntity<?> removeBookFromWishlist(@RequestBody UserWishlistRequest wishlistRequest,
                                                    @PathVariable(name = "id")int userId) {

            return ResponseEntity.ok(userService.removeBookFromWishlist(wishlistRequest, userId));
    }


    @GetMapping("/{id}/wishlist")
    public ResponseEntity<?> getUserWishlist(@PathVariable(name = "id") int userId) {
        return ResponseEntity.ok(userService.getUserWishlist(userId));
    }











































}
