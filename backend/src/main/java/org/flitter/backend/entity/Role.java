package org.flitter.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.flitter.backend.entity.enums.Permission;

import java.util.Set;

@Data
@Entity
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    @ElementCollection(targetClass = Permission.class,
            fetch = FetchType.EAGER)
    @CollectionTable(name = "role_permission",
            joinColumns = @JoinColumn(name = "role_id"))
    @Enumerated(EnumType.STRING)
    @Column(name="permission")
    private Set<Permission> permissions;
}
