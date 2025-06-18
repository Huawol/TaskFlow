package com.example.taskflow.user.service;
import com.example.taskflow.security.PasswordEncoder;
import com.example.taskflow.common.exception.LoginFailedException;
import com.example.taskflow.security.enums.UserRole;
import com.example.taskflow.user.dto.LoginRequestDto;
import com.example.taskflow.user.dto.LoginResponseDto;
import com.example.taskflow.user.dto.SignupRequestDto;
import com.example.taskflow.user.dto.SignupResponseDto;
import com.example.taskflow.common.exception.UserNotFoundException;
import com.example.taskflow.user.entity.User;
import com.example.taskflow.user.repository.UserRepository;
import com.sun.jdi.request.DuplicateRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


//사용자 서비스 구현체
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;


	//회원탈퇴 처리
	//- DB에서 사용자 정보 조회
	//- 입력받은 비밀번호와 DB 비밀번호 비교
	@Override
	@Transactional
	public void deleteUser(String email, String password) {
		User user = userRepository.findByEmailAndDeletedFalse(email)
			.orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다."));

		if (!passwordEncoder.matches(password, user.getPassword())) {
			throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
		}

		user.softDelete();
	}


	@Override
	public SignupResponseDto signUp(SignupRequestDto signupRequestDto) {
		if (userRepository.existsByEmail(signupRequestDto.getEmail())) {
			throw new DuplicateRequestException("이미 존재하는 이메일입니다.");
		}

		/*
		userName 즉 일반적으로 사용되는 id 값도 중복불가
		*/
		if (userRepository.existsByUserName(signupRequestDto.getUserName())) {
			throw new DuplicateRequestException("이미 존재하는 유저명입니다.");
		}

		User user = new User(
			signupRequestDto.getEmail(),
			passwordEncoder.encode(signupRequestDto.getPassword()),
			signupRequestDto.getUserName(),
			signupRequestDto.getName()
		);
		user.setRole(UserRole.USER);
		User saveUser = userRepository.save(user);

		return new SignupResponseDto(saveUser);
	}

	@Override
	public LoginResponseDto login(LoginRequestDto loginRequestDto) {
		User user = userRepository.findByUserNameAndDeletedFalse(loginRequestDto.getUserName())
			.orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 아이디입니다."));

		if (!passwordEncoder.matches(loginRequestDto.getPassword(), user.getPassword())) {
			throw new LoginFailedException("비밀번호가 일치하지 않습니다.");
		}
		return new LoginResponseDto(user);
	}
}