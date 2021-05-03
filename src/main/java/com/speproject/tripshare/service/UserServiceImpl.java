package com.speproject.tripshare.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import com.speproject.tripshare.model.Role;
import com.speproject.tripshare.model.User;
import com.speproject.tripshare.repository.UserRepository;
import com.speproject.tripshare.web.dto.UserProfileDto;
import com.speproject.tripshare.web.dto.UserRegistrationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{

	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	public UserServiceImpl(UserRepository userRepository) {
		super();
		this.userRepository = userRepository;
	}

	@Override
	public User save(UserRegistrationDto registrationDto) {
		User user = new User(registrationDto.getFirstName(), 
				registrationDto.getLastName(), registrationDto.getEmail(),
				passwordEncoder.encode(registrationDto.getPassword()), new Role("ROLE_USER"));
		
		return userRepository.save(user);
	}

	@Override
	public User update(String username, UserProfileDto userProfileDto) {
		User user = userRepository.findByEmail(username);
		user.setFirstName(userProfileDto.getFirstName());
		user.setLastName(userProfileDto.getLastName());
		user.setPassword(passwordEncoder.encode(userProfileDto.getPassword()));
		user.setDob(userProfileDto.getDob());
		user.setGender(userProfileDto.getGender());
		user.setDescription(userProfileDto.getDescription());

		return userRepository.save(user);
	}

    @Override
    public User getUserFromId(Long id) {
		return userRepository.findById(id).orElse(null);
	}

    @Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		User user = userRepository.findByEmail(username);
		if(user == null) {
			throw new UsernameNotFoundException("Invalid username or password.");
		}
		return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), getGrantedAuthority(user));
	}

	private Collection<GrantedAuthority> getGrantedAuthority(User user) {
		Collection<GrantedAuthority> authorities = new ArrayList<>();
		if (user.getRole().getName().equalsIgnoreCase("admin")) {
			authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
		}
		authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
		return authorities;
	}


}
