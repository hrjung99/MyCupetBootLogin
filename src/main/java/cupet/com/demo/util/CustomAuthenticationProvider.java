package cupet.com.demo.util;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import cupet.com.demo.service.UserService;
import cupet.com.demo.vo.User;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

	private final UserService userService;

	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		
		System.out.println("token auth provider start");
		UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;
		String loginId = token.getName();
		String userPassword = (String) token.getCredentials();

		User userDto = (User) userService.loadUserByUsername(loginId);
		//로그인시 사용자 DB 비밀번호 검증과정
		if (!bCryptPasswordEncoder().matches(userPassword, userDto.getCupet_userpwd())) {
			throw new BadCredentialsException(userDto.getUsername() + "Invalid password");
        }
		UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
				userDto, userPassword,userDto.getAuthorities());
		return authToken;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		// TODO Auto-generated method stub
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}

}
