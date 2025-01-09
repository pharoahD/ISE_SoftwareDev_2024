package org.flitter.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
@Entity
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @ManyToOne
    @JoinColumn(name = "project_id")    // 为外键制定了一个列名
    private Project belongsToProject;

    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task belongsToTask;

    @JsonIgnore
    @OneToMany(mappedBy = "belongsToDocument", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    // 这里mappedBy说明通过Document的belongsToDocument
    private List<DocumentVersion> versions;

    @Column(name = "shared_user_names")
    private String sharedUserNames;

    // 获取共享用户名称列表
    public List<String> getSharedUserNamesList() {
        return new ArrayList<>(Arrays.asList(this.sharedUserNames.split(",")));
    }

    // 设置共享用户名称列表
    public void setSharedUserNamesList(List<String> sharedUserNamesList) {
        this.sharedUserNames = String.join(",", sharedUserNamesList);
    }
}