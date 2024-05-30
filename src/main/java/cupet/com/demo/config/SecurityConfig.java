package cupet.com.demo.config;

import java.io.IOException;
import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import cupet.com.demo.filter.JwtAuthenticationFilter;
import cupet.com.demo.util.JwtProvider;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	
	private final JwtProvider jwtProvider;

	// doc =
	// https://docs.spring.io/spring-security/reference/5.8/migration/servlet/config.html
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		System.out.println("접근감지");
		http.httpBasic(hp -> hp.disable()).csrf(csrf -> csrf.disable())
				.cors(cors -> cors.configurationSource(request -> {
					CorsConfiguration config = new CorsConfiguration();
					config.applyPermitDefaultValues();
					return config;
				}))
				.authorizeRequests(ar->ar
						.requestMatchers("/BootMain/**","/api2/**").permitAll()
						.anyRequest().denyAll())				
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.addFilterBefore(new JwtAuthenticationFilter(jwtProvider), UsernamePasswordAuthenticationFilter.class)
				.exceptionHandling(e -> e.accessDeniedHandler(new AccessDeniedHandler() {

					
					
					@Override
					public void handle(HttpServletRequest request, HttpServletResponse response,
							AccessDeniedException accessDeniedException) throws IOException, ServletException {
						response.setStatus(403);
						response.setCharacterEncoding("utf-8");
						response.setContentType("text/html; charset=UTF-8");
						response.getWriter().write("권한이 없는 사용자입니다.");

					}
				}).authenticationEntryPoint(new AuthenticationEntryPoint() {

					@Override
					public void commence(HttpServletRequest request, HttpServletResponse response,
							AuthenticationException authException) throws IOException, ServletException {
						response.setStatus(401);
						response.setCharacterEncoding("utf-8");
						response.setContentType("text/html; charset=UTF-8");
						response.getWriter().write("인증되지 않은 사용자입니다.");

					}
				}));

		return http.build();
	}

	
}
