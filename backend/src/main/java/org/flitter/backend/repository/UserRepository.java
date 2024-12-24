package org.flitter.backend.repository;

import org.springframework.data.repository.CrudRepository;

import org.flitter.backend.entity.User;

public interface UserRepository extends CrudRepository<User, Long> {
    User findByUsername(String username);
}
