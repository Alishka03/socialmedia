package com.example.social.conttollers;

import com.example.social.dto.AuthenticationDTO;
import com.example.social.response.JwtResponse;
import com.example.social.response.MessageResponse;
import com.example.social.security.jwt.JwtUtil;
import com.example.social.entities.User;
import com.example.social.services.RegistrationService;
import com.example.social.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin("http://localhost:3000/")
@RequestMapping("/auth")
public class AuthController {
    private final JwtUtil jwtUtil;
    private final RegistrationService registrationService;
    private final AuthenticationManager authenticationManager;
    private  final UserService userService;
    public AuthController(JwtUtil jwtUtil, RegistrationService registrationService, AuthenticationManager authenticationManager, UserService userService) {
        this.jwtUtil = jwtUtil;
        this.registrationService = registrationService;
        this.authenticationManager = authenticationManager;
        this.userService = userService;
    }
    @PostMapping("/login")
    public ResponseEntity<?> performLogin(@RequestBody AuthenticationDTO authenticationDTO) {
        UsernamePasswordAuthenticationToken authInputToken =
                new UsernamePasswordAuthenticationToken(authenticationDTO.getUsername(),
                        authenticationDTO.getPassword());

        try {
            authenticationManager.authenticate(authInputToken);
        } catch (BadCredentialsException e) {
            return new ResponseEntity(HttpStatus.OK);
        }
        System.out.println(authInputToken.getPrincipal().toString());
        String token = jwtUtil.generateToken(authenticationDTO.getUsername());

        return ResponseEntity
                .ok(new JwtResponse(token, Long.valueOf(1) , "ssas" , "12121"));

    }
    @PostMapping("/register")
    public Map<String, String> register(@RequestBody User user) {
        registrationService.reqisterUser(user);
        String token = jwtUtil.generateToken(user.getUsername());
        return Map.of("token", token);
    }
    @ExceptionHandler
    private ResponseEntity<MessageResponse> handleException(BadCredentialsException e) {
        MessageResponse error = new MessageResponse(e.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

}
