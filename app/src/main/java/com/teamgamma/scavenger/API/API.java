package com.teamgamma.scavenger.API;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.teamgamma.scavenger.plant.Plant;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Param on 3/5/2017.
 */

public class API {
    private DatabaseReference reference;

    public API() {
        reference = FirebaseDatabase.getInstance().getReference();
    }

    public DatabaseReference getReference() {
        return reference;
    }

    public void addPlant(Plant newPlant) {
        reference.setValue(newPlant);

    }

    public List<Plant> getPlants(LatLng currLocation) {
        List<Plant> plantList = new ArrayList<Plant>();
        return plantList;
    }
}
