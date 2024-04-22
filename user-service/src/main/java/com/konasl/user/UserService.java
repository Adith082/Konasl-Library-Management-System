package com.konasl.user;

//import com.example.bookService.book.Book;
import com.konasl.user.payload.Message;
import com.konasl.user.payload.UserWishlistRequest;
import com.konasl.user.payload.UserWishlistResponse;
import com.konasl.user.payload.WishlistRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class UserService {

    private final RestTemplate restTemplate;
    private final String bookServiceBaseUrl = "http://localhost:8082"; // Replace with actual URL of book service
    @Autowired
    public UserService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    @Autowired
    UserRepository userRepository;


    public List<User> getAllUnapprovedUsers(){
        return userRepository.findByApprovalOfAdmin(false);
    }

    public List<User> getAllUsers(){
        List <User> users = new ArrayList<>();
         userRepository.findAll().forEach(users::add);
        return users;
    }
    public User getUserById(int id){
        return  userRepository.findById(id).get();
    }
    public void addUser(User user){
        userRepository.save(user);
    }
    public void updateUser(User user, int id){
        userRepository.save(user);
    }
    public void deleteUser(int id){
        userRepository.deleteById(id);
    }


    public boolean registerUser(User user){
       boolean isExist = userRepository.existsByEmail(user.getEmail());
       if(isExist){
           return false;
       }   userRepository.save(user);
           return true;
    }

    public Message loginUser(User user){
        String userEmail = user.getEmail();
        if(!userRepository.existsByEmail(userEmail)){
            return new Message("user not registered");
        }   User loggedInUser = userRepository.findByEmail(userEmail);
            String userPassword = user.getPassword();
            String loggedInPassword = loggedInUser.getPassword();
            if(!userPassword.equals(loggedInPassword)){
                return new Message("Wrong password given.");
            }   if(!loggedInUser.isApprovalOfAdmin()) {
                return new Message("Admin has not approved the account yet");
        }
                return new Message("Account approved and Logged in");
    }





/// for restTemplate test check
    public void deleteBookFromBookService(String typeId, String id) {
        String url = bookServiceBaseUrl + "admin/types/" + typeId + "/books/" + id;

        // Make HTTP DELETE request to book service
        restTemplate.exchange(url, HttpMethod.DELETE, null, Void.class);
    }



    public Message addToWishlist(UserWishlistRequest userWishlistRequest, int userId){
          String url = bookServiceBaseUrl + "/user-wishlist/add";
          System.out.println("created url for microservices + " + url);
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        WishlistRequest req = WishlistRequest.builder().bookId(userWishlistRequest.getBook_id()).userId(userId).
                build();


        HttpEntity<WishlistRequest> entity = new HttpEntity<>(req, headers);
      //   System.out.println("I am still alive");
        try {
            ResponseEntity<Message> response = restTemplate.exchange(url, HttpMethod.POST, entity, Message.class);
            // System.out.println(" and stillll");
            return response.getBody();
        } catch(HttpClientErrorException e){
            return new Message("Error occured !");
        }
    }




    public Message removeBookFromWishlist(UserWishlistRequest userWishlistRequest, int userId){
        String url = bookServiceBaseUrl + "/user-wishlist/remove";
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        WishlistRequest req = WishlistRequest.builder().bookId(userWishlistRequest.getBook_id()).userId(userId).
                build();
        HttpEntity<WishlistRequest> entity = new HttpEntity<>(req, headers);
        try {
            ResponseEntity<Message> response = restTemplate.exchange(url, HttpMethod.POST, entity, Message.class);
            return response.getBody();
        } catch(HttpClientErrorException e){
            return new Message("error occured !!!");
        }
    }

    public List<UserWishlistResponse> getUserWishlist(int userId) {

        String url = bookServiceBaseUrl + "/user-wishlist/" + userId;
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<?> entity = new HttpEntity<>(headers);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

            ResponseEntity<List<UserWishlistResponse>> response = restTemplate.exchange(url, HttpMethod.GET, entity,
                    new ParameterizedTypeReference<List<UserWishlistResponse>>() {});
            return response.getBody();

    }



















}
