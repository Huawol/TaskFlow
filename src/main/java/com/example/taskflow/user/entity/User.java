package com.example.taskflow.user.entity;

import com.example.taskflow.common.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter // 테스트용
@Getter
@Table(name = "users")
// @NoArgsConstructor(access = AccessLevel.PROTECTED) //테스트를 위해 막아둠
public class User extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// @Column(nullable = false, unique = true)
	private String userName;

	// @Column(nullable = false)
	private String password;
}
