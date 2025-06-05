package com.wildlifebackend.wildlife.service.serviceImpl;


import com.wildlifebackend.wildlife.entitiy.OpenUser;
import com.wildlifebackend.wildlife.exception.ResourceNotFoundException;
import com.wildlifebackend.wildlife.repository.OpenUserRepository;
import com.wildlifebackend.wildlife.service.OpenUserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class OpenUserServiceImpl implements OpenUserService {

    @Autowired
    private  OpenUserRepository openuserRepository;
    @Autowired
    private  PasswordEncoder passwordEncoder;
    @Autowired
    private  AuthenticationManager authenticationManager;

//    @Autowired
//    private  PhotoRepository photoRepository;
//
//
//    public OpenUser createUserWithPhotos(OpenUser user, List<Photo> photos) {
//        for (Photo photo : photos) {
//            photo.setOwner(user);
//        }
//        user.getPhotos().addAll(photos);
//        return openuserRepository.save(user);
//    }



    @Transactional
    public void registerUser(OpenUser openuser) {
        if (openuserRepository.findByEmail(openuser.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email is already in use");
        }

        if (openuserRepository.findByNic(openuser.getNic()).isPresent()) {
            throw new IllegalArgumentException("NIC is already in use");
        }

        openuser.setPassword(passwordEncoder.encode(openuser.getPassword())); // Hash password
        openuserRepository.save(openuser);
    }


    public OpenUser loginUser(String email, String password) {
        OpenUser openuser = openuserRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (!passwordEncoder.matches(password, openuser.getPassword())) {
            throw new IllegalArgumentException("Invalid credentials");
        }

        // Authentication logic using Spring Security
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return openuser;
    }


    public OpenUser getAuthenticatedUser(org.apache.tomcat.util.net.openssl.ciphers.Authentication authentication) {
        return null;
    }


//    public OpenUser getAuthenticatedUser(Authentication authentication) {
//        if (authentication == null || !authentication.isAuthenticated()) {
//            throw new UsernameNotFoundException("Authentication information is missing or invalid.");
//        }
//
//        String email = authentication.getName(); // Assumes username = email
//
//        return openuserRepository.findByEmail(email)
//                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
//    }



}
