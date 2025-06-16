package com.example.taskflow.comment.entity;

import com.example.taskflow.common.BaseEntity;
import com.example.taskflow.todo.entity.;
import com.example.taskflow.user.entity.;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.scheduling.config.Task;
import org.springframework.security.core.userdetails.User;


import java.util.Optional;


@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "comments")
@Getter
@SQLDelete(sql = "UPDATE comments SET deleted = true WHERE comment_id = ?")
@SQLRestriction("deleted = false")
public class Comment extends BaseEntity{

    public Comment(Task task, User user, String content) {
        this.task = task;
        this.user = user;
        this.content = content;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "task_id")
    private Task task;

    public void updateComment(String content) {
        Optional.ofNullable(content).ifPresent(n -> this.content = n);
    }

}
