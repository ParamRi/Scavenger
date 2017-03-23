package com.teamgamma.scavenger.API;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.teamgamma.scavenger.plant.Plant;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Param on 3/5/2017.
 */

public class API {
    private static DatabaseReference databaseReference;
    private static StorageReference storageReference;

    public API() {
        databaseReference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();
    }

    public static DatabaseReference getDatabseReference() {
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
    public void addPlant(Plant newPlant) {
        databaseReference.setValue(newPlant);

    }

    public List<Plant> getPlants(LatLng currLocation) {
        List<Plant> plantList = new ArrayList<Plant>();
        return plantList;
    }
}
