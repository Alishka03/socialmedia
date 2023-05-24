package com.example.social.services;

import com.example.social.dto.CommentDto;
import com.example.social.entities.Comment;
import com.example.social.entities.Post;
import com.example.social.entities.User;
import com.example.social.repository.CommentRepository;
import com.example.social.repository.PostRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserService userService;
    private final PostRepository postRepository;

    public CommentService(CommentRepository commentRepository, UserService userService,
                          PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.userService = userService;
        this.postRepository = postRepository;
    }

    public Comment addingComment(CommentDto content, Post post) {
        User authUser = userService.getAuthenticatedUser();
        Comment comment = new Comment();
        comment.setContent(content.getContent());
        comment.setDateCreated(new Date());
        comment.setAuthor(authUser);
        comment.setPost(post);
        authUser.getComments().add(comment);
        post.getComments().add(comment);
        post.setCommentCount(post.getCommentCount() + 1);
        System.out.println("POST POST"+post.toString());
        return commentRepository.save(comment);
    }

}
