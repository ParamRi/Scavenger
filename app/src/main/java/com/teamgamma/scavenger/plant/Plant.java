package com.teamgamma.scavenger.plant;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Plant Object containing all plant information
 */
public class Plant implements Parcelable{

    private String plantName;
    private String sciName;
    private String description;
    private boolean isEdible;
    private boolean isVerified;
    private Double latitude;
    private Double longitude;
    private String downloadUrlString;

    public Plant() {
        plantName = "";
        sciName = "";
        description = "";
    }

    public Plant(String plantName, String sciName, String description, boolean checked,
                 boolean isVerified, com.google.android.gms.maps.model.LatLng latLng,
                 String downloadUrlString) {

    }

    public Plant(String plantName, String sciName, String description, boolean isEdible,
                     boolean isVerified, double latitude, double longitude, String downloadUrlString) {
        this.plantName = plantName;
        this.sciName = sciName;
        this.description = description;
        this.isEdible = isEdible;
        this.isVerified = isVerified;
        this.latitude = latitude;
        this.longitude = longitude;
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

    public void setLatitude(double lat) {
        this.latitude = lat;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
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

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getDownloadUrlString() { return downloadUrlString; }

    public Plant(Parcel in) {
        plantName = in.readString();
        sciName = in.readString();
        description = in.readString();
        isEdible = ( in.readInt() == 1 );
        isVerified = (in.readInt() == 1);
        latitude = in.readDouble();
        longitude = in.readDouble();
        downloadUrlString = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(plantName);
        parcel.writeString(sciName);
        parcel.writeString(description);
        parcel.writeInt((isEdible) ? 1 : 0);
        parcel.writeInt((isVerified) ? 1 : 0);
        parcel.writeDouble(latitude);
        parcel.writeDouble(longitude);
        parcel.writeString(downloadUrlString);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {

        @Override
        public Plant createFromParcel(Parcel parcel) {
            return new Plant(parcel);
        }

        @Override
        public Object[] newArray(int i) {
            return new Object[0];
        }
    };
}
