package marchcat.security;

import java.util.Date;
import java.util.Optional;

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
	
	//TODO HARDCODED FOR NOW
	//@Value("${ACCESS_SECRET_KEY}")
	private static String accessSecretKey = "82d86070dfba752ab387f4060fe3e961c90a015920898af031d21f2261f3c1b0";
	//@Value("${REFRESH_SECRET_KEY}")
	private static String refreshSecretKey = "a2dec1923940d616997e3ca88a2cf305385266a8b45cd34d52626697aee2b252";
	//@Value("${STORAGE_SECRET_KEY}")
	private static String storageSecreftKey = "h6pfb7rn9dkvk6hbt78xrlp8ed55wbpszsv9vtrn1yc5bhs5pnjjztqx3nba4e7v";
	
	private SecretKey accessSecret;
	private SecretKey refreshSecret;
	public static long accessExpTime = 360_000L; //in MILLISECONDS
	public static long refreshExpTime = 30 * 24 * 60 * 60 * 1000L; //30 days
	public static long storageExpTime = 30 * 1000L;
	
	public JwtTokenProvider() {
		
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
	
	protected String generateRefreshToken(User user) {
		Date now = new Date();;
		Date expiration = new Date(now.getTime() + refreshExpTime);
		
		
		String refreshToken =  Jwts.builder()
				.subject(user.getUsername())
				.expiration(expiration)
				.signWith(refreshSecret)
				.compact();		
		
		return refreshToken;
	}
	
	
	protected Optional<Claims>  getAccessClaims(@NonNull String token) {
        return getClaims(token, accessSecret);
    }

  protected Optional<Claims> getRefreshClaims(@NonNull String token) {
        return getClaims(token, refreshSecret);
    }
	
	protected Optional<Claims> getClaims(@NonNull String token, @NonNull SecretKey secret) {
		Optional<Claims> claimsOptional = Optional.empty();
		try {
			claimsOptional = Optional.of(Jwts.parser()
                .verifyWith(secret)
                .build()
                .parseSignedClaims(token)
                .getPayload());
		} catch (JwtException | IllegalArgumentException | NullPointerException e) {
			LoggingAspect.log("Token is not valid: " + e.getMessage());
		}
			return claimsOptional;
    }
	protected void name() {
		
	}
}
