package com.goodmeaning.controller;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LogoutController {
	
	  @RequestMapping("/invalidate")
	  public String invalidateSession(HttpSession session) {
	    session.invalidate();
	    return "redirect:/auth/login";
	  }

}
