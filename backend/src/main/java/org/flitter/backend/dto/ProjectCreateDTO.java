package org.flitter.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import org.flitter.backend.entity.enums.Priority;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@ToString
public class ProjectCreateDTO {
    private Long id;
    private String name;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private Priority priority;
    private Long managerId;
}
