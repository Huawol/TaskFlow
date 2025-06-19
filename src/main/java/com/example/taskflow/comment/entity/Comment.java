package com.example.taskflow.comment.entity;

import java.util.Optional;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import com.example.taskflow.common.BaseEntity;
import com.example.taskflow.task.entity.Task;
import com.example.taskflow.user.entity.User;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "comments")
@Getter
@SQLDelete(sql = "UPDATE comments SET deleted = true WHERE comment_id = ?")
@SQLRestriction("deleted = false")
public class Comment extends BaseEntity {

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