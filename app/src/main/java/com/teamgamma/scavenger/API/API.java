package com.teamgamma.scavenger.API;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.teamgamma.scavenger.plant.Plant;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Param on 3/5/2017.
 */

public class API {
    private static DatabaseReference reference;
    private static GeoFire mGeoFire;

    public API() {
        reference = FirebaseDatabase.getInstance().getReference();
    }

    public static DatabaseReference getReference() {
        if (null == reference) {
            reference = FirebaseDatabase.getInstance().getReference();
        }
        return reference;
    }

    public static GeoFire getGeoFire() {
        if(null == mGeoFire) {
            if(null == reference) {
                reference = FirebaseDatabase.getInstance().getReference();
            }
            mGeoFire = new GeoFire(reference.child("plant_location"));
        }
        return mGeoFire;
    }

    public void addPlant(Plant newPlant, GeoLocation plantLocation) {
        String plantId = reference.child("plants").push().getKey();
        reference.child("plants").child(plantId).setValue(newPlant);
        mGeoFire.setLocation(plantId, plantLocation);
    }

    public static Map<String, GeoLocation> getPlants(LatLng currLocation, int radius) {
        final Map<String, GeoLocation> plantList = new Map<String, GeoLocation>();
        mGeoFire = new GeoFire(reference.child("plant_location"));
        GeoQuery getPlants = mGeoFire.queryAtLocation(new GeoLocation(currLocation.latitude, currLocation.longitude), radius);
        getPlants.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {
                plantList.add(key,location);
            }

            @Override
            public void onKeyExited(String key) {

            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {

            }

            @Override
            public void onGeoQueryReady() {

            }

            @Override
            public void onGeoQueryError(DatabaseError error) {

            }
        });
        return plantList;
    }

    private static Plant getPlant(String key) {

    }
}
