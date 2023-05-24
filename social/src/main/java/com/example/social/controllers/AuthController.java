package com.example.social.controllers;

import com.example.social.dto.AuthenticationDTO;
import com.example.social.dto.RegistrationDTO;
import com.example.social.repository.UserRepository;
import com.example.social.response.JwtResponse;
import com.example.social.response.MessageResponse;
import com.example.social.security.jwt.JwtUtil;
import com.example.social.entities.User;
import com.example.social.services.RegistrationService;
import com.example.social.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@CrossOrigin("http://localhost:4200/")
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final JwtUtil jwtUtil;
    private final RegistrationService registrationService;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final UserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity<?> performLogin(@RequestBody AuthenticationDTO authenticationDTO) {
        UsernamePasswordAuthenticationToken authInputToken =
                new UsernamePasswordAuthenticationToken(authenticationDTO.getUsername(),
                        authenticationDTO.getPassword());
        try {
            authenticationManager.authenticate(authInputToken);
        } catch (BadCredentialsException e) {
            MessageResponse msg = new MessageResponse(e.getMessage());
            return new ResponseEntity<>(new JwtResponse(msg.getMessage()), HttpStatus.NOT_FOUND);
        }
        String username = (authInputToken.getPrincipal().toString());
        String token = jwtUtil.generateToken(authenticationDTO.getUsername());
        Optional<User> user = userService.userByUsername(username);
        HttpHeaders newHttpHeaders = new HttpHeaders();
        newHttpHeaders.add("Jwt-Token", token);
        if (user.isEmpty()) {
            return new ResponseEntity<>(new JwtResponse("FAILED"), HttpStatus.NOT_FOUND);
        } else {
            user.get().setToken(token);
            log.info(user.get().toString());
            return new ResponseEntity<>(user.get(), newHttpHeaders, HttpStatus.OK);
        }
//        return user.map(value -> ResponseEntity
//                .ok(new JwtResponse("SUCCESS", token, value.getId(), value.getUsername()))).orElseGet(() -> new ResponseEntity<>(new JwtResponse("FAILED"), HttpStatus.NOT_FOUND));
    }

    @RequestMapping(path = "/register", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> register(@RequestBody RegistrationDTO user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            return new ResponseEntity<>(new MessageResponse("already exists"), HttpStatus.BAD_REQUEST);
        }
        User savedUser = registrationService.registerUser(user);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    @ExceptionHandler
    private ResponseEntity<MessageResponse> handleException(BadCredentialsException e) {
        MessageResponse error = new MessageResponse(e.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
