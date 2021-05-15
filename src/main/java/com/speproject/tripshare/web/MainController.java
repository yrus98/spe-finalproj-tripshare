package com.speproject.tripshare.web;

import com.speproject.tripshare.config.JwtTokenProvider;
import com.speproject.tripshare.model.ChatMessage;
import com.speproject.tripshare.model.Trip;
import com.speproject.tripshare.model.User;
import com.speproject.tripshare.repository.ChatMessageRepository;
import com.speproject.tripshare.repository.UserRepository;
import com.speproject.tripshare.service.ChatMessageService;
import com.speproject.tripshare.service.TripService;
import com.speproject.tripshare.service.UserService;
import com.speproject.tripshare.service.UserServiceImpl;
import com.speproject.tripshare.web.dto.ChatMessageDto;
import com.speproject.tripshare.web.dto.TripCreationDto;
import com.speproject.tripshare.web.dto.TripScoreDto;
import com.speproject.tripshare.web.dto.UserProfileDto;
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
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@CrossOrigin(origins = "*")
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

	@Autowired
	private UserService userService;

	@Autowired
	private ChatMessageService chatMessageService;

	@Autowired
	private ChatMessageRepository chatMessageRepository;

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
	public String rtest(Authentication auth) {
//		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return auth.getName() + auth.getAuthorities() + auth.getCredentials();
	}

	@ModelAttribute("trip")
	public TripCreationDto tripCreationDto() {
		return new TripCreationDto();
	}


	@GetMapping("/user/createtrip")
	public String createTripForm() {
		return "createtrip";
	}

	@PostMapping("/user/createtrip")
	public ResponseEntity submitCreateTrip(@RequestBody TripCreationDto tripCreationDto) {
		try {
			Trip trip = tripService.save(tripCreationDto);
			return ResponseEntity.status(HttpStatus.OK).body("{\"data\":\"Trip created Successfully\", \"tripId\":" + trip.getTripId() + "}");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"data\":\"Failed to create trip\"}");
		}
	}

	@GetMapping("/user/getdetails")
	@ResponseBody
	public User getUserDetails(Authentication auth) {

		User user = userRepository.findByEmail(auth.getName());
		user.setPassword("");
		return user;
	}

	@GetMapping("/user/gettrips")
	@ResponseBody
	public List<Trip> getUserTrips(Authentication auth) {
		User user = userRepository.findByEmail(auth.getName());

		return user.getTripList();
	}

	@GetMapping("/user/matchtrips/{tripId}")
	@ResponseBody
	public List<TripScoreDto> matchUserTrips(@PathVariable Long tripId, Authentication auth) {
		Trip trip = tripService.getTripByTripId(tripId);
		if (!trip.getUser().getEmail().equals(auth.getName()))
			return null;
		return tripService.matchUserTrips(tripId, 0, 10);
	}

	@PostMapping(value = "/user/authenticate", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public ResponseEntity<String> authenticate(@RequestBody User user) {
		log.info("UserResourceImpl : authenticate");
		JSONObject jsonObject = new JSONObject();
		try {
//			log.info(user.getEmail() + " : " + user.getPassword());
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

	@ModelAttribute("user")
	public UserProfileDto userProfileDto() {
		return new UserProfileDto();
	}

	@PostMapping("/user/updatedetails")
	public ResponseEntity updateUserDetails(@RequestBody UserProfileDto userProfileDto, Authentication auth) {
		try {
			userService.update(auth.getName(), userProfileDto);
			return ResponseEntity.status(HttpStatus.OK).body("{\"data\":\"Updated Successfully\"}");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"data\":\"Failed to update\"}");
		}
	}

	@PostMapping("/user/delete")
	public ResponseEntity deleteUser(Authentication auth) {
		try {
			User user = userRepository.findByEmail(auth.getName());
			userRepository.delete(user);
			return ResponseEntity.status(HttpStatus.OK).body("{\"data\":\"User deleted Successfully\"}");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"data\":\"Failed to delete\"}");
		}
	}

	@PostMapping("/user/savephoto")
	public ResponseEntity saveUserPhoto(@RequestParam("image") MultipartFile multipartFile, Authentication auth) {
		try {
			User user = userRepository.findByEmail(auth.getName());
			String fileName = user.getFirstName() + user.getId() + StringUtils.cleanPath(multipartFile.getOriginalFilename());
			String uploadDir = "src/main/resources/imgs/userphotos/";

			Path uploadPath = Paths.get(uploadDir);

			if (!Files.exists(uploadPath)) {
				Files.createDirectories(uploadPath);
			}
			InputStream inputStream = multipartFile.getInputStream();
			Path filePath = uploadPath.resolve(fileName);
			Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
			user.setPhotoPath(fileName);
			userRepository.save(user);
			return ResponseEntity.status(HttpStatus.OK).body("{\"data\":\"Updated Successfully\"}");

		} catch (Exception e) {
			log.error(e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"data\":\"Failed to update\"}");
		}
	}

	@PostMapping("/user/deletetrip/{tripId}")
	@ResponseBody
	public ResponseEntity deleteUserTrip(@PathVariable Long tripId, Authentication auth) {
		try {
			Trip trip = tripService.getTripByTripId(tripId);
			if (!trip.getUser().getEmail().equals(auth.getName()) || !tripService.deleteTrip(trip))
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"data\":\"User Not allowed\"}");
			return ResponseEntity.status(HttpStatus.OK).body("{\"data\":\"Trip deleted Successfully\"}");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"data\":\"Failed to delete\"}");
		}
	}


	@PostMapping("/user/savemessage")
	@ResponseBody
	public ResponseEntity saveMessage(@RequestBody ChatMessageDto chatMessageDto) {
		try {
			chatMessageService.save(chatMessageDto);
			return ResponseEntity.status(HttpStatus.OK).body("{\"data\":\"Message saved successfully\"}");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"data\":\"Failed to save\"}");
		}
	}

	@GetMapping("/user/getchatslist")
	@ResponseBody
	public List<UserProfileDto> getAllMessages(Authentication auth) {
		User user = userRepository.findByEmail(auth.getName());
		return chatMessageService.getOngoingChatUsersList(user);
	}

	@GetMapping("/user/getchats/{otherUserId}")
	@ResponseBody
	public List<ChatMessage> getAllChatsWithUser(@PathVariable Long otherUserId, Authentication auth) {
		User user = userRepository.findByEmail(auth.getName());
		User otherUser = userService.getUserFromId(otherUserId);
		if (otherUser == null)
			return null;
		return chatMessageService.getChatsBetweenUsers(user, otherUser);
	}

}
