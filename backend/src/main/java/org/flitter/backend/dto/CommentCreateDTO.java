package org.flitter.backend.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import org.flitter.backend.entity.Document;
import org.flitter.backend.entity.Task;
import org.flitter.backend.entity.User;
import org.flitter.backend.entity.enums.CommentType;

import java.time.LocalDateTime;

@AllArgsConstructor
@Data
@ToString
public class CommentCreateDTO {
    private String stringComment;
    private Document documentComment;
    private User publisher;
    private LocalDateTime datetime; //评价发表日期
    private Task belongedTask;
}
