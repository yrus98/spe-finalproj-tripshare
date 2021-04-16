package com.speproject.tripshare.web.dto;

import com.speproject.tripshare.model.Trip;

import java.sql.Date;

public class TripScoreDto {
    private Trip trip;

    private Float matchScore;

    public TripScoreDto(){

    }

    public TripScoreDto(Trip trip, Float matchScore){
        this.trip = trip;
        this.matchScore = matchScore;
    }

    public Trip getTrip() {
        return trip;
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
    }

    public Float getMatchScore() {
        return matchScore;
    }

    public void setMatchScore(Float matchScore) {
        this.matchScore = matchScore;
    }
}
