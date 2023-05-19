package com.example.social.controllers;

import com.example.social.dto.UserInfoDto;
import com.example.social.dto.UserUpdateInfoDTO;
import com.example.social.entities.Post;
import com.example.social.entities.User;
import com.example.social.exception.EmptyPostException;
import com.example.social.pojo.PostResponse;
import com.example.social.pojo.ProfileResponse;
import com.example.social.pojo.UserResponse;
import com.example.social.response.MessageResponse;
import com.example.social.services.PostService;
import com.example.social.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
@CrossOrigin(origins = "http://localhost:4200", maxAge = 4800, allowCredentials = "false")
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
    public ResponseEntity<?> gettingCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.userByUsername(auth.getName()).get();
        return new ResponseEntity<>(new ProfileResponse("SUCCESS", user), HttpStatus.OK);
    }

    @PatchMapping("/profile/update")
    private ResponseEntity<?> updateUserInfo(@RequestBody UserInfoDto userInfoDto) {
        System.out.println(userInfoDto);
        userService.testing();
        return new ResponseEntity<>("OKAY", HttpStatus.OK);
    }

    @GetMapping("/myposts")
    public ResponseEntity<?> myPosts() {
        postService.getPostsOfUser();
        User authUser = getAuthenticatedUser();
        List<PostResponse> posts = new ArrayList<>();
        for (Post post : postService.getPostsOfUser()) {
            PostResponse response = new PostResponse();
            response.setLikedByAuthUser(post.getLikeList().contains(authUser));
            response.setPost(post);
            posts.add(response);
        }
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    @RequestMapping(value = "/update-profile-image", method = RequestMethod.POST)
    public ResponseEntity<?> updateProfileImage(@RequestParam(value = "file") Optional<MultipartFile> file) throws IOException {
        if ((file.isEmpty() || file.get().getSize() <= 0)) {
            throw new EmptyPostException();
        }
        MultipartFile postImageToAdd = file.isEmpty() ? null : file.get();
        log.info("Adding image");
        User user = userService.updateImage(postImageToAdd);
        return new ResponseEntity<>(user, HttpStatus.OK);
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
    public ResponseEntity<?> getAllUsers() {
        User authUser = getAuthenticatedUser();
        System.out.println(authUser.toString());
        List<UserResponse> userResponses = new ArrayList<>();
        List<User> users = userService.findAllUsers();
        for (User user : users) {
            UserResponse userResponse = new UserResponse();
            userResponse.setFollowedByAuthUser(authUser.getFollowingUsers().contains(user));
            userResponse.setUser(user);
            userResponses.add(userResponse);
        }
        return new ResponseEntity<>(userResponses, HttpStatus.OK);
    }

    @GetMapping("/users/{userId}/posts")
    public ResponseEntity<?> getPostsOfUser(@PathVariable int userId){
        return new ResponseEntity<>(postService.getPostsOfUserById(userId), HttpStatus.OK);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<?> getUsersByUserId(@PathVariable("userId") int userId) {
        Optional<User> user = userService.userById(userId);
        if (user.isEmpty()) {
            return new ResponseEntity<>(new MessageResponse("NOT FOUND"), HttpStatus.NOT_FOUND);
        } else {
            UserResponse userResponse = new UserResponse();
            User authUser = getAuthenticatedUser();
            userResponse.setFollowedByAuthUser(authUser.getFollowingUsers().contains(user.get()));
            userResponse.setUser(user.get());
            return new ResponseEntity<>(userResponse, HttpStatus.OK);
        }
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

    public User getAuthenticatedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userService.userByUsername(auth.getName()).get();
    }

    @PostMapping("/update-profile")
    public ResponseEntity<?> updateProfile(@RequestBody UserUpdateInfoDTO userUpdateInfoDTO){
        User user = userService.updateProfile(userUpdateInfoDTO);
        return new ResponseEntity<>(user , HttpStatus.OK);
    }
    @GetMapping("/users/search/{filter}")
    public ResponseEntity<?> usersFilter(@PathVariable String filter){
        User authUser = getAuthenticatedUser();
        List<UserResponse> userResponses = new ArrayList<>();
        List<User> users = userService.search(filter);
        for (User user : users) {
            UserResponse userResponse = new UserResponse();
            userResponse.setFollowedByAuthUser(authUser.getFollowingUsers().contains(user));
            userResponse.setUser(user);
            userResponses.add(userResponse);
        }
        return new ResponseEntity<>(userResponses,HttpStatus.OK);
    }
}
