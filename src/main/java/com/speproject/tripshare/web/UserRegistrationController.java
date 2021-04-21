package com.speproject.tripshare.web;

import com.speproject.tripshare.web.dto.UserRegistrationDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.speproject.tripshare.service.UserService;

@Controller
@RequestMapping("/registration")
public class UserRegistrationController {

	private UserService userService;

	public UserRegistrationController(UserService userService) {
		super();
		this.userService = userService;
	}
	
	@ModelAttribute("user")
    public UserRegistrationDto userRegistrationDto() {
        return new UserRegistrationDto();
    }
	
	@GetMapping
	public String showRegistrationForm() {
		return "registration";
	}
	
	@PostMapping
	public ResponseEntity registerUserAccount(@ModelAttribute("user") UserRegistrationDto registrationDto) {
		try {
			userService.save(registrationDto);
			return ResponseEntity.status(HttpStatus.OK).body("{'data':'Registered Successfully'}");
		}catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{'data':'Email already in use'}");
//			return "redirect:/registration?success";
		}
	}

}
