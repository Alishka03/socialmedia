package com.example.social.conttollers;

import com.example.social.dto.UserInfoDto;
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
import org.springframework.web.bind.annotation.*;

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
        System.out.println(user);
        return user;
    }

    @GetMapping("/profile")
    public ResponseEntity<?> gettingCurrentUser(Authentication authentication) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = (String) authentication.getCredentials();
        User user = userService.userByUsername(username).get();
        System.out.println(user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
    @PatchMapping("/profile/update")
    private ResponseEntity<?> updateUserInfo(@RequestBody UserInfoDto userInfoDto){
        System.out.println(userInfoDto);
        userService.testing();
        return new ResponseEntity<>(  "OKAY" , HttpStatus.OK);
    }

    @GetMapping("/myposts")
    public List<Post> myPosts() {
        User user = getAuthenticatedUser();
        return user.getPostsList();
    }

    @GetMapping("/greeting")
    @PreAuthorize("isAuthenticated()")
    public String userAccess() {
        return "Congratulations! You are an authenticated user.";
    }

    @GetMapping("/followings")
    public ResponseEntity<?> getFollowings() {
        User user = getAuthenticatedUser();
        return new ResponseEntity<>(user.getFollowingUsers(), HttpStatus.OK);
    }

    @GetMapping("/followers")
    public ResponseEntity<?> getFollowers() {
        User user = getAuthenticatedUser();
        return new ResponseEntity<>(user.getFollowerUsers(), HttpStatus.OK);
    }
    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers(){
        return new ResponseEntity<>(userService.findAllUsers() ,HttpStatus.OK);
    }
    @GetMapping("/users/{userId}")
    public ResponseEntity<?> getUsersByUserId(@PathVariable("userId") int userId) {
        User user = userService.userById(userId).get();
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("/users/{userId}/follow")
    public ResponseEntity<?> followUserByUserId(@PathVariable("userId") int userId) {
        userService.followUser(userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/users/{userId}/unfollow")
    public ResponseEntity<?> unFollowUserByUserId(@PathVariable("userId") int userId) {
        userService.unfollowUser(userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }



    public final User getAuthenticatedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userService.userByUsername(auth.getCredentials().toString()).get();
    }
}
