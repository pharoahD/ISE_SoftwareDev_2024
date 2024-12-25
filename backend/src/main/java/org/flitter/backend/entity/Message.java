package org.flitter.backend.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@Table(name="messages")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String message;  //具体消息内容

    // JoinColumn不用加 Column
    @ManyToOne
    @JoinColumn(name="author_id", nullable = false)
    private User author; //发送方

    @Column(nullable = false)
    private LocalDate date;  //发送日期

    @ManyToOne
    @JoinColumn(name= "receiver_id", nullable = false)
    private User receiver;  //接收方

    @Column(nullable = false)
    private String readed;
}
