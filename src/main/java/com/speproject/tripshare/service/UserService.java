package com.speproject.tripshare.service;

import com.speproject.tripshare.model.User;
import com.speproject.tripshare.web.dto.UserRegistrationDto;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService{
	User save(UserRegistrationDto registrationDto);
}
