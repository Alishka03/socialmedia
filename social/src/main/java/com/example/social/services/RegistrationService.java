package com.example.social.services;

import com.example.social.dto.RegistrationDTO;
import com.example.social.entities.User;
import com.example.social.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class RegistrationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    public RegistrationService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    public User registerUser(RegistrationDTO user){
        User userToSave = new User();
        userToSave.setName(user.getName());
        userToSave.setUsername(user.getUsername());
        userToSave.setPassword(passwordEncoder.encode(user.getPassword()));
        userToSave.setSurname(user.getSurname());
        Date date = new Date();
        userToSave.setJoinDate(date);
        userToSave.setRole("ROLE_USER");
        return userRepository.save(userToSave);

//        user.setJoinDate(date);
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
//        userRepository.save(user);
    }
}
