package com.goodmeaning.security;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;


@Component
public class RequestControlInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		System.out.println("getRequestURI:" + request.getRequestURI());
		
		HttpSession session = request.getSession(); 
		 
		if( session.getAttribute("user")==null && request.getRequestURI().indexOf("amdin")>=0) {
			response.sendRedirect("/admin/auth/login");
			return false;
		}else {
			return true;
		}
	}
	
	
	
	
	
	
	
}
