package com.teamgamma.scavenger;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.teamgamma.scavenger.plant.Plant;


public class TabFragment2 extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Intent intent = new Intent(getActivity(), PlantListActivity.class);
        startActivity(intent);


        return null;
    }


}
