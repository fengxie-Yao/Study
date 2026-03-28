package com.example.demo2.service;
import com.example.demo2.dto.LoginResponse;
import com.example.demo2.exception.BusinessException;
import com.example.demo2.exception.LoginFailException;
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

    public void register(String username, String password) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new BusinessException(409, "用户名 [" + username + "] 已存在，请更换");
        }
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
    }


    public LoginResponse login(String username, String password) {

        Optional<User> optionalUser = userRepository.findByUsername(username);
        User user = optionalUser.orElseThrow(() -> new RuntimeException("用户不存在"));
        if (!passwordEncoder.matches(password, optionalUser.get().getPassword())) {
            throw new LoginFailException("用户名或密码错误");
        }

        String token = JwtUtil.generateToken(user);
        UserDTO userDTO = new UserDTO(user.getId(), user.getUsername());
        return new LoginResponse(token, userDTO);
    }


    public List<UserDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(user -> new UserDTO(user.getId(), user.getUsername()))
                .collect(Collectors.toList());
    }
}