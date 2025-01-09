package org.flitter.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.flitter.backend.entity.enums.CommentType;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Data
@Entity
@Table(name="comment")
public class CommentAdapter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private CommentType type;

    private String stringComment;

    private Long fileId;

    private String filename;

    @ManyToOne
    @JoinColumn(name = "publisher_id")
    private User publisher;

    @Column(unique = false)
    private LocalDateTime datetime; //评价发表日期

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="task_id")
    private Task belongedTask;
}
