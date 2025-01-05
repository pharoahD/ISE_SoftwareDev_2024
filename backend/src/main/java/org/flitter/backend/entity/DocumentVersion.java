package org.flitter.backend.entity;


import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class DocumentVersion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String version;
    private String filename;
    private Long fileSize;
    private String fileType;
    private LocalDateTime uploadTime;

    @Lob
    @Column(columnDefinition = "BLOB")
    private byte[] fileData;

    @ManyToOne
    @JoinColumn(name = "document_id")
    private Document belongsToDocument;
}
