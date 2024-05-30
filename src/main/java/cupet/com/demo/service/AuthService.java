package cupet.com.demo.service;

import java.security.Key;
import java.util.Base64;
import java.util.Map;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import cupet.com.demo.vo.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@Service
public class AuthService {

	@Value("${jwt.secret.key}")
	private String salt;

	private Key secretKey;

	public Map<String, Object>  GetUserparseToken(String token) {
		byte[] decodedKey = Base64.getDecoder().decode(salt);
        secretKey = new SecretKeySpec(decodedKey, "HmacSHA256");
		Claims claims = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();
		System.out.println(claims);
		Map<String, Object> userDataMap = (Map<String, Object>) claims.get("userDataForm");
		return userDataMap;
	}
	
	
}
