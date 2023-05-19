package com.example.social.services;

import com.example.social.dto.CommentDto;
import com.example.social.entities.Comment;
import com.example.social.entities.Post;
import com.example.social.entities.User;
import com.example.social.exception.InvalidOperationException;
import com.example.social.exception.PostNotFoundException;
import com.example.social.pojo.PostResponse;
import com.example.social.repository.CommentRepository;
import com.example.social.repository.PostRepository;
import com.example.social.repository.UserRepository;
import com.example.social.util.FileUploadUtil;
import com.example.social.util.FileNamingUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PostService {

    private String PATH_DIRECTORY = "C:\\Users\\Admin\\Desktop\\social-media-front-back\\front-end-final-project\\src\\assets\\images";
    //"C:\Users\Admin\Desktop\social-media-front-back\front-end-final-project\src\assets\images"
    private final PostRepository postRepository;
    private final UserService userService;
    private final FileNamingUtil fileNamingUtil;
    private final FileUploadUtil fileUploadUtil;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;


    public PostService(PostRepository postRepository, UserService userService, FileNamingUtil fileNamingUtil, FileUploadUtil fileUploadUtil, CommentRepository commentRepository,
                       UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userService = userService;
        this.fileNamingUtil = fileNamingUtil;
        this.fileUploadUtil = fileUploadUtil;
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
    }
    public List<PostResponse> getAllPostsNotAuth() {
        List<Post> posts = postRepository.findAllByOrderByDateCreatedAsc();
        List<PostResponse> postResponses = new ArrayList<>();
        for(Post post:posts){
            PostResponse postResponse = new PostResponse();
            postResponse.setLikedByAuthUser(false);
            postResponse.setPost(post);
            postResponses.add(postResponse);
        }
        return postResponses;
    }


    public List<PostResponse> getAllPosts() {
        List<Post> posts = postRepository.findAllByOrderByDateCreatedAsc();
        List<PostResponse> postResponses = new ArrayList<>();
        User authUser = userService.getAuthenticatedUser();
        for(Post post:posts){
            PostResponse postResponse = new PostResponse();
            postResponse.setLikedByAuthUser(post.getLikeList().contains(authUser));
            postResponse.setPost(post);
            postResponses.add(postResponse);
        }
        return postResponses;
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
            String newPhotoUrl =  newPhotoName;
            newPost.setPostPhoto(newPhotoUrl);
            try {
                fileUploadUtil.saveNewFile(PATH_DIRECTORY, newPhotoName, postPhoto);
            } catch (IOException e) {
                System.out.println("PATH NOT FOUND");
                throw new RuntimeException();
            }
        }
        newPost.setCommentCount(0);
        newPost.setLikeCount(0);
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

    public void commentPost(CommentDto content,int postId){
        Optional<Post> targetPost = postRepository.findById(postId);
        User author = userService.getAuthenticatedUser();
        if(targetPost.isEmpty()){
            throw new InvalidOperationException("Post not found with id :"+postId);
        }else{
            Post post = targetPost.get();
            Comment com = new Comment();
            com.setAuthor(author);
            com.setContent(content.getContent());
            com.setDateCreated(new Date());
            com.setPost(post);
            post.getComments().add(com);
            post.setCommentCount(post.getCommentCount()+1);
            commentRepository.save(com);
            postRepository.save(post);
        }
    }
    public boolean likePost(int postId) {
        User authUser = userService.getAuthenticatedUser();
        Optional<Post> targetPost  = getPostById(postId);
        if(targetPost.isPresent()) {
            if(!targetPost.get().getLikeList().contains(authUser)){
                targetPost.get().setLikeCount(targetPost.get().getLikeCount() + 1);
                targetPost.get().getLikeList().add(authUser);
                System.out.println(targetPost.get().toString());
                postRepository.save(targetPost.get());
                authUser.getLikedPosts().add(targetPost.get());
                return true;
            }else{
                targetPost.get().setLikeCount(targetPost.get().getLikeCount() - 1);
                targetPost.get().getLikeList().remove(authUser);
                postRepository.save(targetPost.get());
                authUser.getLikedPosts().remove(targetPost.get());
                return false;
            }
        }else {
            throw new InvalidOperationException("POST NOT FOUND WITH ID : "+postId);
        }
    }

    public void deletePhoto(int postId) {
        User user= userService.getAuthenticatedUser();
        Optional<Post> post = getPostById(postId);
        if (post.isEmpty()) {
            throw new PostNotFoundException("Post not found with id : " + postId);
        }
        Post target = post.get();
        if(user.getId()==target.getAuthor().getId()){
            target.setPostPhoto(null);
            postRepository.save(target);
        }else throw new InvalidOperationException("Invalid operation exception");

    }

    public Post updatePost(int postId, String contentToAdd, MultipartFile postPhoto) {
        Optional<Post> post = postRepository.findById(postId);
        if(post.isEmpty()){
            throw new InvalidOperationException("Post not found with id: "+postId);
        }
        Post newPost= post.get();
        newPost.setContent(contentToAdd);
        if (postPhoto != null && postPhoto.getSize() > 0) {
            System.out.println("PATH_DIRECTORY : " + PATH_DIRECTORY);
            String newPhotoName = fileNamingUtil.nameFile(postPhoto);
            newPost.setPostPhoto(newPhotoName);
            try {
                fileUploadUtil.saveNewFile(PATH_DIRECTORY, newPhotoName, postPhoto);
            } catch (IOException e) {
                System.out.println("PATH NOT FOUND");
                throw new RuntimeException();
            }
        }
        return postRepository.save(newPost);
    }

    public List<Post> getPostsOfUser() {
        User user = userService.getAuthenticatedUser();
        return postRepository.findByAuthorIdOrderByDateCreatedDesc(user.getId());
    }

    public void deletePost(int postId) {
        User user = userService.getAuthenticatedUser();
        Optional<Post> post = postRepository.findById(postId);
        if(post.isEmpty()){
            throw new InvalidOperationException("Post not found with id: "+postId);
        }else {
            user.getPostsList().remove(post.get());
            postRepository.deleteById(postId);
        }
    }
    public List<PostResponse> getPostsOfUserById(int userId){
        User authUser = userService.getAuthenticatedUser();
        List<Post> posts = postRepository.findByAuthorId(userId);
        List<PostResponse> postResponses = new ArrayList<>();
        for(Post post : posts){
            PostResponse response = new PostResponse();
            response.setLikedByAuthUser(post.getLikeList().contains(authUser));
            response.setPost(post);
            postResponses.add(response);
        }
        return postResponses;
    }
}
