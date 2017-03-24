package com.teamgamma.scavenger.API;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.teamgamma.scavenger.plant.Plant;

/**
 * Created by Param on 3/5/2017.
 */

public class API {
    private static DatabaseReference reference;
    private static GeoFire mGeoFire;
    private static DatabaseReference databaseReference;
    private static StorageReference storageReference;

    public API() {
        databaseReference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();
    }

    public static DatabaseReference getDatabaseReference() {
        if (null == databaseReference) {
            databaseReference = FirebaseDatabase.getInstance().getReference();
        }
        return databaseReference;
    }
    public static StorageReference getStorageReference() {
        if (null == storageReference) {
            storageReference = FirebaseStorage.getInstance().getReference();
        }
        return storageReference;
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

}
