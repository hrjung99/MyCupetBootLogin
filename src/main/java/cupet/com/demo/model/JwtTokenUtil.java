package cupet.com.demo.model;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Duration;
import java.time.Instant;
import java.util.*;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Component;

import cupet.com.demo.vo.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenUtil {

	private static final String jwtSecretKey = "thisIsASecretKeyUsedForJwtTokenGenerationAndItIsLongEnoughToMeetTheRequirementOf256Bits";
	// 인증용 시크릿 키 발급 <- 알고리즘은 HmacSHA256 적용
	private static final Key key = new SecretKeySpec(Base64.getDecoder().decode(jwtSecretKey),
			SignatureAlgorithm.HS256.getJcaName());
	private static final String JWT_TYPE = "JWT";
	private static final String ALGORITHM = "HS256";
	private static final String LOGIN_ID = "userID";
	private static final String USERNAME = "userRealname";

	public static String createToken(User usertoken) {

		JwtBuilder builder = Jwts.builder().setHeader(createHeader()).setClaims(createClaims(usertoken))
				.setSubject(String.valueOf(usertoken.getCupet_user_id())).setIssuer("mycupetIssuer")
				.setExpiration(createExpiredDate()).signWith(key);

		return builder.compact();
	}

	// Header생성
	private static Map<String, Object> createHeader() {
		Map<String, Object> header = new HashMap<>();
		header.put("teamname", "KosaCupetTeam");
		header.put("typ", JWT_TYPE);
		header.put("alg", ALGORITHM);
		header.put("regDate", System.currentTimeMillis());
		return header;
	}

	private static Map<String, Object> createClaims(User userDto) {
		// 공개 클래임에 사용자의 이름과 이메일을 설정해서 정보를 조회할 수 있다.
		Map<String, Object> claims = new HashMap<>();

		System.out.println("loginId : " + userDto.getCupet_user_id());
		System.out.println("username : " + userDto.getCupet_user_name());

		claims.put(LOGIN_ID, userDto.getCupet_user_id());
		claims.put(USERNAME, userDto.getCupet_user_name());
		claims.put("USERNICKNAME", userDto.getCupet_user_nickname());
		return claims;
	}

	private static Date createExpiredDate() {
		// 만료 2시간 지정
		Instant now = Instant.now();
		Instant expiryDate = now.plus(Duration.ofHours(2));
		return Date.from(expiryDate);
	}

	// claim 토큰 정보 반환
	private static Claims getClaimsFormToken(String token) {
		return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
	}

	// 토큰인증정보반환
	public static boolean isValidToken(String token) {
		try {
			Claims claims = getClaimsFormToken(token);

			System.out.println("expireTime : " + claims.getExpiration());
			System.out.println("loginId : " + claims.get(LOGIN_ID));
			System.out.println("username : " + claims.get(USERNAME));

			return true;
		} catch (ExpiredJwtException expiredJwtException) {
			return false;
		} catch (JwtException jwtException) {
			return false;
		} catch (NullPointerException npe) {
			return false;
		}
	}

	// 토큰에서 사용자 ID 반환
	public static String getUserIdFromToken(String token) {
		Claims claims = getClaimsFormToken(token);
		return claims.get(LOGIN_ID).toString();
	}
}
