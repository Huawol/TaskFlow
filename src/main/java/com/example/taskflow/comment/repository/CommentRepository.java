package com.example.taskflow.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.taskflow.comment.entity.Comment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;


@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
	default Comment findByIdOrElseThrow(Long commentId) {
		return findById(commentId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Does not exist id = " + commentId));
	}

}
