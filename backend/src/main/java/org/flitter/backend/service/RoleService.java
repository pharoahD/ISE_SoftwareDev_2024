package org.flitter.backend.service;

import org.flitter.backend.entity.Role;
import org.flitter.backend.entity.User;
import org.flitter.backend.repository.RoleRepository;
import org.flitter.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RoleService {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository, UserRepository userRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public boolean addRoleToUser(String username, String rolename) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new IllegalArgumentException("用户不存在");
        }

        Role role = roleRepository.findByName(rolename);
        if (role == null) {
            throw new IllegalArgumentException("角色不存在");
        }
        if (user.getRoles().contains(role)) {
            throw new IllegalArgumentException("用户已经拥有角色");
        }

        user.getRoles().add(role);
        userRepository.save(user);
        return true;
    }
}
