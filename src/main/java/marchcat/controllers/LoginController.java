package marchcat.controllers;

import java.util.Arrays;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import marchcat.security.JwtTokenProvider;
import marchcat.users.LoginProcessor;
import marchcat.users.User;
import marchcat.users.exception.LoginFailedException;

@Controller
public class LoginController {

	//private boolean loggedIn = false;
	private LoginProcessor loginProcessor;
	JwtTokenProvider jwtTokenProvider;

	public LoginController(LoginProcessor loginProcessor, JwtTokenProvider jtp) {
		
		this.jwtTokenProvider = jtp;
		this.loginProcessor = loginProcessor;
	}

	@GetMapping("/login")
	public String viewLoginPage(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		Optional<Cookie> accessTokenCookie = Arrays.stream(cookies)
				.filter(c -> "access_token".equals(c.getName()))
				.findAny();
		if(accessTokenCookie.isPresent()) {
			String accessToken = accessTokenCookie.get().getValue();
			
			if(jwtTokenProvider.validateAccessToken(accessToken)) {
				return "redirect:/main";
			}
		}
		
		return "login.html";
	}

	/**
	 * Check for login and set Logged username to logged in username
	 * 
	 * @param model
	 * @param username
	 * @param password
	 * @return
	 */

	@PostMapping("/login")
	public ResponseEntity<String> postLogin(@RequestHeader String username,
			@RequestHeader String password,
			HttpServletRequest request,
			HttpServletResponse response) {

		User user;
		try {
			user = loginProcessor.login(username, password);
			String accessToken = jwtTokenProvider.generateAccessToken(user);
			String refreshToken = jwtTokenProvider.generateRefreshToken(user);
			
			response.addHeader("Set-Cookie", "access_token=" + accessToken +
                    "; Max-Age=" + (JwtTokenProvider.accessExpTime / 1000) + ";" +
                    "Path=/;" +
                    "HttpOnly=true;" +
//                    "Secure=true;" +
                    "SameSite=Strict;");
			response.addHeader("Set-Cookie", "refresh_token=" + refreshToken +
                    "; Max-Age=" + (JwtTokenProvider.refreshExpTime / 1000) + ";" +
                    "Path=/;" +
                    "HttpOnly=true;" +
//                    "Secure=true;" +
                    "SameSite=Strict;");
			
			return ResponseEntity
					.status(200)
					.body(null);
			
		} catch (LoginFailedException e) {
			return ResponseEntity
					.status(400)
					.body("Username or password is invalid");
		}

	}
}
