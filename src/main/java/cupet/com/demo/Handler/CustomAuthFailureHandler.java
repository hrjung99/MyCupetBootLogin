package cupet.com.demo.Handler;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import org.json.JSONObject;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
@Component
public class CustomAuthFailureHandler implements AuthenticationFailureHandler {

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {

		JSONObject jsonObject = new JSONObject();
		String failMessage  = "";
		
		if (exception instanceof AuthenticationServiceException) {
            failMessage = "로그인 정보가 일치하지 않습니다.";
        } else if (exception instanceof LockedException) {
            failMessage = "계정이 잠겨 있습니다.";
        } else if (exception instanceof DisabledException) {
            failMessage = "계정이 비활성화되었습니다.";
        } else if (exception instanceof AccountExpiredException) {
            failMessage = "계정이 만료되었습니다.";
        } else if (exception instanceof CredentialsExpiredException) {
            failMessage = "인증 정보가 만료되었습니다.";
        }
		
		response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        
        try (PrintWriter printWriter = response.getWriter()) {
            System.out.println(failMessage);

            HashMap<String, Object> resultMap = new HashMap<>();
            resultMap.put("userInfo", null);
            resultMap.put("resultCode", 9999);
            resultMap.put("failMessage", failMessage);
            jsonObject = new JSONObject(resultMap);

            printWriter.print(jsonObject);
            printWriter.flush();
        }
	}

}
