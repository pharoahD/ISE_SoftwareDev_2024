package org.flitter.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
public class Document {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)

    private Long id;
    private String name;

    @ManyToOne
    @JoinColumn(name = "project_id")    // 为外键制定了一个列名
    private Project belongsToProject;

    @JsonIgnore
    @OneToMany(mappedBy = "belongsToDocument", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    // 这里mappedBy说明通过Document的belongsToDocument
    private List<DocumentVersion> versions;
}
