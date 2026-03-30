package com.example.demo2.service;
import com.example.demo2.dto.LoginResponse;
import com.example.demo2.dto.UserQuery;
import com.example.demo2.dto.UserSaveRequest;
import com.example.demo2.entity.Role;
import com.example.demo2.exception.BusinessException;
import com.example.demo2.exception.LoginFailException;
import com.example.demo2.repository.RoleRepository;
import com.example.demo2.util.JwtUtil;
import com.example.demo2.dto.UserDTO;
import com.example.demo2.entity.User;
import com.example.demo2.repository.UserRepository;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;


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

    // 分页查询
    public Page<UserDTO> getUsers(UserQuery query) {
        Pageable pageable = PageRequest.of(query.getPageNum() - 1, query.getPageSize(), Sort.by("createTime").descending());

        Specification<User> spec = (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (StringUtils.hasText(query.getUsername())) {
                predicates.add(criteriaBuilder.like(root.get("username"), "%" + query.getUsername() + "%"));
            }
            if (StringUtils.hasText(query.getNickname())) {
                predicates.add(criteriaBuilder.like(root.get("nickname"), "%" + query.getNickname() + "%"));
            }
            // 角色关联查询 (简化版，实际可能需要 join)
            if (StringUtils.hasText(query.getRole())) {
                Join<User, Role> roleJoin = root.join("roles");
                predicates.add(criteriaBuilder.equal(roleJoin.get("name"), query.getRole()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        return userRepository.findAll(spec, pageable).map(this::convertToDTO);
    }
    // 转换辅助方法
    private UserDTO convertToDTO(User user) {
        UserDTO dto = new UserDTO();
        BeanUtils.copyProperties(user, dto);
        dto.setRoles(user.getRoles().stream().map(Role::getName).toList());
        return dto;
    }
    // 删除
    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new BusinessException("用户不存在");
        }
        userRepository.deleteById(id);
    }
    // 保存 (新增或修改)
    @Transactional
    public void saveUser(UserSaveRequest request) {
        User user;
        if (request.getId() == null) {
            // 新增
            if (userRepository.findByUsername(request.getUsername()).isPresent()) {
                throw new BusinessException("用户名已存在");
            }
            user = new User();
            user.setPassword(passwordEncoder.encode(request.getPassword())); // 必须加密
            user.setUsername(request.getUsername());
        } else {
            // 修改
            user = userRepository.findById(request.getId())
                    .orElseThrow(() -> new BusinessException("用户不存在"));

            // 如果前端传了密码，则更新密码
            if (StringUtils.hasText(request.getPassword())) {
                user.setPassword(passwordEncoder.encode(request.getPassword()));
            }
            // user.setNickname(request.getNickname());
            // user.setEmail(request.getEmail());
            // user.setEnabled(request.getEnabled());
        }

        // 处理角色关联
        if (request.getRoles() != null) {
            List<Role> roles = new ArrayList<>();
            for (String roleName : request.getRoles()) {
                roleRepository.findByName(roleName)
                        .ifPresent(roles::add);
            }
            user.setRoles(roles);
        }

        userRepository.save(user);
    }

}