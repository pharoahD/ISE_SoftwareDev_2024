package org.flitter.backend.repository;

import org.flitter.backend.entity.Document;
import org.flitter.backend.entity.DocumentVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DocumentVersionRepository extends JpaRepository<DocumentVersion, Long> {

    // 根据文档查询所有版本
    List<DocumentVersion> findByBelongsToDocument(Document document);

    // 根据文档ID和版本号查找特定版本
    DocumentVersion findByBelongsToDocumentAndVersion(Document document, String version);

    DocumentVersion findByFilename(String filename);

}
