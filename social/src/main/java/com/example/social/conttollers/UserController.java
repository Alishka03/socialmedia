package com.example.social.conttollers;

import com.example.social.entities.Post;
import com.example.social.entities.User;
import com.example.social.services.PostService;
import com.example.social.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@CrossOrigin("http://localhost:3000/")
public class UserController {
    private final UserService userService;
    private final PostService postService;

    public UserController(UserService userService, PostService postService) {
        this.userService = userService;
        this.postService = postService;
    }

    @GetMapping("/home")
    public User getUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.userByUsername(auth.getCredentials().toString()).get();
        log.info(user.getName());
        return user;
    }

    @GetMapping("/profile")
    public ResponseEntity<?> gettingCurrentUser(Authentication authentication) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = (String) authentication.getCredentials();
        System.out.println(username);
        User user =  userService.userByUsername(username).get();
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/myposts")
    public List<Post> myPosts() {
        User user = getAuthenticatedUser();
        return user.getPostsList();
    }

    @PostMapping("/newpost")
    public ResponseEntity<?> createNewPost(String post) {
        return ResponseEntity.ok("OK");
    }

    @GetMapping("/greeting")
    @PreAuthorize("isAuthenticated()")
    public String userAccess() {
        return "Congratulations! You are an authenticated user.";
    }

    public final User getAuthenticatedUser(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userService.userByUsername(auth.getCredentials().toString()).get();
    }
}
