package org.flitter.backend.dto;

import jakarta.persistence.*;
import org.flitter.backend.entity.Project;
import org.flitter.backend.entity.Task;

public class DocumentDTO {
    private Long id;
    private String name;// 为外键制定了一个列名
    private Long belongsToProject;
    private Long belongsToTask;
}
