package com.example.taskflow.task.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.example.taskflow.task.entity.Status;
import com.example.taskflow.task.entity.Task;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

@Repository
public class TaskJpqlRepositoryImpl implements TaskJpqlRepository{

	@PersistenceContext
	private EntityManager em;

	public List<Task> searchTasksByJpql(String title, String content, String status) {
		//조건문을 추가할 기본 jpql
		String jpql = "select t from Task t";

		//조건문을 담을 리스트
		List<String> conditionList = new ArrayList<>();

		if(StringUtils.hasText(title)) {
			conditionList.add("t.title like concat('%', :title, '%')"); //와일드카드 포함
		}
		if(StringUtils.hasText(content)) {
			conditionList.add("t.content like concat('%', :content, '%')");
		}
		if(StringUtils.hasText(status)) {
			conditionList.add("t.status = :status");
		}

		//조건리스트가 하나라도 있다면 where + conditionList.get(0) + and + .get(1) ...
		if(!conditionList.isEmpty()) {
			jpql += " where " + String.join(" and ", conditionList);
		}

		//완성된 jpql 을 기반으로 쿼리 객체 생성
		TypedQuery<Task> query = em.createQuery(jpql, Task.class);

		//파라미터 바인딩
		if(StringUtils.hasText(title)) {
			query.setParameter("title", title); //와일드카드 포함
		}
		if(StringUtils.hasText(content)) {
			query.setParameter("content", content);
		}
		if(StringUtils.hasText(status)) {
			//이넘타입 변환 메서드 소문자도 가능
			Status fromStatus = Status.from(status);
			query.setParameter("status", fromStatus);
		}

		System.out.println("JPQL: " + jpql);
		//결과 리스트 반환
		return query.getResultList();
	}

}
