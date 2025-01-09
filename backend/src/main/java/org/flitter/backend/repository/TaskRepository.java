package org.flitter.backend.repository;

import org.flitter.backend.entity.Task;
import org.flitter.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    Task findByTitle(String title);

    List<Task> findByBelongedProjectId(Long id);


    List<Task> findByAssignees(User assignees);


    // 用SQL，找出end时间快到了的
    // 还有某个人被指定了的
}
