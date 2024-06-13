package cupet.com.demo.filter;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import cupet.com.demo.util.JwtProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtAuthenticationFilter extends OncePerRequestFilter {
	private final JwtProvider jwtProvider;

	public JwtAuthenticationFilter(JwtProvider jwtProvider) {
		this.jwtProvider = jwtProvider;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String token = jwtProvider.resolveToken(request);
		System.out.println("jwt 필터 접근");
		System.out.println(token);
		System.out.println(request.getRequestURI());
		
		
//		String requestURI = request.getRequestURI();
//		if (requestURI.startsWith("/BootMain")) {
//	        // 필터링을 건너뛰고 바로 요청 처리
//	        filterChain.doFilter(request, response);
//	        return;
//	    }
//		
		

		if (token != null) {
			System.out.println("토큰 존재");
			token = token.split(" ")[1].trim();
			System.out.println(token);
	
			
			if(!jwtProvider.validateToken(token)) {
				System.out.println("만료 토큰에 대한 refresh 토큰확인 절차를 시행합니다.");
				if(jwtProvider.vlidateRefreshToken(token)) {
					//리프레스 토큰에 대한 것이 유효
					System.out.println("만료 토큰에 대한 refresh 토큰 재발급 절차를 시작합니다.");
					//토큰 재생성
			
					String newToken = jwtProvider.RefreshcreateToken(token);
					
					Authentication newauth = jwtProvider.getAuthentication(newToken);
					System.out.println(" 리프레시 토큰을 통한 재발급 + 헤더에 추가");
				    response.setHeader("Authorization","Bearer "+ newToken);
				    System.out.println("전 : " +request.getHeader("Authorization"));
				    System.out.println("후 : " +response.getHeader("Authorization"));
				    SecurityContextHolder.getContext().setAuthentication(newauth);
				}else {
					response.setHeader("jwtres", "failed");
					filterChain.doFilter(request, response);
					return;
				}
			}else {
				response.setHeader("jwtres", "success");
				Authentication auth = jwtProvider.getAuthentication(token);
				SecurityContextHolder.getContext().setAuthentication(auth);
			}
		}
		
		filterChain.doFilter(request, response);

	}

}
