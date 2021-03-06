package com.financemanager.config.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import lombok.RequiredArgsConstructor;

import static org.springframework.util.StringUtils.hasText;

import com.financemanager.entity.CustomUserDetails;
import com.financemanager.service.impl.CustomUserDetailsService;

@Component
@RequiredArgsConstructor
public class JwtFilter extends GenericFilterBean {

	private final JwtProvider jwtProvider;
	private final CustomUserDetailsService customUserDetailsService;
	
	public static final String AUTHORIZATION = "Authorization";
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		logger.info("do filter...");
		String token = getTokenFromRequest((HttpServletRequest) request); // extract header from servlet
		if (token != null && jwtProvider.validateToken(token)) {
            String userLogin = jwtProvider.getLoginFromToken(token);// взяли логін з токена
            CustomUserDetails customUserDetails = customUserDetailsService.loadUserByUsername(userLogin); //взяли обєкт юзердетаілс з логіна
            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());//передали автентифікацію в контекст
            SecurityContextHolder.getContext().setAuthentication(auth);//передай запит і відповідь далі
        }
		chain.doFilter(request, response);
	}

	private String getTokenFromRequest(HttpServletRequest request) {
        String bearer = request.getHeader(AUTHORIZATION);
        if (hasText(bearer) && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }
        return null;
    }
}
