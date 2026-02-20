package marchcat.security;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import marchcat.users.User;
import marchcat.users.UserRepository;
import marchcat.util.HashGen;
import marchcat.util.LoggingAspect;

@Component
public class TokenManager {
	
	static String aTokenName = "access_token";
	static String rTokenName = "refresh_token";
	static String sTokenName = "storage_token";
	static String refreshTokenRedisPrefix = "Refresh: ";

	private JwtTokenProvider jwtTokenProvider;
	private UserRepository userRepository;
	private RedisTemplate<String, String> redisTemplate;

	public TokenManager(JwtTokenProvider jwtTokenProvider, UserRepository userRepository, RedisTemplate<String, String> redisTemplate) {
		this.jwtTokenProvider = jwtTokenProvider;
		this.userRepository = userRepository;
		this.redisTemplate = redisTemplate;
	}

	public Optional<String> validateAccess(HttpServletRequest request, HttpServletResponse response) throws TokenException{
		Optional<String> accessTokenOptional = getTokenFromRequest(request, aTokenName);
		
		try {
			String accessToken = accessTokenOptional.get();
			
			if (validateAccessToken(accessToken).isEmpty()) {
				
				if(validateRefresh(request)) {
					Optional<String> refreshTokenOptional = getTokenFromRequest(request, rTokenName);
					Optional<Claims> claims = jwtTokenProvider.getRefreshClaims(refreshTokenOptional.get());
					
					String subject = claims.get().getSubject();
					
					User user = userRepository.findUserByName(subject);
					
					putAccessTokenToResponse(response, user);
				} else {
					throw new TokenException("Refresh token is invalid");
				}
				
			}
		} catch (NoSuchElementException e) {
			LoggingAspect.log("No such token: " + e.getMessage());
			throw new TokenException("Failed to validate accessToken", e);
		}

		return accessTokenOptional;
	}

	private boolean validateRefresh(HttpServletRequest request) {
		Optional<String> refreshTokenOptional = getTokenFromRequest(request, rTokenName);

		if (refreshTokenOptional.isPresent()) {
			String refreshToken = refreshTokenOptional.get();
			if (validateRefreshToken(refreshToken, request)) {
				return true;
			}
		}

		return false;
	}
	
	private Optional<Claims> validateAccessToken(String accessToken) {
		return jwtTokenProvider.getAccessClaims(accessToken);
	}
	private boolean validateRefreshToken(String refreshToken, HttpServletRequest request) {
		if(jwtTokenProvider.getRefreshClaims(refreshToken).isPresent()) {
			
			String refreshTokenHash = HashGen.generateStringHash(refreshToken);
			Claims claims;
			try {
				claims = jwtTokenProvider.getRefreshClaims(refreshToken).get();
			} catch (NoSuchElementException e) {
				return false;
			}
			String username = claims.getSubject();
			String refreshRedisKey = refreshTokenRedisPrefix + username;
			String userAgent = request.getHeader("User-Agent");
			
			if(redisTemplate.opsForHash().get(refreshRedisKey, userAgent) == refreshTokenHash) {
				return true;
			}
			
		}
		
		return false;
	}
	

	public void putAccessTokenToResponse(HttpServletResponse response, User user) {
		String accessToken = jwtTokenProvider.generateAccessToken(user);

		putTokenToResponse(response, aTokenName, accessToken, JwtTokenProvider.accessExpTime / 1000);//in SECONDS
	}
	
	public void putRefreshTokenToResponse(HttpServletRequest request, HttpServletResponse response, User user) {
		String refreshToken = jwtTokenProvider.generateRefreshToken(user);

		String tokenHash = HashGen.generateStringHash(refreshToken);
		String userAgent = request.getHeader("User-Agent");
		String refreshRedisKey = refreshTokenRedisPrefix + user.getUsername();
		
		redisTemplate.opsForHash().put(refreshRedisKey, userAgent, tokenHash);
		redisTemplate.expire(refreshRedisKey, 10, TimeUnit.DAYS);
		
		putTokenToResponse(response, rTokenName, refreshToken, JwtTokenProvider.refreshExpTime / 1000);//in SECONDS
	}
	
	public void putStoragetokenToResponse(HttpServletRequest request, HttpServletResponse response, String token) {
		putTokenToResponse(response, sTokenName, token, JwtTokenProvider.storageExpTime / 1000);//in SECONDS
	}
	
	public void putExpiredTokens(HttpServletRequest request, HttpServletResponse response) {
		Cookie[] cookies = request.getCookies();
		Optional<Cookie> refreshTokenCookie = Arrays.stream(cookies).filter(c -> rTokenName.equals(c.getName()))
				.findAny();
		Optional<Claims> claims = jwtTokenProvider.getRefreshClaims(refreshTokenCookie.get().getValue());
		String subject = claims.get().getSubject();
		
		putTokenToResponse(response, aTokenName, "", 0);
		putTokenToResponse(response, rTokenName, "", 0);
		deleteTokenFromRedis(request, subject);
	}

	private void putTokenToResponse(HttpServletResponse response, String tokenName, String token, long expirationTime) {

		response.addHeader("Set-Cookie", tokenName + "=" + token + "; Max-Age="
				+ (expirationTime) + 
				";" + "Path=/;" + 
				"HttpOnly=true;" +
//                "Secure=true;" +
				"SameSite=Strict;");
	}
	
	/**
	 * Get token from the request.
	 * @param request - {@link HttpServletRequest}
	 * @param tokenName - {@link String}
	 * @return {@link Optional}<String> - optional of token
	 */
	private Optional<String> getTokenFromRequest(HttpServletRequest request, String tokenName) {
		Cookie[] cookies = request.getCookies();
		Optional<Cookie> tokenCookie = Arrays.stream(cookies).filter(c -> tokenName.equals(c.getName()))
				.findAny();
		Optional<String> tokenOptional = tokenCookie.map(Cookie::getValue);
		
		return tokenOptional;
	}
	public String getUsernameFromToken(String token) throws TokenException{
		String username;
		try {
			username = jwtTokenProvider.getAccessClaims(token).get().getSubject();
		} catch (NoSuchElementException e) {
			throw new TokenException("Can't get username from the token: " + token);
		}
		return username;
	}
	public String getIdFromToken(String token) throws TokenException{
		String id;
		try {
			id = (String) jwtTokenProvider.getAccessClaims(token).get().get("id");
		} catch (NoSuchElementException e) {
			throw new TokenException("Can't get id from the token: " + token);
		}
		
		return id;
	}
	
	private void deleteTokenFromRedis(HttpServletRequest request, String subjectName) {
		String userAgent = request.getHeader("User-Agent");
		String redisKey = refreshTokenRedisPrefix + subjectName;
		redisTemplate.opsForHash().delete(redisKey, userAgent);
	}
}
