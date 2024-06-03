package cupet.com.demo.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import cupet.com.demo.dto.SignRequest;
import cupet.com.demo.dto.SignResponse;
import cupet.com.demo.mapper.UserMapper;
import cupet.com.demo.util.JwtProvider;
import cupet.com.demo.vo.User;
import cupet.com.demo.vo.UserAdressVO;
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
		if (res == null) {
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

	public boolean register(Map<String, String> requestBody) {
		System.out.println("회원가입 요청 시도");
		int res = 0;
		boolean flag=false;
		try {
			User member = User.builder().cupet_user_id(requestBody.get("id"))
					.cupet_userpwd(passwordEncoder.encode(requestBody.get("password")))
					.cupet_user_name(requestBody.get("name")).cupet_user_nickname(requestBody.get("nickname"))
					.cupet_user_address(requestBody.get("postcode")).cupet_user_gender(requestBody.get("gender"))
					.cupet_user_phonenumber(requestBody.get("phone")).cupet_user_birth(requestBody.get("birth"))
					.cupet_user_principle("cupet_user").cupet_user_point("0").build();
			res = userMapper.save(member);
			System.out.println(res);
			flag = true;
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		if (res == 1) {
			UserAdressVO userAdressVO = UserAdressVO.builder()
					.cupet_user_id(requestBody.get("id"))
					.roadAddress(requestBody.get("roadAddress"))
					.jibunAddress(requestBody.get("jibunAddress")).detailAddress(requestBody.get("detailAddress"))
					.locateX(requestBody.get("locateX")).locateY(requestBody.get("locateY")).build();
			
			
			userMapper.saveAddres(userAdressVO);
			
		}

		return flag;
	}

	public boolean getUserId(String id) {
		List<User> list = new ArrayList<>();
		list = userMapper.getUserId(id);
		System.out.println(list);
		if (list.isEmpty()) {
			return true;
		}
		return false;
	}

	public String redirectToken(String nwid) {
		User u = userMapper.login(User.builder().cupet_user_id(nwid).build());
		String token = jwtProvider.createToken(u);
		return token;
	}
}
