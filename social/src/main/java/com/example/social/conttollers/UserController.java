package com.example.social.conttollers;

import com.example.social.entities.User;
import com.example.social.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@CrossOrigin("http://localhost:3000/")
public class UserController {
    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }
    @GetMapping("/home")
    public User getUser(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.userByUsername(auth.getCredentials().toString()).get();
        log.info(user.getName());
        return user;
    }
    @GetMapping("/all")
    private String getAll(){
        return "all";
    }
    @GetMapping("/currentuser")
    public User gettingCurrentUser(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = (String) auth.getCredentials();
        System.out.println(username);
        return userService.userByUsername(username).orElseThrow(()-> new UsernameNotFoundException(username));
    }
    @GetMapping("/greeting")
    @PreAuthorize("isAuthenticated()")
    public String userAccess() {

        return "Congratulations! You are an authenticated user.";
    }
}
