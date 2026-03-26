package com.example.demo2.service;
import com.example.demo2.dto.LoginResponse;
import com.example.demo2.util.JwtUtil;
import com.example.demo2.dto.UserDTO;
import com.example.demo2.entity.User;
import com.example.demo2.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.util.Optional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User register(String username, String password) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        return userRepository.save(user);
    }

//    public String login(String username, String password) {
//        Optional<User> user = userRepository.findByUsername(username);
//        if (user.isPresent() &&
//                passwordEncoder.matches(password, user.get().getPassword())) {
//
//            return JwtUtil.generateToken(username);
//        }
//
//        return null;
//    }
    public LoginResponse login(String username, String password) {

        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isPresent() &&
                passwordEncoder.matches(password, optionalUser.get().getPassword())) {
            User user = optionalUser.get();
            String token = JwtUtil.generateToken(username);
            UserDTO userDTO = new UserDTO(user.getId(), user.getUsername());
            return new LoginResponse(token, userDTO);
        }
        return null;
    }


    public List<UserDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(user -> new UserDTO(user.getId(), user.getUsername()))
                .collect(Collectors.toList());
    }
}