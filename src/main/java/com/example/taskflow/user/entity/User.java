package com.example.taskflow.user.entity;

import com.example.taskflow.common.BaseEntity;
import com.example.taskflow.security.enums.UserRole;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@Entity
@Setter
@Table(name = "users")
public class User extends BaseEntity {  //BaseEntity 상속 추가

	@Id
	@GeneratedValue
	@Column(name = "user_id")
	private Long id;

	private String email;

	private String password;

	private String userName;

	private String name;

	@Enumerated(EnumType.STRING)
	private UserRole role;

	public User(String email, String password, String userName, String name) {
		this.email = email;
		this.password = password;
		this.userName = userName;
		this.name = name;
	}

}