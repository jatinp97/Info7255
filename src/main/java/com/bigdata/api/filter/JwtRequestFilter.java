package com.bigdata.api.filter;

import java.io.IOException;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.bigdata.api.model.ErrorResponse;
import com.bigdata.api.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {
	
	 private final ObjectMapper mapper;
	    private final JwtUtil jwtUtil;

	    public JwtRequestFilter(ObjectMapper mapper, JwtUtil jwtUtil) {
	        this.mapper = mapper;
	        this.jwtUtil = jwtUtil;
	    }

	    @Override
	    protected boolean shouldNotFilter(HttpServletRequest request) {
	        String path = request.getRequestURI();
	        return "/token".equals(path);
	    }

	    @Override
	    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
	        final String authorizationHeader = httpServletRequest.getHeader("Authorization");

	        if (authorizationHeader == null) {
	            ErrorResponse errorResponse = new ErrorResponse(
	                    "Token missing",
	                    HttpStatus.UNAUTHORIZED.value(),
	                    new Date(),
	                    HttpStatus.UNAUTHORIZED.getReasonPhrase()
	            );

	            httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
	            httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);

	            mapper.writeValue(httpServletResponse.getWriter(), errorResponse);
	            return;
	        }

	        boolean isValid;
	        try {
	            String token = authorizationHeader.substring(7);
	            isValid = jwtUtil.validateToken(token);
	        } catch (Exception e) {
	            System.out.println(e);
	            isValid = false;
	        }

	        if (!isValid) {
	            ErrorResponse errorResponse = new ErrorResponse(
	                    "Invalid Token!",
	                    HttpStatus.UNAUTHORIZED.value(),
	                    new Date(),
	                    HttpStatus.UNAUTHORIZED.getReasonPhrase()
	            );

	            httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
	            httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);

	            mapper.writeValue(httpServletResponse.getWriter(), errorResponse);
	            return;
	        }

	        filterChain.doFilter(httpServletRequest, httpServletResponse);
	}


}
