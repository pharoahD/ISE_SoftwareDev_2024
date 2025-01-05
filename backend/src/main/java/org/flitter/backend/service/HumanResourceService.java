package org.flitter.backend.service;

import org.flitter.backend.dto.UserNameIdDTO;
import org.flitter.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HumanResourceService {
    private final UserRepository userRepository;

    public HumanResourceService(@Autowired UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserNameIdDTO> fetchAllUsersIdNameWithLimit1000() {
        Pageable pageable = PageRequest.of(0, 1000);
        return userRepository.findAllUserNameId(pageable);
    }

    public List<UserNameIdDTO> searchUserIdNameLimit1000(String username) {
        Pageable pageable = PageRequest.of(0, 1000);
        return userRepository.searchUserByUsernameLike("%" + username + "%", pageable);
    }
}
