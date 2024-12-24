package org.flitter.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.flitter.backend.repository.UserRepository;
import org.flitter.backend.entity.User;

@Service
public class UserService {
    private final UserRepository userRepository;

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    // 注册用户以及保存
    public void saveUser(User user) {
        userRepository.save(user);
    }

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
