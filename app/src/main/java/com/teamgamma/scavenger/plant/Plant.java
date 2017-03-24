package com.teamgamma.scavenger.plant;

import com.google.android.gms.maps.model.LatLng;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class Plant {

    private String plantName;
    private String sciName;
    private String description;
    private boolean isEdible;
    private boolean isVerified;
    private LatLng location;
    private String downloadUrlString;

    public Plant() {

    }

    public Plant(String plantName, String sciName, String description, boolean isEdible,
                     boolean isVerified, LatLng location, String downloadUrlString) {
        this.plantName = plantName;
        this.sciName = sciName;
        this.description = description;
        this.isEdible = isEdible;
        this.isVerified = isVerified;
        this.location = location;
        this.downloadUrlString = downloadUrlString;
    }

    public void setPlantName(String plantName) {
        this.plantName = plantName;
    }

    public void setSciName(String sciName) {
        this.sciName = sciName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setEdibility(boolean isEdible){
        this.isEdible = isEdible;
    }

    public void setVerified(boolean isVerified) {
        this.isVerified = isVerified;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public void setDownloadUrlString(String downloadUrlString) {
        this.downloadUrlString = downloadUrlString;
    }

    public String getPlantName() {
        return plantName;
    }

    public String getSciName() {
        return sciName;
    }

    public String getDescription() {
        return description;
    }

    public boolean isEdible(){
        return isEdible;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public LatLng getLocation() {
        return location;
    }

    public String getDownloadUrlString() { return downloadUrlString; }
}
