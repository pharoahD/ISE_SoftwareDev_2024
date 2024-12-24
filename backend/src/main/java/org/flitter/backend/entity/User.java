package org.flitter.backend.entity;

import jakarta.persistence.*;
import lombok.Data;



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

    // 使用enum类型的角色 用于基本权限
    @Enumerated(EnumType.STRING)    // 存储枚举名称
    @Column(nullable = false)
    private Role role;
}
