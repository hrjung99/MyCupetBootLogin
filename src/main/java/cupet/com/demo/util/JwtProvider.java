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

import cupet.com.demo.service.UserService;
import cupet.com.demo.vo.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
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

	private final long exp = 1000L * 60 * 60 * 4; //4시간

	private final UserService userDetailsService;

	@PostConstruct
	 protected void init() throws NoSuchAlgorithmException, InvalidKeySpecException {
		byte[] decodedKey = Base64.getDecoder().decode(salt);
        secretKey = new SecretKeySpec(decodedKey, "HmacSHA256");
	}

	public String createToken(User user) {
		Claims claims = Jwts.claims();

		Date now = new Date();
		return Jwts.builder().setHeader(createHeader(user)).setClaims(createClaim(user)).setIssuedAt(now)
				.setSubject(user.getCupet_user_id())
				.setExpiration(new Date(now.getTime() + exp)).signWith(secretKey, SignatureAlgorithm.HS256).compact();
	}

	private static Map<String, Object> createHeader(User user) {
		Map<String, Object> header = new HashMap<>();
		header.put("teamname", "KosaCupetTeam");
		// 필요한 정보 헤더에 추가 이후
		header.put("regDate", System.currentTimeMillis());
		return header;
	}
	private static Map<String , Object> createClaim(User user){
		Map<String, Object> claims = new HashMap<>();
		claims.put("userDataForm", user);
		//여기에 추가
		
		
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
			if (!token.substring(0, "BEARER ".length()).equalsIgnoreCase("BEARER ")) {
				return false;
			} else {
				token = token.split(" ")[1].trim();
			}
			Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
			// 만료되었을 시 false
			return !claims.getBody().getExpiration().before(new Date());
		} catch (Exception e) {
			return false;
		}
	}
	
	
}
