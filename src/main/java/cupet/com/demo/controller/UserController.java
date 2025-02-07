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
import cupet.com.demo.service.EmailService;
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
	private final EmailService emailService;
	
	
	private final UserService userService;

	@GetMapping("test")
	public void vuecome() {
		System.out.println("hello");
	}
	
	@PostMapping(value = "/user/tokenexpcheck")
	public String tokenexpcheck(@RequestHeader("Authorization") String token) {
		System.out.println("토큰채커 발생");
		
		
		return "";
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
		if (signService.register(requestBody)) {
			res.put("result", "success");
			res.put("cupet_user_id", requestBody.get("id"));

		} else {
			res.put("result", "failed");
		}
		return res;
	}
	
	
	
	/*
	 * 
	 * 아이디 확인 절차
	 * 
	 * */
	@PostMapping(value = "/user/sendidrecoveryemail")
	public String sendidrecoveryemail(@RequestBody Map<String,String> req) {
		String email = req.get("email");
		if(emailService.sendEmailtoUserID(email)) {
			return "success";
		}else {
			return "failed";
		}
		
		
	}
	
	
	/*
	 * 
	 * 비밀번호 확인 절차
	 * 
	 * 
	 * */
	@PostMapping(value = "/user/sendpasswordrecoveryemail")
	public String sendpasswordrecoveryemail(@RequestBody Map<String,String> req) {
		String email = req.get("email");
		String id = req.get("id");
		System.out.println(email);
		System.out.println(id);
		//올바른 아이디 입력에 대한 확인 절차
		int checker =userService.findId(id,email);
		System.out.println(checker);
		if(checker == 0) {
			return "noid";
		}
		if(checker == 1) {
			return "nomatchemail"; 
		}
		if(checker == -1) {
			emailService.sendEmail(email);
			return "success";
		}
		
		return "failed";
		
	}
	
	

	@PostMapping(value = "/user/idcheck")
	public String idcheck(@RequestBody Map<String, String> requestBody) throws Exception {
		String id = requestBody.get("id");
		if (id == "" || id.length() < 5) {
			return "donot";
		}
		if (signService.getUserId(id)) {
			return "ok";
		} else {
			return "no";
		}
	}

	
	
	@PostMapping(value = "/user/emailcheck")
	public String sendEmailVerify(@RequestBody Map<String, String> req) {
		String email = req.get("email");
		System.out.println("이메일 인증 : " + email);
		
		if(!signService.emailVaildCheck(email)) {
			return "vaild";
		}
		
		if (emailService.sendEmail(email)) {
			return "success";
		} else {
			return "failed";
		}
	}
	
	/*
	 * 
	 * 비밀번호를 재설정 해주는 메서드
	 * 
	 */
	@PostMapping(value="/user/updatePassword")
	public String updatePassword(@RequestBody Map<String, String> req) {
		String email = req.get("email");
		String pwd = req.get("newPassword");
		System.out.println("이메일 : " + email);
		System.out.println("새로운 패스워드 : " + pwd);
		int res = signService.resetPasswordUser(email,pwd);
		if(res != 0) {
			return "success";
		}else {
			return "failed";
		}
	}
		
	@PostMapping(value = "/user/verifyCode")
	public String verifyEmail(@RequestBody Map<String, String> req) {
		String email = req.get("email");
		String code = req.get("code");
		System.out.println(email);
		System.out.println(code);
		if (emailService.verifyCode(email, code)) {
			return "success";
		} else {
			return "failed";
		}
	}

	@PostMapping(value = "/user/redirectToken")
	public String redirectToken(@RequestHeader("Authorization") String token) {
		String res = "";
		System.out.println("토큰 재생성 접근 확인");
		Map<String, Object> response = new HashMap<>();

		if (token.startsWith("Bearer")) {
			res = token.substring(7);
		}
		response = authService.GetUserparseToken(res);
		String nwid = (String) response.get("cupet_user_id");
		System.out.println("redirect Token id : " + nwid);
		String restoken = signService.redirectToken(nwid);
   
		return restoken;
	}

}
