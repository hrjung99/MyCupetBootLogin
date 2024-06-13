package cupet.com.demo.scheduleder;

import java.util.Date;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import cupet.com.demo.service.AuthService;
import cupet.com.demo.vo.LoginTokenVO;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TokenScheduling {

	final private AuthService authService;
	
	@Scheduled(fixedRate = 60000)
	public void checkTokenRefresh() {
		System.out.println("리프레시 토큰 갱신");
		Date now = new Date();
		//객체를 처음만 생성 하도록 서버 데이터가 커지면 객체마다 now를 비교 하는걸로 수정
		List<LoginTokenVO> list = authService.dbLoginTokenList();
		for(LoginTokenVO item : list) {
			if(!item.aftercheckedExpiration_date(now)) {
				authService.deleteExpiredToken(item.getLogintoken_idx());
			}
		}
	}
}
