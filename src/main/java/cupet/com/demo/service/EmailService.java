package cupet.com.demo.service;

import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import cupet.com.demo.util.EmailUtil;
import cupet.com.demo.vo.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService {

	
	private final JavaMailSender javaMailSender;
	private final EmailUtil emailUtil;
	private final RedisTemplate<String, Object> redisTemplate;

	
	public boolean sendEmail(String email) {
		System.out.println("이메일 발송 프로세스");
		try {
			String rand = emailUtil.createNumber();
			//redis 에서 인증번호 6분동안 유효
			System.out.println(email);
			System.out.println("redis 진입");
			
			redisTemplate.opsForValue().set(email, rand,3,TimeUnit.MINUTES);
			System.out.println("redis 탈출");
			
			MimeMessage message = emailUtil.createEmailForm(email, rand);
			javaMailSender.send(message);
			System.out.println("이메일 인증 발송");
			return true;
		
		}catch(MessagingException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	//이메일 검증
	public boolean verifyCode(String email, String code) {
		String storedCode = (String) redisTemplate.opsForValue().get(email);
		System.out.println(storedCode);
		return storedCode != null && storedCode.equals(code);
	}

	//아이디 찾기 서비스
	public boolean sendEmailtoUserID(String email) {
		try {
		MimeMessage message = emailUtil.createEmailFormtoUserID(email);
		javaMailSender.send(message);
		return true;
		}catch(MessagingException e) {
			e.printStackTrace();
			return false;
		}
	}

	
	
}
