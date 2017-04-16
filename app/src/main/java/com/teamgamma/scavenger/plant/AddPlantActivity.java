package com.teamgamma.scavenger.plant;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.teamgamma.scavenger.API.API;
import com.teamgamma.scavenger.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class AddPlantActivity extends AppCompatActivity implements View.OnClickListener, android.location.LocationListener {
    protected LocationManager locationManager;
    protected LocationListener locationListener;
    protected Location location;
    protected Context context;
    private boolean gps_enabled;
    private boolean network_enabled;
    private static final long MIN_TIME_BW_UPDATES = 5000; //5 seconds
    private static final float MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; //10 meters
    private double latitude;
    private double longitude;
    private AutoCompleteTextView plantNameText;
    private EditText plantSciNameText;
    private EditText plantDescText;
    private ImageView plantImage;
    private CheckBox edibilityCheckBox;
    private Button addPlantButton;
    private Button addImageButton;
    private Button selectImageFromGalleryButton;
    private Plant createPlant;
    private static final int CAMERA_REQUEST_CODE = 1;
    private static final int GALLERY_INTENT = 2;
    private ProgressDialog mProgress;
    private ImageView uploadImageView;
    private String downloadUrlString;
    private Uri photoURI;
    private ProgressBar progressBar;
    private boolean jsonLoadedFlag = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mProgress = new ProgressDialog(this);
        //mProgress.setCancelable(false);
        mProgress.setCanceledOnTouchOutside(false);
        mProgress.setMessage("Getting Plant Suggestions for you. Please Wait :) ");
        //mProgress.show();
        final ArrayList<String> commonName =  new ArrayList<String>(50000);
        final HashMap<String, String> commonScientificMap = new HashMap<String, String>(50000);
        final HashMap<String, String> scientificPalatableMap = new HashMap<String, String>(50000);

        try {
            JSONObject obj = new JSONObject(loadJSONFromAsset());
            JSONArray m_jArry = obj.getJSONArray("plantName");
            //ArrayList<HashMap<String, String>> formList = new ArrayList<HashMap<String, String>>();
            //HashMap<String, String> m_li;

            for (int i = 0; i < m_jArry.length(); i++) {
                JSONObject jo_inside = m_jArry.getJSONObject(i);
                Log.d("Details-->", jo_inside.getString("Common Name"));
                String plantCommonName = jo_inside.getString("Common Name");
                String plantScientificName = jo_inside.getString("Scientific Name");
                String palatableHuman = jo_inside.getString("Palatable Human");

                commonName.add(plantCommonName);
                commonScientificMap.put(plantCommonName, plantScientificName);
                scientificPalatableMap.put(plantScientificName, palatableHuman);
            }
            jsonLoadedFlag = true;

        } catch (JSONException e) {
            e.printStackTrace();
        }


        /*
        DatabaseReference database = API.getDatabaseReference();
        database.child("plantName").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Basically, this says "For each DataSnapshot *Data* in dataSnapshot, do what's inside the method.
                //commonName.add("Apple");
                //commonName.add("Banana");

                for (DataSnapshot suggestionSnapshot : dataSnapshot.getChildren()){
                    //Get the suggestion by childing the key of the string you want to get.
                    String plantCommonName = suggestionSnapshot.child("Common Name").getValue(String.class);
                    String plantScientificName = suggestionSnapshot.child("Scientific Name").getValue(String.class);
                    String palatableHuman = suggestionSnapshot.child("Palatable Human").getValue(String.class);
                    //Add the retrieved string to the list
                    commonName.add(plantCommonName);
                    commonScientificMap.put(plantCommonName, plantScientificName);
                    scientificPalatableMap.put(plantScientificName, palatableHuman);
                    Log.d("Suggestion adeed",plantCommonName);
                    mProgress.show();
                }

                mProgress.dismiss();

            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
*/
        setContentView(R.layout.activity_add_plant);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Add Plant");

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        LatLng plantLocation = getIntent().getExtras().getParcelable("LatLng");
        latitude = plantLocation.latitude;
        longitude = plantLocation.longitude;
        plantNameText = (AutoCompleteTextView) findViewById(R.id.plantNameText);
        plantSciNameText = (EditText) findViewById(R.id.sciNameText);
        plantDescText = (EditText) findViewById(R.id.descriptionTextEditor);
        uploadImageView = (ImageView) findViewById(R.id.plantImageView);


        //Create a new ArrayAdapter with your context and the simple layout for the dropdown menu provided by Android
        //final ArrayAdapter<String> autoComplete = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1, COUNTRIES);
        //Child the root before all the push() keys are found and add a ValueEventListener()



        final ArrayAdapter<String> autoComplete = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1, commonName);
        plantNameText.setAdapter(autoComplete);
        plantNameText.setThreshold(1);


        plantNameText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });

        plantSciNameText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });

        plantDescText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });

        edibilityCheckBox = (CheckBox) findViewById(R.id.checkBox);
        addPlantButton = (Button) findViewById(R.id.addPlantButton);
        addImageButton = (Button) findViewById(R.id.addImageButton);
        selectImageFromGalleryButton = (Button) findViewById(R.id.uploadImageButton);
        addImageButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    dispatchTakePictureIntent();
                //}
                   //else{
                    //Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    //startActivityForResult(intent, CAMERA_REQUEST_CODE);
                   //}
            }

        });

        selectImageFromGalleryButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, GALLERY_INTENT);
            }

        });

        plantNameText.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String commonPlantName = (String)adapterView.getItemAtPosition(position);
                String scientificPlantName = commonScientificMap.get(commonPlantName);

                String humanPalatability = scientificPalatableMap.get(scientificPlantName);
                Boolean edibilityCheckBoxValue = false;
                if(humanPalatability.equalsIgnoreCase("yes")){
                    edibilityCheckBoxValue = true;
                }
                plantNameText.setText(commonPlantName);
                plantSciNameText.setText(scientificPlantName);
                edibilityCheckBox.setChecked(edibilityCheckBoxValue);
                InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if(in != null) {
                    in.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
                }

            }
        });

        if(jsonLoadedFlag) {
            plantSciNameText.setEnabled(false);
            edibilityCheckBox.setEnabled(false);
        }

    }

    public String loadJSONFromAsset() {
        String json = null;
        try {

            InputStream is = getAssets().open("PlantNameData.json");

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            json = new String(buffer, "UTF-8");


        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = "";
        if (user != null) {
            userId = user.getUid();
        }
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            mProgress.setMessage("Uploading...");
            mProgress.show();
            //Uri uri = data.getData();
            galleryAddPic();
            if (user != null) {

            }
            StorageReference filepath = API.getStorageReference().child("Photos").child(userId + photoURI.getLastPathSegment());
            filepath.putFile(photoURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(AddPlantActivity.this, "Upload Successful!", Toast.LENGTH_SHORT).show();
                    mProgress.dismiss();
                    @SuppressWarnings("VisibleForTests") Uri downloadUrl_temp = taskSnapshot.getDownloadUrl();
                    downloadUrlString = downloadUrl_temp.toString();

                    //Picasso.with(AddPlantActivity.this).load(downloadUrl_temp).fit().centerCrop().rotate(90).into(uploadImageView);
                    if(downloadUrlString.length() > 0) {
                        Glide.with(AddPlantActivity.this)
                                .load(downloadUrlString)
                                .into(uploadImageView);
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AddPlantActivity.this, "Upload Failed!", Toast.LENGTH_SHORT).show();
                    downloadUrlString = "";
                }
            });

        }

        if (requestCode == GALLERY_INTENT && resultCode == RESULT_OK) {
            mProgress.setMessage("Uploading...");
            mProgress.show();
            Uri uri = null;
            if (data != null) {
                uri = data.getData();
            }

            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                StorageReference filepath = API.getStorageReference().child("Photos").child(userId + photoURI.getLastPathSegment());
                filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(AddPlantActivity.this, "Upload Successful!", Toast.LENGTH_SHORT).show();
                        mProgress.dismiss();
                        @SuppressWarnings("VisibleForTests") Uri downloadUrl_temp = taskSnapshot.getDownloadUrl();
                        downloadUrlString = downloadUrl_temp.toString();
                        //Picasso.with(AddPlantActivity.this).load(downloadUrl_temp).fit().centerCrop().into(uploadImageView);
                        if(downloadUrlString.length() > 0) {
                            Glide.with(AddPlantActivity.this)
                                    .load(downloadUrlString)
                                    .into(uploadImageView);
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddPlantActivity.this, "Adding from Gallery Failed!", Toast.LENGTH_SHORT).show();
                        downloadUrlString = "";
                    }
                });

            }
        }
    }

    /**
     * addPlant() builds and uploads a plant object onto the database
     */
    public void addPlant(View view) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            if (plantNameText.getText().length() == 0) {
                Toast.makeText(AddPlantActivity.this, "Plant Name field cannot be empty", Toast.LENGTH_SHORT).show();

            }
            else if(plantSciNameText.getText().length() == 0){
                Toast.makeText(AddPlantActivity.this, "Scientific Name empty, Please select a common name from the suggestions only", Toast.LENGTH_SHORT).show();
            }
            else if (plantDescText.getText().length() == 0) {
                Toast.makeText(AddPlantActivity.this, "Plant Description field cannot be empty", Toast.LENGTH_SHORT).show();

            } else {
                createPlant = new Plant(plantNameText.getText().toString(), plantSciNameText.getText().toString(),
                        plantDescText.getText().toString(),
                        edibilityCheckBox.isChecked(), false, latitude, longitude,
                        downloadUrlString, user.getUid());
                String plantId = API.getDatabaseReference().child("plants").push().getKey();
                API.getDatabaseReference().child("plants").child(plantId).setValue(createPlant);
                API.getGeoFire().setLocation(plantId, new GeoLocation(latitude, longitude));
                System.out.println("added plant");
                //DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                //mDatabase.child("plants_with_image").push().setValue(createPlant);
                //System.out.println("added plant");
                Toast.makeText(AddPlantActivity.this, "Plant has been added into the databse", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
        else{
            Toast.makeText(AddPlantActivity.this, "You need to be Logged in to add a plant", Toast.LENGTH_SHORT).show();

        }
    }
    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onClick(View view) {
        if (view == addPlantButton) {
            //addPlant();
        } else if (view == addImageButton) {

        }
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

    String mCurrentPhotoPath;


    private File createImageFile() throws IOException {
// Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

// Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;

    }
/*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            uploadImageView.setImageBitmap(imageBitmap);
            galleryAddPic();
        }
    }
*/
    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }



    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
            }
        }
    }
}
