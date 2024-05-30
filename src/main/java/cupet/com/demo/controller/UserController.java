package cupet.com.demo.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cupet.com.demo.dto.SignRequest;
import cupet.com.demo.dto.SignResponse;
import cupet.com.demo.model.JwtTokenUtil;
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
	public ResponseEntity<Boolean> signup(@RequestBody SignRequest request) throws Exception {
		return new ResponseEntity<>(signService.register(request), HttpStatus.OK);
	}

	@PostMapping(value = "/user/idcheck")
	public String idcheck(@RequestBody Map<String, String> requestBody) throws Exception {
		String id = requestBody.get("id");
		if (signService.getUserId(id)) {
			return "ok";
		} else {
			return "no";
		}
	}

}
