package cupet.com.demo.util;

import java.util.Random;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import cupet.com.demo.mapper.UserMapper;
import cupet.com.demo.vo.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class EmailUtil {
	
	private final JavaMailSender javaMailSender;
	private final UserMapper userMapper;
	
	public String createNumber() {
        int length = 6; 
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random rand = new Random();
        StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int index = rand.nextInt(characters.length());
            sb.append(characters.charAt(index));
        }

        return sb.toString();
    }
	
	public MimeMessage createEmailForm(String email, String randomNumber) throws MessagingException {
        String subject = "인증번호 발송";
        String content = "귀하의 인증번호는 다음과 같습니다: " + randomNumber;

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");
        System.out.println("form:"+email);
        helper.setTo(email);
        helper.setSubject(subject);
        helper.setText(content, true); // HTML 설정 가능, 여기서는 단순 텍스트

        return message;
    }

	public MimeMessage createEmailFormtoUserID(String email) throws MessagingException {
		User user = userMapper.getUserIdforEmail(email);
        String subject = "[MyCupet] 귀하의 아이디는";
        String content = "다음과 같습니다: " + user.getCupet_user_id();
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");
        helper.setTo(email);
        helper.setSubject(subject);
        helper.setText(content, true);

		return message;
	}

}
