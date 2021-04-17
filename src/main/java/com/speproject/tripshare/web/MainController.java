package com.speproject.tripshare.web;

import com.speproject.tripshare.model.Trip;
import com.speproject.tripshare.model.User;
import com.speproject.tripshare.repository.UserRepository;
import com.speproject.tripshare.service.TripService;
import com.speproject.tripshare.web.dto.TripCreationDto;
import com.speproject.tripshare.web.dto.TripScoreDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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
		Trip trip = tripService.save(tripCreationDto);
		return "redirect:/user/matchtrips/" + trip.getTripId();
	}

	@GetMapping("/user/getdetails")
	@ResponseBody
	public User getUserDetails(Authentication auth){
		return userRepository.findByEmail(auth.getName());
	}

	@GetMapping("/user/gettrips")
	@ResponseBody
	public List<Trip> getUserTrips(Authentication auth){
		User user = userRepository.findByEmail(auth.getName());

		return user.getTripList();
	}

	@GetMapping("/user/matchtrips/{tripId}")
	@ResponseBody
	public List<TripScoreDto> matchUserTrips(@PathVariable Long tripId, Authentication auth){
		Trip trip = tripService.getTripByTripId(tripId);
		if(!trip.getUser().getEmail().equals(auth.getName()))
			return null;
		return tripService.matchUserTrips(tripId, 0, 10);
	}


}
