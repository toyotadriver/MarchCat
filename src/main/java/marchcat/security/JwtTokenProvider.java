package marchcat.security;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.xml.bind.DatatypeConverter;
import marchcat.users.User;
import marchcat.util.HashGen;
import marchcat.util.LoggingAspect;

@Component
public class JwtTokenProvider {
	
	//HARDCODED FOR NOW
	//@Value("${ACCESS_SECRET_KEY}")
	private static String accessSecretKey = "82d86070dfba752ab387f4060fe3e961c90a015920898af031d21f2261f3c1b0";
	//@Value("${REFRESH_SECRET_KEY}")
	private static String refreshSecretKey = "a2dec1923940d616997e3ca88a2cf305385266a8b45cd34d52626697aee2b252";
	
	private RedisTemplate<String, String> redisTemplate;
	
	private SecretKey accessSecret;
	private SecretKey refreshSecret;
	public static long accessExpTime = 360_000L; //in MILLISECONDS
	public static long refreshExpTime = 30 * 24 * 60 * 60 * 1000L; //30 days
	
	public JwtTokenProvider(RedisTemplate<String, String> redisTemplate) {
		this.redisTemplate = redisTemplate;
		
		byte[] apiAccessSecretKey = DatatypeConverter.parseBase64Binary(accessSecretKey);
		byte[] apiRefreshSecretKey = DatatypeConverter.parseBase64Binary(refreshSecretKey);
		this.accessSecret = new SecretKeySpec(apiAccessSecretKey, "HmacSHA256");
		this.refreshSecret = new SecretKeySpec(apiRefreshSecretKey, "HmacSHA256");
		
	}
	
	protected String generateAccessToken(User user) {
		Date now = new Date();;
		Date expiration = new Date(now.getTime() + accessExpTime);
		
		String accessToken = Jwts.builder()
				.subject(user.getUsername())
				.claim("id", user.getId())
				.claim("role", user.getRole())
				.issuedAt(now)
				.expiration(expiration)
				.signWith(accessSecret, Jwts.SIG.HS256)
				.compact();
		
		return accessToken;
	}
	
	protected String generateRefreshToken(User user, HttpServletRequest request) {
		Date now = new Date();;
		Date expiration = new Date(now.getTime() + refreshExpTime);
		
		
		String refreshToken =  Jwts.builder()
				.subject(user.getUsername())
				.expiration(expiration)
				.signWith(refreshSecret)
				.compact();
		
		String tokenHash = HashGen.generateStringHash(refreshToken);
		String userAgent = request.getHeader("User-Agent");
		String userName = user.getUsername();
		
		redisTemplate.opsForHash().put(userName, userAgent, tokenHash);
		redisTemplate.expire(userName, 10, TimeUnit.DAYS);
		
		
		return refreshToken;
	}
	
	protected boolean validateAccessToken(String accessToken) {
		return validateToken(accessToken, accessSecret);
	}
	protected boolean validateRefreshToken(String refreshToken, HttpServletRequest request) {
		if(validateToken(refreshToken, refreshSecret)) {
			
			String refreshTokenHash = HashGen.generateStringHash(refreshToken);
			Claims claims = getClaims(refreshToken, refreshSecret);
			String username = claims.getSubject();
			String userAgent = request.getHeader("User-Agent");
			
			if(redisTemplate.opsForHash().get(username, userAgent) == refreshTokenHash) {
				return true;
			}
			
		}
		
		return false;
	}
	
	protected Claims getAccessClaims(@NonNull String token) {
        return getClaims(token, accessSecret);
    }

    protected Claims getRefreshClaims(@NonNull String token) {
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
	
	protected Claims getClaims(@NonNull String token, @NonNull SecretKey secret) {
        return Jwts.parser()
                .verifyWith(secret)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
