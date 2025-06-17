package com.example.taskflow.login;

import com.example.taskflow.common.SignUser;
import com.example.taskflow.common.exception.LoginFailedException;
import com.example.taskflow.login.dto.LoginRequestDto;
import com.example.taskflow.login.dto.LoginResponseDto;
import com.example.taskflow.login.dto.SignupRequestDto;
import com.example.taskflow.login.dto.SignupResponseDto;
import com.example.taskflow.security.PasswordEncoder;
import com.example.taskflow.security.enums.UserRole;
import com.sun.jdi.request.DuplicateRequestException;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class LoginServiceImpl implements LoginService {
    private final LoginRepository loginRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public SignupResponseDto signUp(SignupRequestDto signupRequestDto) {
        if (loginRepository.existsByEmail(signupRequestDto.getEmail())) {
            throw new DuplicateRequestException("이미 존재하는 이메일입니다.");
        }
        SignUser signUser = new SignUser(
                signupRequestDto.getEmail(),
                passwordEncoder.encode(signupRequestDto.getPassword()),
                signupRequestDto.getUserName(),
                signupRequestDto.getName()
        );
        signUser.setRole(UserRole.USER);
        SignUser saveSignUser = loginRepository.save(signUser);

        return new SignupResponseDto(saveSignUser);
    }

    @Override
    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
        SignUser signUser = loginRepository.findByUserNameAndDeletedFalse(loginRequestDto.getUserName())
                .orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 아이디입니다."));

        if (!passwordEncoder.matches(loginRequestDto.getPassword(), signUser.getPassword())) {
            throw new LoginFailedException("비밀번호가 일치하지 않습니다.");
        }
        return new LoginResponseDto(signUser);
    }

}
