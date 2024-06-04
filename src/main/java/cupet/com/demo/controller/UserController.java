package cupet.com.demo.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cupet.com.demo.dto.SignRequest;
import cupet.com.demo.dto.SignResponse;
import cupet.com.demo.model.JwtTokenUtil;
import cupet.com.demo.service.AuthService;
import cupet.com.demo.service.SignService;
import cupet.com.demo.service.UserService;
import cupet.com.demo.util.LoginRequest;
import cupet.com.demo.vo.User;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api2")
@RequiredArgsConstructor
public class UserController {

	private final SignService signService;
	private final AuthService authService;

	@GetMapping("test")
	public void vuecome() {
		System.out.println("hello");
	}


	@PostMapping(value = "/user/login")
	public ResponseEntity<SignResponse> signin(@RequestParam("username") String username,
			@RequestParam("password") String password) throws Exception {
		SignRequest request = SignRequest.builder().id(username).password(password).build();
		return new ResponseEntity<>(signService.login(request), HttpStatus.OK);
	}

	@PostMapping(value = "/user/register")
	public Map<String, Object> signup(@RequestBody Map<String, String> requestBody) throws Exception {
		System.out.println(requestBody);
		Map<String, Object> res = new HashMap<>();
		if(signService.register(requestBody)) {
			res.put("result", "success");
			
		}else {
			res.put("result", "failed");
		}
		return res;
	}


	@PostMapping(value = "/user/idcheck")
	public String idcheck(@RequestBody Map<String, String> requestBody) throws Exception {
		String id = requestBody.get("id");
		if(id == "" || id.length() < 5) {
			return "donot";
		}
		if (signService.getUserId(id)) {
			return "ok";
		} else {
			return "no";
		}
	}
	
	@PostMapping(value="/user/redirectToken")
	public String redirectToken(@RequestHeader("Authorization") String token) {
		String res="";
		System.out.println("토큰 재생성 접근 확인");
		Map<String, Object> response = new HashMap<>();
		
		if (token.startsWith("Bearer")) {
			res = token.substring(7);
		}
		response = authService.GetUserparseToken(res);
		String nwid = (String)response.get("cupet_user_id");
		System.out.println( "redirect Token id : "+ nwid);
		String restoken = signService.redirectToken(nwid);
			
		return restoken;
	}
	
	

}
