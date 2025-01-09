package org.flitter.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDate;

@Data
@ToString
@AllArgsConstructor
public class TaskForGanttDTO {
    private Long id;               //任务的id
    private String Title;          //任务名称
    private LocalDate StartDate;   //任务开始时间
    private LocalDate EndDate;     //任务结束时间
    private boolean isCompleted;   //任务是否结束
}
