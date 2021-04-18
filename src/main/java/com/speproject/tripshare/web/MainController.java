package com.speproject.tripshare.web;

import com.speproject.tripshare.config.JwtTokenProvider;
import com.speproject.tripshare.model.Trip;
import com.speproject.tripshare.model.User;
import com.speproject.tripshare.repository.UserRepository;
import com.speproject.tripshare.service.TripService;
import com.speproject.tripshare.service.UserServiceImpl;
import com.speproject.tripshare.web.dto.TripCreationDto;
import com.speproject.tripshare.web.dto.TripScoreDto;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@Controller
public class MainController {

	private static Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenProvider tokenProvider;

	@Autowired
	private TripService tripService;

	@Autowired
	private UserRepository userRepository;

	@GetMapping("/ts/login")
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

		User user = userRepository.findByEmail(auth.getName());
		user.setPassword("");
		return user;
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

	@PostMapping(value = "/user/authenticate", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public ResponseEntity<String> authenticate(@RequestBody User user) {
		log.info("UserResourceImpl : authenticate");
		JSONObject jsonObject = new JSONObject();
		try {
			log.info(user.getEmail() + " : " + user.getPassword());
			Authentication authentication = authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));
			if (authentication.isAuthenticated()) {
				String email = user.getEmail();
				jsonObject.put("name", authentication.getName());
				jsonObject.put("authorities", authentication.getAuthorities());
				jsonObject.put("token", tokenProvider.createToken(email, userRepository.findByEmail(email).getRole()));
				return new ResponseEntity<String>(jsonObject.toString(), HttpStatus.OK);
			}
		} catch (JSONException e) {
			try {
				jsonObject.put("exception", e.getMessage());
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
			return new ResponseEntity<String>(jsonObject.toString(), HttpStatus.UNAUTHORIZED);
		}
		return null;
	}



}
