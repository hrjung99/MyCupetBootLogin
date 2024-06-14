package cupet.com.demo.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cupet.com.demo.service.AuthService;
import cupet.com.demo.vo.User;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AuthController {

	private final AuthService authService;

	@GetMapping("/BootMain/auth-token/user")
	public Map<String, Object> AuthByUserFromMainBoot() {

		Map<String, Object> response = new HashMap<>();
		Authentication newauth = SecurityContextHolder.getContext().getAuthentication();
		System.out.println("인증컨트롤러 접근확인");
		String res = "";
		User user = null;
		System.out.println(newauth);
		if (newauth.getPrincipal() instanceof User) {
			user = (User) newauth.getPrincipal();
		} else {
			response.put("failuser", "fail");
			return response;
		}

		System.out.println(user);

		if (user != null) {
			res = user.getCupet_user_id();
		}
		if (res != "") {
			response = authService.GetUserparseToken2(user);
			System.out.println(res);
			return response;
		} else {
			response.put("failuser", "fail");
			return response;
		}
	}

}
