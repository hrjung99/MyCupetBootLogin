package cupet.com.demo.Handler;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import org.json.JSONObject;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import cupet.com.demo.model.JwtTokenUtil;
import cupet.com.demo.vo.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;


@Component
@RequiredArgsConstructor
public class CustomAuthSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws ServletException, IOException {
		System.out.println("Succes Handler start ...");

		User user = (User) authentication.getPrincipal();

		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		JSONObject userObject = new JSONObject(objectMapper.writeValueAsString(user));

		HashMap<String, Object> responseMap = new HashMap<>();
		JSONObject jsonObject;

		// 인증 성공한 토큰을 json으로 반환
		responseMap.put("UserInfo", userObject);
		jsonObject = new JSONObject(responseMap);

		String token = JwtTokenUtil.createToken(user);
		jsonObject.put("token", token);

		// 토큰을 쿠키에 저장
		Cookie jwtCookie = new Cookie("jwt", token);
		jwtCookie.setPath("/");
		response.addCookie(jwtCookie);

		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json");
		try(PrintWriter printWriter = response.getWriter()){
			printWriter.print(jsonObject);
			printWriter.flush();
		}
	}

}
