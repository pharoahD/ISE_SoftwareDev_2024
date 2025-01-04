package org.flitter.backend.dto;

import lombok.Data;
import org.flitter.backend.entity.User;
import org.flitter.backend.entity.enums.Priority;

import java.time.LocalDate;

@Data
public class ProjectListDTO {
    private Long id;
    private String projectName;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private Priority priority;
    private Double progress;
    private Boolean isCompleted;
    private User creator;

    public ProjectListDTO(Long id, String projectName, String description, LocalDate startDate, LocalDate endDate,
                          Priority priority, Double progress, Boolean isCompleted, User creator) {
        this.id = id;
        this.projectName = projectName;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.priority = priority;
        this.progress = progress;
        this.isCompleted = isCompleted;
        this.creator = creator;
    }

    @Override
    public String toString() {
        return "ProjectListDTO{" +
                "id=" + id +
                ", projectName='" + projectName + '\'' +
                ", description='" + description + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", priority=" + priority +
                ", progress=" + progress +
                ", isCompleted=" + isCompleted +
                '}';
    }
}
