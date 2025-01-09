package org.flitter.backend.repository;
import org.flitter.backend.entity.CommentAdapter;
import org.flitter.backend.entity.Task;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<CommentAdapter, Long> {
    List<CommentAdapter> findByBelongedTask(Task task);
}
