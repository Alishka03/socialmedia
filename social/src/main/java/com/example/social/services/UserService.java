package com.example.social.services;

import com.example.social.dto.UserInfoDto;
import com.example.social.entities.User;
import com.example.social.exception.InvalidOperationException;
import com.example.social.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public Optional<User> userById(int id) {
        return userRepository.findById(id);
    }

    public Optional<User> userByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public final User getAuthenticatedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userByUsername(auth.getCredentials().toString()).get();
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public void followUser(int userId) {
        User authUser = getAuthenticatedUser();
        if (authUser.getId() != userId) {
            User userToFollow = userById(userId).get();
            authUser.getFollowingUsers().add(userToFollow);
            userToFollow.getFollowerUsers().add(authUser);

        } else {
            throw new InvalidOperationException();
        }
    }

    public void testing() {
        System.out.println("SOMETHING");
    }

    @Transactional
    public void updateUser(UserInfoDto userInfoDto) {
        System.out.println(userInfoDto);
    }

    @Transactional
    public void unfollowUser(int userId) {
        User authUser = getAuthenticatedUser();
        if (authUser.getId() != (userId)) {
            User userToUnfollow = userById(userId).get();
            authUser.getFollowingUsers().remove(userToUnfollow);
            userToUnfollow.getFollowerUsers().remove(authUser);

        } else {
            throw new InvalidOperationException();
        }
    }
}
