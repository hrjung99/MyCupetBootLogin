package cupet.com.demo.Handler;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CustomPasswordEncoder implements PasswordEncoder{

	@Override
	public String encode(CharSequence rawPassword) {
		// BCryptPasswordEncoder를 이용하여 비밀번호를 암호화
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		return encoder.encode(rawPassword);
	}
	

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        // BCryptPasswordEncoder를 이용하여 암호화된 비밀번호를 확인
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.matches(rawPassword, encodedPassword);
    }
}
