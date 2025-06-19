package com.example.taskflow.comment.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import com.example.taskflow.comment.entity.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

	List<Comment> findByDeletedFalse();

	List<Comment> findByTaskIdAndDeletedFalse(Long taskId);

	default Comment findByIdOrElseThrow(Long commentId) {
		return findById(commentId).orElseThrow(
			() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Does not exist id = " + commentId));
	}

}