package com.example.taskflow.comment.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.taskflow.comment.entity.Comment;

//테스트용 임시 리포지터리
public interface CommentRepository extends JpaRepository<Comment, Long> {

	List<Comment> findByTaskIdAndDeletedFalse(Long taskId);
}
