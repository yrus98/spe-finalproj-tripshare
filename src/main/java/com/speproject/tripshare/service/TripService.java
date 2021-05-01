package com.speproject.tripshare.service;

import com.speproject.tripshare.model.Trip;
import com.speproject.tripshare.web.dto.TripCreationDto;
import com.speproject.tripshare.web.dto.TripScoreDto;

import java.util.List;

public interface TripService {
	Trip save(TripCreationDto tripCreationDto);

	Trip getTripByTripId(Long tripId);

	List<TripScoreDto> matchUserTrips(Long tripId, int startCount, int resultCount);

	Boolean deleteTrip(Trip trip);
}
