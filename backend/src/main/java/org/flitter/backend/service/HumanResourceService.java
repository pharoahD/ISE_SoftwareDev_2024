package org.flitter.backend.service;

import org.flitter.backend.config.SecurityConfig;
import org.flitter.backend.dto.UserIDRoleDTO;
import org.flitter.backend.dto.UserNameIdDTO;
import org.flitter.backend.entity.User;
import org.flitter.backend.entity.enums.Role;
import org.flitter.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HumanResourceService {
    private final UserRepository userRepository;
    private final SecurityConfig securityConfig;

    @Autowired
    public HumanResourceService(UserRepository userRepository,
                                SecurityConfig securityConfig) {
        this.userRepository = userRepository;
        this.securityConfig = securityConfig;
    }

    public List<UserNameIdDTO> fetchAllUsersIdNameWithLimit1000() {
        Pageable pageable = PageRequest.of(0, 1000);
        return userRepository.findAllUserNameId(pageable);
    }

    public List<UserNameIdDTO> searchUserIdNameLimit1000(String username) {
        Pageable pageable = PageRequest.of(0, 1000);
        return userRepository.searchUserByUsernameLike("%" + username + "%", pageable);
    }

    public List<UserIDRoleDTO> fetchAllUsersIdRoleWithLimit1000() {
        Pageable pageable = PageRequest.of(0, 1000);
        return userRepository.findAllUserIDRole(pageable);
    }

    public void updateRole(UserIDRoleDTO userIDRoleDTO) {
        User user = userRepository.findById(userIDRoleDTO.getId()).orElse(null);
        User currentUser = securityConfig.getCurrentUser();
        if (user != null) {
            if (isHigherThan(currentUser.getRole(), userIDRoleDTO.getRole())){
                user.setRole(userIDRoleDTO.getRole());
                userRepository.save(user);
            } else {
                throw new IllegalArgumentException("不能授予比自己权限相同或者更高的权限");
            }
        } else {
            throw new IllegalArgumentException("找不到用户");
        }
    }

    private int roleToPriority(Role role) {
        switch (role) {
            case ADMIN -> {
                return 4;
            }
            case APARTMENT_LEADER -> {
                return 3;
            }
            case PROJECT_LEADER -> {
                return 2;
            }
            case USER -> {
                return 1;
            }
            default -> {
                throw new IllegalArgumentException();
            }
        }
    }

    private Boolean isHigherThan(Role role1, Role role2) {
        return roleToPriority(role1) > roleToPriority(role2);
    }
}
