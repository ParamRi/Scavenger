package com.teamgamma.scavenger;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

/**
 * A fragment representing a list of Items.
 * <p/>
 */
public class PlantListActivity extends ListActivity {

    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        String[] values = new String[] { "Plant 1", "Plant 2", "Plant 3",
                "Plant 4", "Plant 5", "Plant 6", "Plant 7", "Plant 8",
                "Plant 9", "Plant 10" };
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
