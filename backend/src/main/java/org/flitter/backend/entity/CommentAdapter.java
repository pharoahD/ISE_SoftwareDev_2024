package org.flitter.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.flitter.backend.entity.enums.CommentType;


@Data
@Entity
public class CommentAdapter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private CommentType type;

    // Nullable: when is a file
    private String comment;


//    @ManyToOne
//    taskid
//    lastmodifiedtiem
//
//
//    docid
//    filename

    // 返回的时候sort
}
