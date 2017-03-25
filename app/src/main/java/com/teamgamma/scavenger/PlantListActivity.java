package com.teamgamma.scavenger;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.teamgamma.scavenger.plant.Plant;

import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 */
public class PlantListActivity extends ListActivity {

    private List<Plant> plantList;

    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        plantList = getIntent().getExtras().getParcelableArrayList("PlantList");

        String[] values = new String[plantList.size()];
        for(int i = 0; i < plantList.size(); i++) {
            values[i] = plantList.get(i).getPlantName();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, values);
        setListAdapter(adapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        String item = (String) getListAdapter().getItem(position);
        Toast.makeText(this, item + " selected", Toast.LENGTH_LONG).show();
    }
}
