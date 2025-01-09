package org.flitter.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;


@Data
@Entity
@Table(name = "presend_message")
public class PreSendingMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "task_id")
    private Long taskId;

    @Column(name = "user_ids")
    private String userIds; // 逗号分隔的用户 ID

    @Column(name = "time")
    private LocalDate time; // 截止时间

    @Column(name = "created_at")
    private LocalDate createdAt; // 创建时间
}

