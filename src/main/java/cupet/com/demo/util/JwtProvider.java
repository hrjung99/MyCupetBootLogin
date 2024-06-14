package cupet.com.demo.util;

import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import cupet.com.demo.mapper.UserMapper;
import cupet.com.demo.service.AuthService;
import cupet.com.demo.service.UserService;
import cupet.com.demo.vo.LoginTokenVO;
import cupet.com.demo.vo.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtProvider {

	@Value("${jwt.secret.key}")
	private String salt;

	private Key secretKey;

	//private final long exp = 1000L * 60; // 10초
	private final long exp = 1000L * 60 * 60 ; // 1시간

	private final long refreshexp = 1000L * 60 * 60 * 72; // 72시간

	private final UserService userDetailsService;
	private final AuthService authService;
	private final UserMapper userMapper;

	@PostConstruct
	protected void init() throws NoSuchAlgorithmException, InvalidKeySpecException {
		byte[] decodedKey = Base64.getDecoder().decode(salt);
		secretKey = new SecretKeySpec(decodedKey, "HmacSHA256");
	}

	public String createToken(User user) {
		Claims claims = Jwts.claims();

		Date now = new Date();

		Date intoken = new Date(now.getTime() + exp);
		Date intokenRef = new Date(now.getTime() + refreshexp);
		String id = user.getCupet_user_id();

		// 리프레시 토큰 정보를 DB에 기입
		userMapper.insertLoginToken(LoginTokenVO.builder().cupet_user_id(id).Expiration_date(intokenRef).build());
		// 리프레시 토큰 중복을 방지하기 위한 절차
		authService.refreshTokenClear(id);

		return Jwts.builder().setHeader(createHeader(user)).setClaims(createClaim(user)).setIssuedAt(now).setSubject(id)
				.setExpiration(intoken).signWith(secretKey, SignatureAlgorithm.HS256).compact();
	}

	private static Map<String, Object> createHeader(User user) {
		Map<String, Object> header = new HashMap<>();
		header.put("teamname", "KosaCupetTeam");
		// 필요한 정보 헤더에 추가 이후
		header.put("regDate", System.currentTimeMillis());
		return header;
	}

	private static Map<String, Object> createClaim(User user) {
		Map<String, Object> claims = new HashMap<>();
		claims.put("userDataForm", user);
		// 여기에 추가

		return claims;
	}

	public Authentication getAuthentication(String token) {
		UserDetails userDetails = userDetailsService.loadUserByUsername(this.getAccount(token));
		return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
	}

	private String getAccount(String token) {
		return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody().getSubject();
	}

	public String resolveToken(HttpServletRequest request) {
		return request.getHeader("Authorization");
	}

	// 토큰 검증
	public boolean validateToken(String token) {
		try {
			// Bearer 검증
			if (token.substring(0, "BEARER ".length()).equalsIgnoreCase("BEARER ")) {
				token = token.split(" ")[1].trim();
			}

			System.out.println("validateToken insert");
			Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
			// 만료되었을 시 false
			System.out.println("claims insert");
			boolean res = !claims.getBody().getExpiration().before(new Date());
			System.out.println("유효시간 검증 :" + res);
			return res;
		} catch (ExpiredJwtException e) {

			return false;
		}
	}

	public boolean vlidateRefreshToken(String token) {
		System.out.println("리프레시 토큰에 대하여 검증 시작");
		if (token.substring(0, "BEARER ".length()).equalsIgnoreCase("BEARER ")) {
			token = token.split(" ")[1].trim();

		}
		Claims claims = getExpirationTokenDetail(token);
		System.out.println(claims);
		Map<String, Object> userDataMap = (Map<String, Object>) claims.get("userDataForm");
		String user_id = (String) userDataMap.get("cupet_user_id");
		List<LoginTokenVO> list = userMapper.dbLoginTokenListofLoginUser(user_id);
		if (list.isEmpty()) {
			return false;
		} else {
			LoginTokenVO item = list.get(0);
			System.out.println("sss  :" + item.getCupet_user_id());
			if (item.aftercheckedExpiration_date(new Date())) {
				System.out.println("리프레시 유효");
				return true;
			} else {
				System.out.println("리프레시 만료");
				return false;
			}
		}

	}

	public Claims getExpirationTokenDetail(String token) {
		try {
			Claims claims = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();

			return claims;
		} catch (ExpiredJwtException e) {
			return e.getClaims();
		}
	}

	public String RefreshcreateToken(String token) {
		if (token.substring(0, "BEARER ".length()).equalsIgnoreCase("BEARER ")) {
			token = token.split(" ")[1].trim();
		}
		Claims claims = getExpirationTokenDetail(token);
		System.out.println(claims);
		Map<String, Object> userDataMap = (Map<String, Object>) claims.get("userDataForm");
		String user_id = (String) userDataMap.get("cupet_user_id");
		User newuser = userMapper.login(User.builder().cupet_user_id(user_id).build());
		return createToken(newuser);

	}

}
