package com.example.social.controllers;

import com.example.social.dto.CommentDto;
import com.example.social.entities.Comment;
import com.example.social.entities.Post;
import com.example.social.exception.EmptyPostException;
import com.example.social.exception.PostNotFoundException;
import com.example.social.services.CommentService;
import com.example.social.services.PostService;
import com.example.social.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/posts")
public class PostController {
    private final UserService userService;
    private final PostService postService;
    private final CommentService commentService;

    public PostController(UserService userService, PostService postService, CommentService commentService) {
        this.userService = userService;
        this.postService = postService;
        this.commentService = commentService;
    }

    @GetMapping("")
    public ResponseEntity<?> getAllPosts() {
        return new ResponseEntity(postService.getAllPosts(), HttpStatus.OK);
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
    @DeleteMapping("/{postId}/comments/{commentId}")
    public ResponseEntity<?> deletePostById(@PathVariable("postId") int postId,@PathVariable("commentId") int commentId){
        postService.deleteComment(postId,commentId);
        return new ResponseEntity<>("Deleted",HttpStatus.OK);
    }

    @GetMapping("/{postId}/comments")
    public ResponseEntity<?> getCommentsByPostId(@PathVariable("postId") int postId) {
        Optional<Post> post = postService.getPostById(postId);
        if (post.isEmpty()) {
            throw new PostNotFoundException("Post not found with id : " + postId);
        }
        return new ResponseEntity<>(post.get().getComments(),HttpStatus.OK);
    }

    @PostMapping("/{postId}/comments/create")
    public Comment addComment(@PathVariable("postId") int postId,@RequestBody CommentDto content) {
        Optional<Post> post = postService.getPostById(postId);
        if (post.isEmpty()) {
            throw new PostNotFoundException("Post not found with id : " + postId);
        }
        return commentService.addingComment(content,post.get());
    }


}
