package org.flitter.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.flitter.backend.entity.enums.Role;

import java.util.Set;
import java.util.HashSet;


@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 登录账号
    @Column(nullable = false, unique = true)
    private String username;

    // 密码 加密存储
    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String email;

    // 使用enum类型的角色 用于基本权限
    @Enumerated(EnumType.STRING)    // 存储枚举名称
    @Column(nullable = false)
    private Role role;

    // 所属项目
    @ManyToMany(mappedBy = "participants")
    private Set<Project> projects = new HashSet<>();

    // 被指定的任务
    @ManyToMany(mappedBy = "assignees")
    private Set<Task> tasks = new HashSet<>();
}
