package com.example.social.controllers;

import com.example.social.dto.UserUpdateInfoDTO;
import com.example.social.entities.User;
import com.example.social.repository.PostRepository;
import com.example.social.services.CommentService;
import com.example.social.services.PostService;
import com.example.social.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private final PostService postService;
    private final CommentService commentService;
    private final PostRepository postRepository;

    public AdminController(UserService userService, PostService postService, CommentService commentService,
                           PostRepository postRepository) {
        this.userService = userService;
        this.postService = postService;
        this.commentService = commentService;
        this.postRepository = postRepository;
    }
    @GetMapping("/users")
    public List<User> findAll(){
        return userService.findAllUsers();
    }

    @PostMapping("/users/{userId}/update")
    public ResponseEntity<?> updateProfile(@PathVariable("userId")int userId , @RequestBody UserUpdateInfoDTO userUpdateInfoDTO){

        User user = userService.updateProfileAdmin(userUpdateInfoDTO,userId);
        return new ResponseEntity<>(user , HttpStatus.OK);
    }
    @DeleteMapping("/users/{userId}/delete")
    public ResponseEntity<?> deleteUser(@PathVariable int userId){
        userService.deleteUser(userId);
        return new ResponseEntity<>("User deleted with id: "+userId , HttpStatus.OK);
    }
}
