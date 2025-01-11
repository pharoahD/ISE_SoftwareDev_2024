package org.flitter.backend.repository;

import org.flitter.backend.entity.Project;
import org.flitter.backend.entity.User;

import org.flitter.backend.dto.UserNameIdDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    User findByUsername(String username);

    List<UserNameIdDTO> findAllByProjectsContains(Project project);

    @Query("SELECT new org.flitter.backend.dto.UserNameIdDTO(p.id, p.username) FROM User p " +
            "WHERE p.id <> 1")
    List<UserNameIdDTO> findAllUserNameId(Pageable pageable);


    @Query("SELECT new org.flitter.backend.dto.UserNameIdDTO(p.id, p.username) FROM User p " +
            "WHERE p.username like :name and p.id <> 1")
    List<UserNameIdDTO> searchUserByUsernameLike(String name, Pageable pageable);

//    @Query("SELECT new org.flitter.backend.dto.UserIDRoleDTO(p.id, p.username, p.role) FROM User p " +
//            "WHERE p.id <> 1")
//    List<UserIDRoleDTO> findAllUserIDRole(Pageable pageable);
}
