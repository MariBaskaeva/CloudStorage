package ru.baskaeva.cloudstorage.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.baskaeva.cloudstorage.config.JwtTokenUtil;
import ru.baskaeva.cloudstorage.dto.CredentialDTO;
import ru.baskaeva.cloudstorage.dto.TokenDTO;
import ru.baskaeva.cloudstorage.models.User;
import ru.baskaeva.cloudstorage.repositories.UserRepository;

import java.util.List;

@CrossOrigin(origins = "${frontend.endpoint}")
@RestController
public class AuthenticationController {
    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenUtil tokenUtil;


    @PostMapping("/login")
    public TokenDTO login(@RequestBody CredentialDTO credential) {
        Authentication authentication;
        try {
            authentication = manager.authenticate(
                    new UsernamePasswordAuthenticationToken(credential.getLogin(), credential.getPassword()));
        } catch (Exception ex) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "400 - bad credentials", ex
            );
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);

        return new TokenDTO(tokenUtil.generateToken(userDetailsService.loadUserByUsername(credential.getLogin())));
    }

    @PostMapping("/logout")
    public void logout() {
        SecurityContextHolder.clearContext();
    }

    @GetMapping("/users")
    public List<User> getUsers() {
        return userRepository.findAll();
    }
}
