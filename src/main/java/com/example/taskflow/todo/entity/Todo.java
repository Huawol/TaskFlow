package com.example.taskflow.todo.entity;

import java.time.LocalDate;

import com.example.taskflow.common.BaseEntity;
import com.example.taskflow.user.entity.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@Table(name="todos")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Todo extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "created_by_id")
	private User createdBy;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "assigned_to_id")
	private User assignedTo;

	@Column(nullable = false)
	private String title;

	@Column(nullable = false)
	private String content;

	@Column
	private LocalDate deadLine;

	@Enumerated(EnumType.STRING)
	private Status status = Status.TODO;

	@Enumerated(EnumType.STRING)
	private Priority priority = Priority.MEDIUM;

}
