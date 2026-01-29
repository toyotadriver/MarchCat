package marchcat.security;

import java.util.Arrays;
import java.util.Optional;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import marchcat.users.User;
import marchcat.users.UserRepository;

@Component
public class TokenManager {

	private JwtTokenProvider jwtTokenProvider;
	private UserRepository userRepository;

	public TokenManager(JwtTokenProvider jwtTokenProvider, UserRepository userRepository) {
		this.jwtTokenProvider = jwtTokenProvider;
		this.userRepository = userRepository;
		
	}

	public boolean validateAccess(HttpServletRequest request, HttpServletResponse response) {
		Cookie[] cookies = request.getCookies();
		Optional<Cookie> accessTokenCookie = Arrays.stream(cookies).filter(c -> "access_token".equals(c.getName()))
				.findAny();
		if (accessTokenCookie.isPresent()) {
			String accessToken = accessTokenCookie.get().getValue();
			if (jwtTokenProvider.validateAccessToken(accessToken)) {
				return true;
			} else {
				
				if(validateRefresh(request)) {
					Optional<Cookie> refreshTokenCookie = Arrays.stream(cookies).filter(c -> "refresh_token".equals(c.getName()))
							.findAny();
					Claims claims = jwtTokenProvider.getRefreshClaims(refreshTokenCookie.get().getValue());
					
					String subject = claims.getSubject();
					
					User user = userRepository.findUserByName(subject);
					
					putAccessTokenToResponse(response, user);
					return true;
				}
				
			}
		}

		return false;
	}

	private boolean validateRefresh(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		Optional<Cookie> refreshTokenCookie = Arrays.stream(cookies).filter(c -> "refresh_token".equals(c.getName()))
				.findAny();
		if (refreshTokenCookie.isPresent()) {
			String refreshToken = refreshTokenCookie.get().getValue();
			if (jwtTokenProvider.validateRefreshToken(refreshToken, request)) {
				return true;
			}
		}

		return false;
	}
	
	
	

	public void putAccessTokenToResponse(HttpServletResponse response, User user) {
		String accessToken = jwtTokenProvider.generateAccessToken(user);

		putTokenToResponse(response, "access_token", accessToken, JwtTokenProvider.accessExpTime / 1000);//in SECONDS
	}
	
	public void putRefreshTokenToResponse(HttpServletRequest request, HttpServletResponse response, User user) {
		String refreshToken = jwtTokenProvider.generateRefreshToken(user, request);
		
		putTokenToResponse(response, "refresh_token", refreshToken, JwtTokenProvider.refreshExpTime / 1000);//in SECONDS
	}

	private void putTokenToResponse(HttpServletResponse response, String tokenName, String token, long expirationTime) {

		response.addHeader("Set-Cookie", tokenName + "=" + token + "; Max-Age="
				+ (expirationTime) + 
				";" + "Path=/;" + 
				"HttpOnly=true;" +
//                "Secure=true;" +
				"SameSite=Strict;");
	}
}
