package org.flitter.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import org.flitter.backend.entity.Project;
import org.flitter.backend.entity.User;

import java.util.HashSet;
import java.util.Set;

@Data
@ToString
@AllArgsConstructor
public class TaskAssigneeDTO {
    private Long id;
    private Project belongedProject;
    private Set<User> assignees = new HashSet<>();
}
