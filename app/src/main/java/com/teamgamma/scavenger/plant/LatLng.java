package com.teamgamma.scavenger.plant;

/**
 * Created by Param on 3/24/2017.
 */

public class LatLng {
    private Double latitude;
    private Double longitude;

    public LatLng() {}

    public LatLng(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Double getLatitude(){
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }
}
