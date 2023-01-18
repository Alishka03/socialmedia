package com.example.social.conttollers;

import com.example.social.entities.Post;
import com.example.social.exception.EmptyPostException;
import com.example.social.exception.PostNotFoundException;
import com.example.social.services.PostService;
import com.example.social.services.UserService;
import com.example.social.util.FileUploadUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/posts")
public class PostController {
    private final UserService userService;
    private final PostService postService;

    public PostController(UserService userService, PostService postService ) {
        this.userService = userService;
        this.postService = postService;
    }

    @GetMapping("")
    public ResponseEntity<?> getAllPosts(){
        return new ResponseEntity(postService.getAllPosts(),HttpStatus.OK);
    }
    @PostMapping("/create")
    public ResponseEntity<?> createPost(@RequestParam(value = "content", required = false) Optional<String> content,
                                        @RequestParam(value = "file", required = false) Optional<MultipartFile> file) throws IOException {
        if ((content.isEmpty() || content.get().length() <= 0) && (file.isEmpty() || file.get().getSize() <= 0)) {
            throw new EmptyPostException();
        }
        String contentToAdd = content.isEmpty() ? null : content.get();
        MultipartFile postImageToAdd = file.isEmpty() ? null : file.get();
        Post createdPost = postService.createNewPost(contentToAdd, postImageToAdd);
        return new ResponseEntity<>(createdPost, HttpStatus.CREATED);
    }

    @GetMapping("/{postId}")
    public Optional<Post> getPostById(@PathVariable("postId") int postId) {
        Optional<Post> post = postService.getPostById(postId);
        if (post.isEmpty()) {
            throw new PostNotFoundException("Post not found with id : " + postId);
        }
        return post;
    }
}
