package com.example.demosqlite.models;

import java.util.UUID;

public class TripModel {

    //region $Private Properties
    private UUID tripId;
    private String tripName;
    private String tripDest;
    private String StartDateTrip;
    private String EndDateTrip;
    private boolean isRiskAssessmentChecked;
    private String tripDesc;

    //endregion

    //region $Getters and Setters
    public UUID gTripId() {
        return tripId;
    }

    public void sTripId(UUID tripId) {
        this.tripId = tripId;
    }

    public String gTripName() {
        return tripName;
    }

    public void sTripName(String tripName) {
        this.tripName = tripName;
    }

    public String gTripDest() {
        return tripDest;
    }

    public void sTripDest(String tripDest) {
        this.tripDest = tripDest;
    }

    public String gStartDateTrip() {
        return StartDateTrip;
    }

    public void sStartDateTrip(String startDateTrip) {
        this.StartDateTrip = startDateTrip;
    }

    public boolean isRiskAssessmentChecked() {
        return isRiskAssessmentChecked;
    }

    public void setRiskAssessmentChecked(boolean riskAssessmentChecked) {
        isRiskAssessmentChecked = riskAssessmentChecked;
    }

    public String gTripDesc() {
        return tripDesc;
    }

    public void sTripDesc(String tripDesc) {
        this.tripDesc = tripDesc;
    }

    public String gEndDateTrip() {
        return EndDateTrip;
    }

    public void sEndDateTrip(String endDateTrip) {
        this.EndDateTrip = endDateTrip;
    }



    //endregion

    //region $Constructor
    public TripModel(UUID tripID, String tripName, String tripDest, String tripStartDate, String tripEndDate, boolean isRiskAssessmentChecked, String tripDesc, byte[] tripImage) {
        this.tripId = tripID;
        this.tripName = tripName;
        this.tripDest = tripDest;
        this.StartDateTrip = tripStartDate;
        this.EndDateTrip = tripEndDate;
        this.isRiskAssessmentChecked = isRiskAssessmentChecked;
        this.tripDesc = tripDesc;
    }

    //endregion
}
