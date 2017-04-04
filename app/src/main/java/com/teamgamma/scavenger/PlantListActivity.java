package com.teamgamma.scavenger;

import android.app.Activity;
import android.content.Intent;
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
    private String[] descList;

    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.plant_list);

        plantList = getIntent().getExtras().getParcelableArrayList("PlantList");

        int arraySize = plantList.size();
        values = new String[arraySize];
        images = new String[arraySize];
        descList = new String[arraySize];
        for(int i = 0; i < arraySize; i++) {
            Plant aPlant = plantList.get(i);
            if(null != aPlant.getPlantName() ) {
                values[i] = aPlant.getPlantName();
            } else {
                values[i] = "no name available";
            }
            descList[i] = aPlant.getDescription();
            if(plantList.get(i).getDownloadUrlString() != null) {
                images[i] = plantList.get(i).getDownloadUrlString();
            }
        }
        PlantList adapter = new PlantList(PlantListActivity.this,
                values, images, descList);
        list = (ListView) findViewById(R.id.list);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Toast.makeText(PlantListActivity.this, "You Clicked at " + values[+ position], Toast.LENGTH_SHORT).show();
                Plant plantInfo = plantList.get(position);
                if((plantInfo.getLatitude() >= -90 || plantInfo.getLatitude() <= 90)
                        && (plantInfo.getLongitude() >= -180 || plantInfo.getLongitude() <= 180)) {
                    Intent i = new Intent();
                    i.putExtra("latitude", plantInfo.getLatitude());
                    i.putExtra("longitude", plantInfo.getLongitude());
                    setResult(RESULT_OK, i);
                    finish();
                }
            }
        });
    }
}
