package org.flitter.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import org.flitter.backend.entity.enums.Role;

@AllArgsConstructor
@ToString
@Data
public class UserIDRoleDTO {
    private Long id;
    private String username;
    private Role role;
}
