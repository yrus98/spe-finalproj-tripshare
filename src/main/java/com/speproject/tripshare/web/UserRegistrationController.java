package com.speproject.tripshare.web;

import com.speproject.tripshare.web.dto.UserRegistrationDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.speproject.tripshare.service.UserService;

import javax.ws.rs.Consumes;

@CrossOrigin(origins = "*")
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

	@PostMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity registerUserAccount(@RequestBody UserRegistrationDto registrationDto) {
		try {
			userService.save(registrationDto);
			return ResponseEntity.status(HttpStatus.OK).body("{'data':'Registered Successfully'}");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{'data':'Email already in use'}");
//			return "redirect:/registration?success";
		}
	}

}
