package com.teamgamma.scavenger;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.model.CameraPosition;
import com.teamgamma.scavenger.API.ProximitySorter;
import com.teamgamma.scavenger.plant.Plant;

import java.util.ArrayList;


public class TabFragment2 extends Fragment {


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tab_fragment_2, container, false);

    }
}