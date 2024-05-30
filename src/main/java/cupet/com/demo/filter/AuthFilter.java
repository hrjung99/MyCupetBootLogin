package cupet.com.demo.filter;

import java.io.IOException;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import cupet.com.demo.vo.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;



public class AuthFilter extends UsernamePasswordAuthenticationFilter {

	public AuthFilter(AuthenticationManager authenticationManager) {
		System.out.println("인증필터 생성자");
		this.setAuthenticationManager(authenticationManager);

	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {

		System.out.println("인증필터 접근");
		UsernamePasswordAuthenticationToken authRequest;
		try {
			authRequest = getAuthRequest(request);
			setDetails(request, authRequest);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return this.getAuthenticationManager().authenticate(authRequest);

	}

	private UsernamePasswordAuthenticationToken getAuthRequest(HttpServletRequest request) throws Exception {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.registerModule(new JavaTimeModule());
			objectMapper.configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, true);
			User user = objectMapper.readValue(request.getInputStream(), User.class);
			return new UsernamePasswordAuthenticationToken(user.getCupet_user_id(), user.getCupet_userpwd());
		} catch (UsernameNotFoundException e) {
			throw new UsernameNotFoundException(e.getMessage());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}