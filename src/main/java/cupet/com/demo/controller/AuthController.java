package cupet.com.demo.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cupet.com.demo.service.AuthService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/BootMain")
@RequiredArgsConstructor
public class AuthController {

	private final AuthService authService;

	//test?
	@GetMapping("/auth-token/user")
	public Map<String, Object> AuthByUserFromMainBoot(@RequestHeader("Authorization") String authorizationHeader) {
		System.out.println("인증컨트롤러 접근확인");
		String res = "";
		System.out.println(authorizationHeader);
		Map<String, Object> response = new HashMap<>();
		if (authorizationHeader.startsWith("Bearer")) {
			res = authorizationHeader.substring(7);
		}
		if (res != "") {
			response = authService.GetUserparseToken(res);
			System.out.println(res);
			return response;
		} else {
			response.put("failuser","fail");
			return response;
		}
	}

}
