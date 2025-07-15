package com.example.taskflow.task.repository;

import static com.example.taskflow.task.entity.QTask.*;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.example.taskflow.task.entity.Status;
import com.example.taskflow.task.entity.Task;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class TaskCustomRepositoryImpl implements TaskCustomRepository {

	private final JPAQueryFactory queryFactory;

	@Override
	public List<Task> searchTasksByQueryDsl(String title, String content, String status) {
		return queryFactory
			.selectFrom(task)
			.where(titleContains(title), contentContains(content), statusEq(status))
			.fetch();
	}

	private BooleanExpression titleContains(String title) {
		return title != null ? task.title.contains(title) : null;
	}

	private BooleanExpression contentContains(String content) {
		return content != null ? task.content.contains(content) : null;
	}

	private BooleanExpression statusEq(String status) {
		return status != null ? task.status.eq(Status.from(status)) : null;
	}

}