package org.flitter.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.flitter.backend.entity.enums.Priority;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "projects")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String projectName;

    @Column(nullable = false)
    private LocalDate startDate = LocalDate.now();  // 默认开始时间为当前时间

    @Column(nullable = false)
    private LocalDate endDate = LocalDate.now();

    @Column(nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)    // 存储枚举名称
    @Column(nullable = false)
    private Priority priority;

    @Column(nullable = false)
    private Double progress = 0.0;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Task> tasks = new HashSet<>();

    @Column(nullable = false)
    private Boolean isCompleted = false;   //工程是否完成

    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "project_person",
            joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "person_id")
    )
    private Set<User> participants = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "creator_id")
    private User creator;
}
