package com.github.tbquyen.config;

import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.util.WebUtils;

import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
@Service
public class JwtService {
	public static final SecretKey SECRET_KEY = Jwts.SIG.HS256.key().build();
	public static final String COOKIE_NAME = "jwtToken";

	public void setTokenToCookie(HttpServletResponse response, UserDetails userDetails) {
		String jwt = Jwts.builder().subject(userDetails.getUsername()).signWith(SECRET_KEY).compact();
		// Đặt JWT vào cookie
		Cookie cookie = new Cookie(COOKIE_NAME, jwt);
		cookie.setHttpOnly(true); // chặn việc truy cập cookie thông qua JavaScript
		cookie.setSecure(true); // Đảm bảo chỉ gửi cookie qua HTTPS
		cookie.setPath("/");
		cookie.setMaxAge(24 * 60 * 60); // Cookie tồn tại trong 1 ngày
		response.addCookie(cookie);
	}

	public void removeToken(HttpServletResponse response) {
		Cookie cookie = new Cookie(COOKIE_NAME, null);
		cookie.setHttpOnly(true);
		cookie.setSecure(true);
		cookie.setPath("/");
		cookie.setMaxAge(0);
		response.addCookie(cookie);
	}

	public String getUserNameFromJwtToken(String token) {
		if (!StringUtils.hasText(token)) {
			throw new IllegalArgumentException("JWT claims string is empty");
		}
		return Jwts.parser().verifyWith(SECRET_KEY).build().parseSignedClaims(token).getPayload().getSubject();
	}

	/**
	 * trích xuất JWT (JSON Web Token) từ yêu cầu HTTP
	 * 
	 * @param request
	 * @return
	 */
	public String getJwtFromRequest(HttpServletRequest request) {
		Cookie cookie = WebUtils.getCookie(request, COOKIE_NAME);
		if (cookie != null) {
			return cookie.getValue();
		}

		return null;
	}
}
