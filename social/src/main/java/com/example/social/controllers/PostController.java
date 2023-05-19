package com.example.social.controllers;

import com.example.social.dto.CommentDto;
import com.example.social.entities.Comment;
import com.example.social.entities.Post;
import com.example.social.exception.EmptyPostException;
import com.example.social.exception.PostNotFoundException;
import com.example.social.pojo.PostResponse;
import com.example.social.repository.PostRepository;
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
    private final PostRepository postRepository;

    public PostController(UserService userService, PostService postService, CommentService commentService,
                          PostRepository postRepository) {
        this.userService = userService;
        this.postService = postService;
        this.commentService = commentService;
        this.postRepository = postRepository;
    }

    @GetMapping("")
    public ResponseEntity<?> getAllPosts() {
        return new ResponseEntity<>(postService.getAllPosts(), HttpStatus.OK);
    }
    @GetMapping("/no-auth")
    public ResponseEntity<?> getAllPostsNotAuth() {
        return new ResponseEntity<>(postService.getAllPostsNotAuth(), HttpStatus.OK);
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
    public ResponseEntity<?> deletePostById(@PathVariable("postId") int postId, @PathVariable("commentId") int commentId) {
        postService.deleteComment(postId, commentId);
        return new ResponseEntity<>("Deleted", HttpStatus.OK);
    }
    @DeleteMapping("/{postId}/delete")
    public ResponseEntity<?> deletePost(@PathVariable("postId") int postId){
        postService.deletePost(postId);
        return new ResponseEntity<>("DELETED",HttpStatus.OK);
    }

    @GetMapping("/{postId}/comments")
    public ResponseEntity<?> getCommentsByPostId(@PathVariable("postId") int postId) {
        Optional<Post> post = postService.getPostById(postId);
        if (post.isEmpty()) {
            throw new PostNotFoundException("Post not found with id : " + postId);
        }
        return new ResponseEntity<>(post.get().getComments(), HttpStatus.OK);
    }

    @PostMapping("/{postId}/photo/delete")
    public ResponseEntity<?> deletePhoto(@PathVariable("postId") int postId) {
        postService.deletePhoto(postId);
        return new ResponseEntity<>("DELETED", HttpStatus.OK);
    }

    @PostMapping("/{postId}/comments/create")
    public ResponseEntity<?> addComment(@PathVariable("postId") int postId, @RequestBody CommentDto content) {
        Optional<Post> post = postService.getPostById(postId);
        if (post.isEmpty()) {
            throw new PostNotFoundException("Post not found with id : " + postId);
        }
        Comment comment = commentService.addingComment(content,post.get());
        return new ResponseEntity<>(comment, HttpStatus.CREATED);
    }

    @PostMapping("/{postId}/like")
    public ResponseEntity<?> like(@PathVariable("postId") int postId) {
        boolean flag = postService.likePost(postId);
        PostResponse response = new PostResponse();
        Optional<Post> post = postService.getPostById(postId);
        response.setLikedByAuthUser(post.get().getLikeList().contains(userService.getAuthenticatedUser()));
        response.setPost(post.get());
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @PatchMapping("/{postId}/update")
    public ResponseEntity<?> update(@PathVariable("postId") int postId, @RequestParam(value = "content", required = false) Optional<String> content,
                                    @RequestParam(value = "file", required = false) Optional<MultipartFile> file) throws IOException {
        if ((content.isEmpty() || content.get().length() <= 0) && (file.isEmpty() || file.get().getSize() <= 0)) {
            throw new EmptyPostException();
        }
        String contentToAdd = content.isEmpty() ? null : content.get();
        MultipartFile postImageToAdd = file.isEmpty() ? null : file.get();
        Post post = postService.updatePost(postId,contentToAdd,postImageToAdd);
        PostResponse response = new PostResponse();
        response.setLikedByAuthUser(post.getLikeList().contains(userService.getAuthenticatedUser()));
        response.setPost(post);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

}
