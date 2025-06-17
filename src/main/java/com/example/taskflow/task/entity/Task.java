package com.example.taskflow.task.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name="tasks")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Task extends BaseEntity {

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
	private LocalDate startDate;

	@Column
	private LocalDateTime deletedAt;

	@Column
	private LocalDate deadline;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private Status status = Status.TODO;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private Priority priority = Priority.LOW;

	//new 키워드 접근 제한
	private Task(User createdBy, User assignedTo, String title, String content, LocalDate deadline, String priority) {
		this.createdBy = createdBy;
		this.assignedTo = assignedTo;
		this.title = title;
		this.content = content;
		this.deadline = deadline;
		this.priority = Priority.from(priority);
	}

	//생성 팩토리 메서드
	public static Task create(User createdBy, User assignedTo, String title, String content, LocalDate deadline, String priority) {
		return new Task(createdBy, assignedTo, title, content, deadline, priority);
	}

	//업데이트 메서드
	public void updateTaskFrom(User assignedTo, String title, String content, LocalDate deadLine, String priority) {
		this.assignedTo = assignedTo;
		this.title = title;
		this.content = content;
		this.deadline = deadLine;
		this.priority = Priority.from(priority);
	}

	//상태 변경 메서드
	public void changeStatus(String value) {
		this.status = Status.from(value);
	}

	//시작 날짜 세팅 메서드
	public void setStartDate() {
		this.startDate = LocalDate.now();
	}

	//시작 날짜 세팅 메서드
	public void setDeletedAt() {
		this.deletedAt = LocalDateTime.now();
	}

}
