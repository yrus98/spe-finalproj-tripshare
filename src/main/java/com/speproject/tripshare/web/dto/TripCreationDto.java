package com.speproject.tripshare.web.dto;

import java.sql.Date;
import java.sql.Timestamp;

public class TripCreationDto {
    private String destination;

    private Integer landscape; //0 - Any, 1 - Mountain/hills, 2 - Beach, 3 - Snowy region, 4 - Other

    private Date tripDate;

    private Integer tripLength;

    private Integer tripBudget;

    private Integer groupSize;

    private Integer expectedAgeGroup; //0 - Any, 1 - upto 20, 2 - 20 to 30, 3 - 30 to 40, 4 - 40 to 50, 5 - 50+

    private Integer expectedGender; //0 - Any, 1 - Male, 2 - Female

    private String description;

    public TripCreationDto(){

    }

    public TripCreationDto(String destination, Integer landscape, Date tripDate, Integer tripLength, Integer tripBudget, Integer groupSize, Integer expectedAgeGroup, Integer expectedGender, String description) {
        super();
        this.destination = destination;
        this.landscape = landscape;
        this.tripDate = tripDate;
        this.tripLength = tripLength;
        this.tripBudget = tripBudget;
        this.groupSize = groupSize;
        this.expectedAgeGroup = expectedAgeGroup;
        this.expectedGender = expectedGender;
        this.description = description;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public Integer getLandscape() {
        return landscape;
    }

    public void setLandscape(Integer landscape) {
        this.landscape = landscape;
    }

    public Date getTripDate() {
        return tripDate;
    }

    public void setTripDate(Date tripDate) {
        this.tripDate = tripDate;
    }

    public Integer getTripLength() {
        return tripLength;
    }

    public void setTripLength(Integer tripLength) {
        this.tripLength = tripLength;
    }

    public Integer getTripBudget() {
        return tripBudget;
    }

    public void setTripBudget(Integer tripBudget) {
        this.tripBudget = tripBudget;
    }

    public Integer getGroupSize() {
        return groupSize;
    }

    public void setGroupSize(Integer groupSize) {
        this.groupSize = groupSize;
    }

    public Integer getExpectedAgeGroup() {
        return expectedAgeGroup;
    }

    public void setExpectedAgeGroup(Integer expectedAgeGroup) {
        this.expectedAgeGroup = expectedAgeGroup;
    }

    public Integer getExpectedGender() {
        return expectedGender;
    }

    public void setExpectedGender(Integer expectedGender) {
        this.expectedGender = expectedGender;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


}
