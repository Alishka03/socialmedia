package com.example.social.services;

import com.example.social.entities.User;
import com.example.social.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    public Optional<User> userById(int id){
        return userRepository.findById(id);
    }
    public Optional<User> userByUsername(String username){
        return userRepository.findByUsername(username);
    }
    public final User getAuthenticatedUser(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userByUsername(auth.getCredentials().toString()).get();
    }
}
