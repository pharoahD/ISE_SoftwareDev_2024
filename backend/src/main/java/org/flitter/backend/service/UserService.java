package org.flitter.backend.service;

import org.flitter.backend.entity.enums.Permission;
import org.flitter.backend.utils.JwtTokenProvider;
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

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AuthenticationManager authenticationManager;
    private final RoleService roleService;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public UserService(UserRepository userRepository,
                       BCryptPasswordEncoder bCryptPasswordEncoder,
                       AuthenticationManager authenticationManager,
                       RoleService roleService, JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.authenticationManager = authenticationManager;
        this.roleService = roleService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public List<String> getPermissions(User user) {
        return user.getRoles().stream()
                .flatMap(role->role.getPermissions().stream())
                .map(Permission::getPermission)
                .collect(Collectors.toList());
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

        // 加密密码
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

        // 保存用户
        userRepository.save(user);

        // 设置角色
        roleService.addRoleToUser(user.getUsername(), "ROLE_USER");
    }

    public User authenticateUser(User loginRequest) {
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );
            SecurityContextHolder.getContext().setAuthentication(auth);
            return userRepository.findByUsername(loginRequest.getUsername());
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("用户名或者密码不正确");
        }
    }

    public String generateNewAccessKey(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new IllegalArgumentException("找不到对应的用户");
        }
        return jwtTokenProvider.generateAccessToken(user.getUsername(),
                this.getPermissions(user));
    }
}
