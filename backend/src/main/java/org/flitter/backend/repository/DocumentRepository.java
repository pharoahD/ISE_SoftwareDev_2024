package org.flitter.backend.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.flitter.backend.entity.Document;          // 导入 Document 实体类
import org.flitter.backend.entity.DocumentVersion;
import java.util.List;
import org.flitter.backend.entity.Project;

public interface DocumentRepository extends JpaRepository<Document, Long> {
    List<Document> findByBelongsToProject(Project project);
}

