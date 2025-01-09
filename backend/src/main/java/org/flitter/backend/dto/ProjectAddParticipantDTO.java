package org.flitter.backend.dto;

import lombok.Data;

import java.util.List;

@Data
public class ProjectAddParticipantDTO {
    private Long projectId;
    private List<Long> userIds;
}
