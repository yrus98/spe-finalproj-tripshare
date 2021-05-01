package com.speproject.tripshare.service;

import com.speproject.tripshare.model.Trip;
import com.speproject.tripshare.model.User;
import com.speproject.tripshare.repository.TripRepository;
import com.speproject.tripshare.repository.UserRepository;
import com.speproject.tripshare.web.dto.TripCreationDto;
import com.speproject.tripshare.web.dto.TripScoreDto;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class TripServiceImpl implements TripService{

	private TripRepository tripRepository;
	private UserRepository userRepository;


	public TripServiceImpl(TripRepository tripRepository, UserRepository userRepository) {
		super();
		this.tripRepository = tripRepository;
		this.userRepository = userRepository;
	}

	@Override
	public Trip save(TripCreationDto tripCreationDto) {
		String useremail = SecurityContextHolder.getContext().getAuthentication().getName();
		User creatingUser = userRepository.findByEmail(useremail);
		Trip trip = new Trip(creatingUser, tripCreationDto.getDestination(), tripCreationDto.getLandscape(), tripCreationDto.getTripDate(), tripCreationDto.getTripLength(), tripCreationDto.getTripBudget(), tripCreationDto.getGroupSize(), tripCreationDto.getExpectedAgeGroup(), tripCreationDto.getExpectedGender(), tripCreationDto.getDescription(), new Timestamp(new java.util.Date().getTime()));

		return tripRepository.save(trip);
	}

	@Override
	public Trip getTripByTripId(Long tripId){
		return tripRepository.findByTripId(tripId);
	}

	@Override
	public List<TripScoreDto> matchUserTrips(Long tripId, int startCount, int resultCount){

		Trip userTrip = tripRepository.findByTripId(tripId);
		List<Trip> trips =  tripRepository.findAll();
		Map<Long, Float> mapTripIdToScore = new LinkedHashMap<>();
		float score;
		for (Trip trip:
			 trips) {
			if(!trip.getUser().getId().equals(userTrip.getUser().getId())) {
				score = matchScore(userTrip, trip);
				mapTripIdToScore.put(trip.getTripId(), score);
			}
		}
		Map<Long, Float> sortedMapTripIdToScore = new LinkedHashMap<>();

		mapTripIdToScore.entrySet().stream()
				.sorted(Map.Entry.comparingByValue())
				.forEach(k -> sortedMapTripIdToScore.put(k.getKey() , -k.getValue()));

//		List<Map.Entry<Long, Float>> list = new ArrayList<>(mapTripIdToScore.entrySet());
//		list.sort(Map.Entry.comparingByValue());
//		for (Map.Entry<Long, Float> entry : list) {
//			sortedMapTripIdToScore.put(entry.getKey(), entry.getValue());
//		}

		List<TripScoreDto> tripsWithScores = new ArrayList<>();
		int i = 0;
		for (Map.Entry<Long, Float> entry :
				sortedMapTripIdToScore.entrySet()) {
			if (i < startCount){
				i++;
				continue;
			}
			Trip t = tripRepository.findByTripId(entry.getKey());
			t.getUser().setPassword("");
			tripsWithScores.add(new TripScoreDto(t, entry.getValue()));
			if(i++ >= resultCount)
				break;
		}

		return tripsWithScores;
	}

    @Override
    public Boolean deleteTrip(Trip trip) {
        try {
			tripRepository.delete(trip);
			return true;
		}catch(Exception e){
        	return false;
		}

    }

    float matchScore(Trip userTrip, Trip otherTrip){
		float score = 0f, s;
		float slopeLandscape = 0.5f, slopeDate=0.2f, slopeTripLength=0.4f, slopeBudget = 0.001f, slopeAgeGroup = 2f;
		float wLandscape = 0.8f, wDate = 0.7f, wTripLength = 0.5f, wBudget = 0.9f, wAge = 0.5f, wGender = 0.6f;

		if(userTrip.getLandscape()==0 || (userTrip.getLandscape() == otherTrip.getLandscape())) s = 10;
		else if(otherTrip.getLandscape() == 0) s = 8;
		else s = 2;
		score += s * wLandscape;

		long diffInMillies = Math.abs(otherTrip.getTripDate().getTime() - userTrip.getTripDate().getTime());
		long diffInDays = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
		s = Math.max(1, 10 - slopeDate * diffInDays);
		score += s * wDate;

		s = Math.max(1, 10 - slopeTripLength * Math.abs(userTrip.getTripLength() - otherTrip.getTripLength()));
		score += s * wTripLength;

		s = Math.max(1, 10 - slopeBudget * Math.abs(userTrip.getTripBudget() - otherTrip.getTripBudget()));
		score += s * wBudget;

		if(userTrip.getExpectedAgeGroup()==0) s = 10;
		else if(otherTrip.getExpectedAgeGroup() == 0) s = 8;
		else s = 10 - slopeAgeGroup * Math.abs(userTrip.getExpectedAgeGroup() - otherTrip.getExpectedAgeGroup());
		score += s * wAge;

		if(userTrip.getExpectedGender() == 0) s = 10;
		else if(otherTrip.getExpectedGender() == 0) s = 6;
		else s = 2;
		score += s * wGender;

		//total score out of 40 : 10 * (sum of weight parameters)
		//score will be negative for ascending sort
		return -score;
	}


}
