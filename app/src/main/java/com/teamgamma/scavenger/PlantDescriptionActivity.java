package com.teamgamma.scavenger;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.teamgamma.scavenger.plant.Plant;

/**
 * Created by jayakrishna on 3/3/2017.
 */

public class PlantDescriptionActivity extends AppCompatActivity{

    private Plant plantInfo;
    private TextView plantNameText, sciNameText, descText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle b = getIntent().getExtras();
        plantInfo = b.getParcelable("Plant Info");
        //Get Firebase auth instance

        // set the view now
        setContentView(R.layout.activity_plant_description);

        plantNameText = (TextView) findViewById(R.id.textView3);
        plantNameText.setText(plantInfo.getPlantName());
        sciNameText = (TextView) findViewById(R.id.textView4);
        sciNameText.setText(plantInfo.getSciName());
        descText = (TextView) findViewById(R.id.textView5);
        descText.setText(plantInfo.getDescription());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Plant Description");
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                //NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
