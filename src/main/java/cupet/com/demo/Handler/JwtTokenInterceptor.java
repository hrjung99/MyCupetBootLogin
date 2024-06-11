package cupet.com.demo.Handler;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import cupet.com.demo.MyCupetApplicationException;
import cupet.com.demo.MyCupetBootLoginApplication;
import cupet.com.demo.cupetenum.ErrorCode;
import cupet.com.demo.model.JwtTokenUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;


@Component
public class JwtTokenInterceptor implements HandlerInterceptor{

	
	 @Override
	    public boolean preHandle(
	            HttpServletRequest request,
	            @NonNull HttpServletResponse response,
	            @NonNull Object handler
	    ) {
		 
		 
		 if(request.getRequestURI().equals("/favicon.ico")) {
			 return true;
		 }
		 
		 String token = null;
		 
		 
		 Cookie[] cookies = request.getCookies();
		 if(cookies != null) {
			 for(Cookie cookie : cookies) {
				 if(cookie.getName().equals("jwt")) {
					 token = cookie.getValue();
					 break;
				 }
			 }
		 }
		 if(token != null) {
			 if(JwtTokenUtil.isValidToken(token)) {
				 String userid = JwtTokenUtil.getUserIdFromToken(token);
				 if(userid == null) {
					 throw new MyCupetApplicationException(ErrorCode.AUTH_TOKEN_NOT_MATCH);
				 }
				 return true;
			 }else {
				 throw new MyCupetApplicationException(ErrorCode.AUTH_TOKEN_INVALID);
			 }
		 }else {
			 throw new MyCupetApplicationException(ErrorCode.AUTH_TOKEN_IS_NULL);
		 }
		 
	 }
}
