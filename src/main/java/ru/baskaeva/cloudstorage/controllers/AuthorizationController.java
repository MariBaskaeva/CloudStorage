package ru.baskaeva.cloudstorage.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.baskaeva.cloudstorage.models.User;

@RestController
@RequestMapping("/")
@CrossOrigin(origins = {"http://localhost:8080"})
public class AuthorizationController {

    @PostMapping("/login")
    public ResponseEntity logIn(@RequestBody User data){
        System.out.println(data);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/logout")
    public void logOut(){}

}
