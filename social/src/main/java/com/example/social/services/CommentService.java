package com.example.social.services;

import com.example.social.dto.CommentDto;
import com.example.social.entities.Comment;
import com.example.social.entities.Post;
import com.example.social.entities.User;
import com.example.social.repository.CommentRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserService userService;

    public CommentService(CommentRepository commentRepository, UserService userService) {
        this.commentRepository = commentRepository;
        this.userService = userService;
    }

    public Comment addingComment(CommentDto content,Post post){
        User authUser = userService.getAuthenticatedUser();
        Comment comment = new Comment();
        comment.setContent(content.getContent());
        comment.setDateCreated(new Date());
        comment.setAuthor(authUser);
        comment.setPost(post);
        authUser.getComments().add(comment);
        post.getComments().add(comment);
        return commentRepository.save(comment);
    }

}
