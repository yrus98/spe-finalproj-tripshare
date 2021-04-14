package com.speproject.tripshare.web;

import com.speproject.tripshare.model.Trip;
import com.speproject.tripshare.model.User;
import com.speproject.tripshare.repository.TripRepository;
import com.speproject.tripshare.repository.UserRepository;
import com.speproject.tripshare.service.TripService;
import com.speproject.tripshare.web.dto.TripCreationDto;
import com.speproject.tripshare.web.dto.UserRegistrationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class MainController {

	@Autowired
	private TripService tripService;

	@Autowired
	private UserRepository userRepository;

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

	@ModelAttribute("trip")
	public TripCreationDto tripCreationDto() {
		return new TripCreationDto();
	}


	@GetMapping("/user/createtrip")
	public String createTripForm(){
		return "createtrip";
	}

	@PostMapping("/user/createtrip")
	public String submitCreateTrip(@ModelAttribute("trip") TripCreationDto tripCreationDto) {
		tripService.save(tripCreationDto);
		return "redirect:/user/createtrip?success";
	}

	@GetMapping("user/gettrips")
	@ResponseBody
	public List<Trip> getUserTrips(Authentication auth){
		User user = userRepository.findByEmail(auth.getName());

		List<Trip> trips = user.getTripList();
		return trips;
	}


}
