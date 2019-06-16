package org.mahmoudi.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mahmoudi.entities.AppUser;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;


public class JWTAthenticationFilter extends UsernamePasswordAuthenticationFilter{
	private AuthenticationManager authenticationManager;

	public JWTAthenticationFilter(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		try {
			AppUser user = new ObjectMapper().readValue(request.getInputStream(), AppUser.class);
			return authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("Problem in request content! ");
		}	
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		User user = (User) authResult.getPrincipal();
		List<String> roles = new ArrayList<String>();
		user.getAuthorities().forEach(role->{
			roles.add(role.getAuthority());
		});
		String jwt = JWT.create()
						.withIssuer(request.getRequestURI())
						.withSubject(user.getUsername())
						.withArrayClaim("roles", roles.toArray(new String[roles.size()]))
						.withExpiresAt(new Date(System.currentTimeMillis()+SecurityParams.EXPIRATION))
						.sign(Algorithm.HMAC256(SecurityParams.SECRET));
		response.setHeader(SecurityParams.JWT_HEADER_NAME, jwt);
	
	}
	
	
}
