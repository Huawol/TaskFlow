package com.example.taskflow.login;

import com.example.taskflow.common.User;
import com.example.taskflow.common.exception.LoginFailedException;
import com.example.taskflow.login.dto.LoginRequestDto;
import com.example.taskflow.login.dto.LoginResponseDto;
import com.example.taskflow.login.dto.SignupRequestDto;
import com.example.taskflow.login.dto.SignupResponseDto;
import com.example.taskflow.security.enums.UserRole;
import com.sun.jdi.request.DuplicateRequestException;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public SignupResponseDto signUp(SignupRequestDto signupRequestDto) {
        if (userRepository.existsByEmail(signupRequestDto.getEmail())) {
            throw new DuplicateRequestException("이미 존재하는 이메일입니다.");
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
        User user = userRepository.findByUserNameAndDeletedFalse(loginRequestDto.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 아이디입니다."));

        if (!passwordEncoder.matches(loginRequestDto.getPassword(), user.getPassword())) {
            throw new LoginFailedException("비밀번호가 일치하지 않습니다.");
        }
        return new LoginResponseDto(user);
    }

}
