package org.flitter.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "task")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "project_id")
    private Project belongedProject;

    @ManyToOne
    @JoinColumn(name = "publisher_id")
    private User publisher;

    @ManyToMany
    @JsonIgnore
    @JoinTable(
            name = "task_person",
            joinColumns = @JoinColumn(name = "task_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> assignees = new HashSet<>();

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Column(nullable = false)
    private Boolean isCompleted; //项目是否完成
    
    @Column(nullable = false)
    private Double percentCompleted; //完成程度

//    @OneToMany(map
    // TODO:  相关的commentdapter

}
