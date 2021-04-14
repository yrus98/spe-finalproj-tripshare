package com.speproject.tripshare.web;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MainController {
	
	@GetMapping("/login")
	public String login() {
		return "login";
	}
	
	@GetMapping("/")
	public String home() {
		return "index";
	}

	@GetMapping("/rr")
	@ResponseBody
	public String rtest(Authentication auth){
//		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return auth.getName() + auth.getAuthorities() + auth.getCredentials();
	}
}
