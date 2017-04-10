package com.teamgamma.scavenger.plant;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Plant Object containing all plant information
 */
public class Plant implements Parcelable{

    private String plantName;
    private String sciName;
    private String desc;
    private boolean edible;
    private boolean verified;
    private Double latitude;
    private Double longitude;
    private String imgurl;
    private String userId;

    public Plant() {
        plantName = "";
        sciName = "";
        desc = "";
        imgurl = "";
        userId = "";

    }

    public Plant(String plantName, String sciName, String description, boolean isEdible,
                     boolean isVerified, double latitude, double longitude, String imgurl, String userId) {
        this.plantName = plantName;
        this.sciName = sciName;
        this.desc = description;
        this.edible = isEdible;
        this.verified = isVerified;
        this.latitude = latitude;
        this.longitude = longitude;
        this.imgurl = imgurl;
        this.userId = userId;

    }

    public void setPlantName(String plantName) {
        this.plantName = plantName;
    }

    public void setSciName(String sciName) {
        this.sciName = sciName;
    }

    public void setDesc(String description) {
        this.desc = description;
    }

    public void setEdibility(boolean isEdible){
        this.edible = isEdible;
    }

    public void setVerified(boolean isVerified) {
        this.verified = isVerified;
    }

    public void setLatitude(double lat) {
        this.latitude = lat;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPlantName() {
        return plantName;
    }

    public String getSciName() {
        return sciName;
    }

    public String getDesc() {
        return desc;
    }

    public boolean isEdible(){
        return edible;
    }

    public boolean isVerified() {
        return verified;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getImgurl() { return imgurl; }

    public String getUserId() {
        return userId;
    }

    public Plant(Parcel in) {
        plantName = in.readString();
        sciName = in.readString();
        desc = in.readString();
        edible = ( in.readInt() == 1 );
        verified = (in.readInt() == 1);
        latitude = in.readDouble();
        longitude = in.readDouble();
        imgurl = in.readString();
        userId = in.readString();

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(plantName);
        parcel.writeString(sciName);
        parcel.writeString(desc);
        parcel.writeInt((edible) ? 1 : 0);
        parcel.writeInt((verified) ? 1 : 0);
        parcel.writeDouble(latitude == null ? 0 : latitude);
        parcel.writeDouble(longitude == null ? 0 : longitude);
        parcel.writeString(imgurl);
        parcel.writeString(userId);

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
