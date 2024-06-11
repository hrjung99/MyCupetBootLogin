package cupet.com.demo.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.core.Authentication;

import cupet.com.demo.MyCupetApplicationException;
import cupet.com.demo.cupetenum.ErrorCode;
import cupet.com.demo.model.JwtTokenUtil;
import cupet.com.demo.service.UserService;
import cupet.com.demo.vo.User;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

	private final UserService userService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		
		System.out.println("jwt필터 접근");
		String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

		// 토큰 필요없는 url 은 바로 넘기기 위해서 등록, 로그인 시 토큰 검증 필요 x
		List<String> list = Arrays.asList("/aa");
		System.out.println(request.getRequestURI());
		if (list.contains(request.getRequestURI())) {
	
			filterChain.doFilter(request, response);
			System.out.println("검증 필요 없음");
			return;
		}

		
		// 쿠키를 확인하는 인증 요청 수행
		Cookie[] cookies = request.getCookies();
		String token = null;
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if ("jwt".equals(cookie.getName())) {
					token = cookie.getValue();
					break;
				}
			}
		}

		if (token != null && !token.equalsIgnoreCase("")) {
			System.out.println("쿠키 존재 확인, 쿠키 인증 진행");
			try {
				if (JwtTokenUtil.isValidToken(token)) {
					// 사용자 아이디 받기
					String loginid = JwtTokenUtil.getUserIdFromToken(token);

					if (loginid != null && !loginid.equalsIgnoreCase("")) {
						User user = (User) userService.loadUserByUsername(loginid);
						UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
								user, null, user.getAuthorities());

						SecurityContextHolder.getContext().setAuthentication(authenticationToken);

						filterChain.doFilter(request, response);

					} else {
						throw new MyCupetApplicationException(ErrorCode.AUTH_TOKEN_NOT_MATCH);
					}
				} else {
					throw new MyCupetApplicationException(ErrorCode.TOKEN_NOT_FOUND);
				}
			} catch (Exception e) {

				String logMessage = jsonResponseWrapper(e).getString("message");
				// 클라이언트에게 전송할 고정된 메시지
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				response.setCharacterEncoding("UTF-8");
				response.setContentType("application/json");

				PrintWriter printWriter = response.getWriter();
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("error", true);
				jsonObject.put("message", "로그인 에러");

				printWriter.print(jsonObject);
				printWriter.flush();
				printWriter.close();
			}
		}
		System.out.println("jwt 필터 탈출");
	}

	private JSONObject jsonResponseWrapper(Exception e) {

		String resultMessage = "";
		// JWT 토큰 만료
		if (e instanceof ExpiredJwtException) {
			resultMessage = "TOKEN Expired";
		}
		// JWT 허용된 토큰이 아님
		else if (e instanceof SignatureException) {
			resultMessage = "TOKEN SignatureException Login";
		}
		// JWT 토큰내에서 오류 발생 시
		else if (e instanceof JwtException) {
			resultMessage = "TOKEN Parsing JwtException";
		}
		// 이외 JTW 토큰내에서 오류 발생
		else {
			resultMessage = "OTHER TOKEN ERROR";
		}

		HashMap<String, Object> jsonMap = new HashMap<>();
		jsonMap.put("status", 401);
		jsonMap.put("code", "9999");
		jsonMap.put("message", resultMessage);
		jsonMap.put("reason", e.getMessage());
		JSONObject jsonObject = new JSONObject(jsonMap);
		return jsonObject;
	}

}