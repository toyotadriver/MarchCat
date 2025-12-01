package marchcat.security;

import java.util.Date;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.xml.bind.DatatypeConverter;
import marchcat.users.User;
import marchcat.util.LoggingAspect;

@Component
public class JwtTokenProvider {
	
	//@Value("${ACCESS_SECRET_KEY}")
	private static String accessSecretKey = "82d86070dfba752ab387f4060fe3e961c90a015920898af031d21f2261f3c1b0";
	//@Value("${REFRESH_SECRET_KEY}")
	private static String refreshSecretKey = "a2dec1923940d616997e3ca88a2cf305385266a8b45cd34d52626697aee2b252";
	
	private SecretKey accessSecret;
	private SecretKey refreshSecret;
	public static long accessExpTime = 360_000L;
	public static long refreshExpTime = 30 * 24 * 60 * 60 * 1000L; //30 days
	
	public JwtTokenProvider() {
		
		byte[] apiAccessSecretKey = DatatypeConverter.parseBase64Binary(accessSecretKey);
		byte[] apiRefreshSecretKey = DatatypeConverter.parseBase64Binary(refreshSecretKey);
		this.accessSecret = new SecretKeySpec(apiAccessSecretKey, "HmacSHA256");
		this.refreshSecret = new SecretKeySpec(apiRefreshSecretKey, "HmacSHA256");
		
	}
	
	public String generateAccessToken(User user) {
		Date now = new Date();;
		Date expiration = new Date(now.getTime() + accessExpTime);
		return Jwts.builder()
				.subject(user.getUsername())
				.claim("id", user.getId())
				.claim("role", user.getRole())
				.issuedAt(now)
				.expiration(expiration)
				.signWith(accessSecret, Jwts.SIG.HS256)
				.compact();
	}
	
	public String generateRefreshToken(User user) {
		Date now = new Date();;
		Date expiration = new Date(now.getTime() + refreshExpTime);
		return Jwts.builder()
				.subject(user.getUsername())
				.expiration(expiration)
				.signWith(refreshSecret)
				.compact();
	}
	
	public boolean validateAccessToken(String accessToken) {
		return validateToken(accessToken, accessSecret);
	}
	public boolean validateRefreshToken(String refreshToken) {
		return validateToken(refreshToken, refreshSecret);
	}
	
	public Claims getAccessClaims(@NonNull String token) {
        return getClaims(token, accessSecret);
    }

    public Claims getRefreshClaims(@NonNull String token) {
        return getClaims(token, refreshSecret);
    }
	
	
	private boolean validateToken(@NonNull String token, SecretKey key) {
		try {
			Jwts.parser()
				.verifyWith(key)
				.build()
				.parseSignedClaims(token)
				.getPayload();
			return true;
		} catch (JwtException | IllegalArgumentException e) {
			LoggingAspect.log("Token is not valid: " + e.getMessage());
		}
		return false;
	}
	
	private Claims getClaims(@NonNull String token, @NonNull SecretKey secret) {
        return Jwts.parser()
                .verifyWith(secret)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
