package org.flitter.backend.service;

import jakarta.annotation.PostConstruct;
import org.flitter.backend.entity.enums.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import org.flitter.backend.repository.UserRepository;
import org.flitter.backend.entity.User;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public UserService(UserRepository userRepository,
                       BCryptPasswordEncoder bCryptPasswordEncoder,
                       AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.authenticationManager = authenticationManager;
    }

    // 注册用户
    public void registerUser(User user) {
        // 参数校验
        if (user.getUsername() == null || user.getPassword() == null) {
            throw new IllegalArgumentException("用户名和密码不能为空");
        }
        if (user.getEmail() == null) {
            throw new IllegalArgumentException("邮箱不能为空");
        }

        // 检查是否重名
        User existingUser = userRepository.findByUsername(user.getUsername());
        if (existingUser != null) {
            throw new IllegalArgumentException("用户名已经被使用过了");
        }

        // 加密密码，设置角色
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setRole(Role.USER);

        // 保存用户
        userRepository.save(user);
    }

    public void authenticateUser(User loginRequest) {
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );
            SecurityContextHolder.getContext().setAuthentication(auth);
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("用户名或者密码不正确");
        }
    }

    @PostConstruct
    public void init() {    // 创建虚拟用户，用于邮件发送等
        if (userRepository.findByUsername("System") == null) {
            User user = new User();
            user.setUsername("System");
            user.setEmail("System@system.com");
            user.setPassword(bCryptPasswordEncoder.encode("system11223344556677"));
            user.setRole(Role.ADMIN);
            userRepository.save(user);
        }
    }
}
