package cupet.com.demo.service;

import java.util.Collections;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import cupet.com.demo.dto.SignRequest;
import cupet.com.demo.dto.SignResponse;
import cupet.com.demo.mapper.UserMapper;
import cupet.com.demo.util.JwtProvider;
import cupet.com.demo.vo.User;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SignService {

	private final UserMapper userMapper;
	private final PasswordEncoder passwordEncoder;
	private final JwtProvider jwtProvider;

	public SignResponse login(SignRequest request) throws Exception {
		System.out.println("로그인 요청 시도");
		User res = userMapper.login(User.builder().cupet_user_id(request.getId()).build());
		if(res == null) {
			throw new BadCredentialsException("잘못된 계정정보입니다.");
		}
		System.out.println(request.getPassword());
		System.out.println(res.getCupet_userpwd());
		System.out.println(passwordEncoder.encode(request.getPassword()));
		if (!passwordEncoder.matches(request.getPassword(), res.getCupet_userpwd())) {
			System.out.println("비밀번호불일치");
			throw new BadCredentialsException("잘못된 계정정보입니다.");
		}
		res.setCupet_userpwd("");

		return SignResponse.builder().id(res.getCupet_user_id()).name(res.getCupet_user_name())
				.nickname(res.getCupet_user_nickname()).token(jwtProvider.createToken(res)).build();
	}

	public boolean register(SignRequest request) {
		System.out.println("회원가입 요청 시도");
		try {
			User member = User.builder().cupet_user_id(request.getId())
					.cupet_userpwd(passwordEncoder.encode(request.getPassword()))
					.cupet_user_name(request.getName())
					.cupet_user_nickname(request.getNickname())
					.build();
			userMapper.save(member);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return true;
	}
}
