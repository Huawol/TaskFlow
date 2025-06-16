package com.example.taskflow.comment.entity;

import com.example.taskflow.common.BaseEntity;
import lombok.Getter;

@Entity
@Getter
public class Comment extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
