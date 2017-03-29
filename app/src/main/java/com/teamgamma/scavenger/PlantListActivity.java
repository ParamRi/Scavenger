package com.teamgamma.scavenger;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.teamgamma.scavenger.plant.Plant;
import com.teamgamma.scavenger.plant.PlantList;

import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 */
public class PlantListActivity extends Activity {

    private List<Plant> plantList;
    private ListView list;
    private String[] values;
    private String[] images;

    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.plant_list);

        plantList = getIntent().getExtras().getParcelableArrayList("PlantList");

        values = new String[plantList.size()];
        images = new String[plantList.size()];
        for(int i = 0; i < plantList.size(); i++) {
            if(null != plantList.get(i).getPlantName() ) {
                values[i] = plantList.get(i).getPlantName();
            } else {
                values[i] = "no name available";
            }
            if(plantList.get(i).getDownloadUrlString() != null) {
                images[i] = plantList.get(i).getDownloadUrlString();
            }
        }
        PlantList adapter = new PlantList(PlantListActivity.this,
                values, images);
        list = (ListView) findViewById(R.id.list);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Toast.makeText(PlantListActivity.this, "You Clicked at " + values[+ position], Toast.LENGTH_SHORT).show();

            }
        });
    }
}
