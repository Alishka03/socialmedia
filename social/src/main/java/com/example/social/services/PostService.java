package com.example.social.services;

import com.example.social.entities.Comment;
import com.example.social.entities.Post;
import com.example.social.entities.User;
import com.example.social.exception.InvalidOperationException;
import com.example.social.exception.PostNotFoundException;
import com.example.social.repository.CommentRepository;
import com.example.social.repository.PostRepository;
import com.example.social.util.FileUploadUtil;
import com.example.social.util.FileNamingUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PostService {

    private String PATH_DIRECTORY = "C:\\Users\\Admin\\Desktop\\social\\social\\src\\main\\resources\\static\\images";
    private final PostRepository postRepository;
    private final UserService userService;
    private final FileNamingUtil fileNamingUtil;
    private final FileUploadUtil fileUploadUtil;
    private final CommentRepository commentRepository;


    public PostService(PostRepository postRepository, UserService userService, FileNamingUtil fileNamingUtil, FileUploadUtil fileUploadUtil, CommentRepository commentRepository) {
        this.postRepository = postRepository;
        this.userService = userService;
        this.fileNamingUtil = fileNamingUtil;
        this.fileUploadUtil = fileUploadUtil;
        this.commentRepository = commentRepository;
    }

    public List<Post> getAllPosts() {
        return postRepository.findAllByOrderByDateCreatedAsc();
    }

    @Transactional
    public void savePost(Post post) {
        post.setDateCreated(new Date());
    }

    @Transactional
    public Post createNewPost(String content, MultipartFile postPhoto) {
        User authUser = userService.getAuthenticatedUser();
        Post newPost = new Post();
        newPost.setAuthor(authUser);
        newPost.setDateCreated(new Date());
        newPost.setContent(content);
        if (postPhoto != null && postPhoto.getSize() > 0) {
            System.out.println("PATH_DIRECTORY : " + PATH_DIRECTORY);
            String newPhotoName = fileNamingUtil.nameFile(postPhoto);
            String newPhotoUrl = authUser.getUsername() + newPost.getId() + newPhotoName;
            newPost.setPostPhoto(newPhotoUrl);
            try {
                fileUploadUtil.saveNewFile(PATH_DIRECTORY, newPhotoName, postPhoto);
            } catch (IOException e) {
                System.out.println("PATH NOT FOUND");
                throw new RuntimeException();
            }
        }
        return postRepository.save(newPost);
    }

    public Optional<Post> getPostById(int postId) {
        Optional<Post> post = postRepository.findById(postId);
        return post;
    }

    public void deleteComment(int postId, int commentId) {
        User user = userService.getAuthenticatedUser();
        Optional<Post> post = getPostById(postId);
        Optional<Comment> comment = commentRepository.findById(commentId);
        if (post.isEmpty() || comment.isEmpty()) {
            throw new PostNotFoundException("Post or Comment not found with ID:" + postId + " ,commentID:" + commentId);
        }

        if (comment.get().getAuthor().equals(user)) {
            commentRepository.deleteById(commentId);
        } else {
            throw new InvalidOperationException("You dont have opportunity to delete");
        }
    }
}
