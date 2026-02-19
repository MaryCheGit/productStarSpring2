package com.projecr.mariya.productStarSpring2.model;

import java.time.Duration;

public class Result {
    private String fullName;
    private String gender;
    private String distance;
    private String timeString;
    private Duration timeInSeconds;

    public Result(String fullName, String gender, String distance, String timeString) {
        this.fullName = fullName;
        this.gender = gender;
        this.distance = distance;
        this.timeString = timeString;
        this.timeInSeconds = parseTime(timeString);
    }

    private Duration parseTime(String time) {
        String[] parts = time.split(":");
        if (parts.length == 2) {
            return Duration.ofMinutes(Long.parseLong(parts[0]))
                    .plusSeconds(Long.parseLong(parts[1]));
        }
        return Duration.ZERO;
    }

    public String getFullName() { return fullName; }
    public String getGender() { return gender; }
    public String getDistance() { return distance; }
    public String getTimeString() { return timeString; }
    public Duration getTimeInSeconds() { return timeInSeconds; }
    
    @Override
    public String toString() {
        return String.format("%s (%s, %s) - %s", fullName, gender, distance, timeString);
    }
}