package cupet.com.demo.util;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

import javax.crypto.spec.SecretKeySpec;

public class KeyGenerator {

	public  SecretKeySpec generateSecretKey(String salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
        String algorithm = "HmacSHA256";
        byte[] decodedKey = Base64.getDecoder().decode(salt);
        return new SecretKeySpec(decodedKey, algorithm);
    }
}
