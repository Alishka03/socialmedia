package com.example.social.conttollers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class HelloController {


    @GetMapping("/hello")
    public ResponseEntity<String> sayHi(){
        return ResponseEntity.ok("Hello");
    }

}
