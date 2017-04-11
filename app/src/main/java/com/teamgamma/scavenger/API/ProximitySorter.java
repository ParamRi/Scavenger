package com.teamgamma.scavenger.API;

import com.google.android.gms.maps.model.LatLng;
import com.teamgamma.scavenger.plant.Plant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Param on 4/5/2017.
 */

public class ProximitySorter {
    private double userLat;
    private double userLong;
    private List<Plant> plantList;
    private HashMap<Plant, Double> hashDistance;

    public ProximitySorter() {}

    public ProximitySorter(List<Plant> plantList, LatLng userLocation) {
        userLat = userLocation.latitude;
        userLong = userLocation.longitude;
        this.plantList = plantList;
        hashDistance = new HashMap<>();
    }

    public ArrayList<Plant> sortByDistance() {
        for(int i = 0; i < plantList.size(); i++) {
            Plant plant = plantList.get(i);
            double distLat = Math.abs(userLat - plant.getLatitude());
            double distLong = Math.abs(userLong - plant.getLongitude());
            double hypotenuse = Math.sqrt(Math.pow(distLat, 2) + Math.pow(distLong,2));
            hashDistance.put(plant, hypotenuse);
        }

        List<HashMap.Entry<Plant, Double>> list = new LinkedList<HashMap.Entry<Plant, Double>>(hashDistance.entrySet());
        Collections.sort(list, new Comparator<HashMap.Entry<Plant, Double>>() {
            @Override
            public int compare(HashMap.Entry<Plant, Double> o1, HashMap.Entry<Plant, Double> o2) {
                return (o1.getValue()).compareTo(o2.getValue());
            }
        });
        ArrayList<Plant> result = new ArrayList<Plant>();
        for(HashMap.Entry<Plant, Double> entry : list) {
            result.add(entry.getKey());
        }
        return result;
    }
}
