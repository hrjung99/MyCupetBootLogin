package cupet.com.demo.service;

import java.security.Key;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import cupet.com.demo.mapper.UserMapper;
import cupet.com.demo.util.JwtProvider;
import cupet.com.demo.vo.LoginTokenVO;
import cupet.com.demo.vo.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
	
	final private UserMapper userMapper;

	@Value("${jwt.secret.key}")
	private String salt;

	private Key secretKey;

	public Map<String, Object>  GetUserparseToken(String token) {
		byte[] decodedKey = Base64.getDecoder().decode(salt);
		System.out.println("메인부트 인증 반환 처리 시작");
		System.out.println(token);
        secretKey = new SecretKeySpec(decodedKey, "HmacSHA256");
		Claims claims = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();
		System.out.println(claims);
		Map<String, Object> userDataMap = (Map<String, Object>) claims.get("userDataForm");
		return userDataMap;
	}
	
	public Map<String, Object>  GetUserparseToken2(User user) {
		System.out.println("메인부트 인증 반환 처리 시작");
		Map<String, Object> userDataMap = new HashMap<>();
		userDataMap.put("cupet_user_id",user.getCupet_user_id());
		userDataMap.put("cupet_user_name", user.getCupet_user_name());
		userDataMap.put("cupet_user_nickname", user.getCupet_user_nickname());
		userDataMap.put("cupet_user_address", user.getCupet_user_address());
		userDataMap.put("cupet_user_gender", user.getCupet_user_gender());
		userDataMap.put("cupet_user_phonenumber", user.getCupet_user_phonenumber());
		userDataMap.put("cupet_user_point", user.getCupet_user_point());
		
		userDataMap.put("cupet_user_principle", user.getCupet_user_principle());
		userDataMap.put("cupet_user_principle", user.getCupet_user_principle());
		userDataMap.put("cupet_user_birth", user.getCupet_user_birth());
		userDataMap.put("cupet_user_email", user.getCupet_user_email());
		System.out.println(userDataMap);
		return userDataMap;
	}

	
	/*
	 * 리프레시 토큰 갱신을 위하여 목록을 가져오는 메서드
	 * 
	 * */
	public List<LoginTokenVO> dbLoginTokenList() {
		return userMapper.dbLoginTokenList();
		
	}

	/*
	 * 리프레시 토큰 만료 삭제
	 * */
	public void deleteExpiredToken(String logintoken_idx) {
		
		System.out.println("리프레시 토큰 삭제 갱신 : " +logintoken_idx);
		userMapper.deleteExpiredToken(logintoken_idx);
		
	}

	/*
	 * 
	 * 리프레시 토큰이 두개 인지 확인 후 가장 마지막 로그인 제외 삭제
	 * 
	 * */
	public void refreshTokenClear(String id) {
		//가장 높은 만료시간을 가진 토큰 0번쨰 인덱스
		//이후로 오는 토큰은 삭제 진행
		List<LoginTokenVO> list = userMapper.dbLoginTokenListofLoginUser(id);
		for(int i = 1 ; i <list.size() ; i++) {
			LoginTokenVO item = list.get(i);
			userMapper.deleteExpiredToken(item.getLogintoken_idx());
		}
		
	}

	
	
}
