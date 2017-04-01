package com.teamgamma.scavenger.plant;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.teamgamma.scavenger.R;

/**
 * Created by Param on 3/28/2017.
 */

public class PlantList extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] plantNames;
    private final String[] imageId;
    private final String[] desclist;
    public PlantList(Activity context,
                      String[] plantNames, String[] imageId, String[] desclist) {
        super(context, R.layout.fragment_plant, plantNames);
        this.context = context;
        this.plantNames = plantNames;
        this.imageId = imageId;
        this.desclist = desclist;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.fragment_plant, null, true);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.plantName);
        TextView txtDesc = (TextView) rowView.findViewById(R.id.plantDesc);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        txtTitle.setText(plantNames[position]);
        txtDesc.setText(desclist[position]);

        if(imageId[position] != null){
            Glide.with(this.getContext())
                    .load(imageId[position])
                    .into(imageView);
        }

        return rowView;
    }
}