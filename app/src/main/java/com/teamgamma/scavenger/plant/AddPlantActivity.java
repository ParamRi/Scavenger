package com.teamgamma.scavenger.plant;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.geofire.GeoLocation;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.teamgamma.scavenger.API.API;
import com.teamgamma.scavenger.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
    private EditText plantNameText;
    private EditText plantSciNameText;
    private EditText plantDescText;
    private ImageView plantImage;
    private CheckBox edibilityCheckBox;
    private Button addPlantButton;
    private Button addImageButton;
    private Plant createPlant;
    private static final int CAMERA_REQUEST_CODE = 1;
    private ProgressDialog mProgress;
    private ImageView uploadImageView;
    private String downloadUrlString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_plant);
        mProgress = new ProgressDialog(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Add Plant");

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
/*
        if (gps_enabled) {
            if (location == null) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                locationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        MIN_TIME_BW_UPDATES,
                        MIN_DISTANCE_CHANGE_FOR_UPDATES, (android.location.LocationListener) this);
                Log.d("activity", "RLOC: GPS Enabled");
                if (locationManager != null) {
                    location = locationManager
                            .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if (location != null) {
                        Log.d("activity", "RLOC: loc by GPS");

                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                    }
                }
            }
        }
        */
        LatLng plantLocation = getIntent().getExtras().getParcelable("LatLng");
        latitude = plantLocation.latitude;
        longitude = plantLocation.longitude;
        plantNameText = (EditText) findViewById(R.id.plantNameText);
        plantSciNameText = (EditText) findViewById(R.id.sciNameText);
        plantDescText = (EditText) findViewById(R.id.descriptionTextEditor);
        uploadImageView = (ImageView) findViewById(R.id.imageView3);

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
        addImageButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    dispatchTakePictureIntent();
                }
                    else{
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, CAMERA_REQUEST_CODE);
                    }



            }

        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            uploadImageView.setImageBitmap(imageBitmap);
        }
        */
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {

            mProgress.setMessage("Uploading Image...");
            mProgress.show();
            Uri uri = data.getData();

            StorageReference filepath = API.getStorageReference().child("Photos").child(uri.getLastPathSegment());
            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    mProgress.dismiss();

                    Toast.makeText(AddPlantActivity.this, "Upload Successful!", Toast.LENGTH_SHORT).show();

                    @SuppressWarnings("VisibleForTests") Uri downloadUrl_temp = taskSnapshot.getDownloadUrl();
                    downloadUrlString = downloadUrl_temp.toString();
                    Picasso.with(AddPlantActivity.this).load(downloadUrl_temp).fit().centerCrop().into(uploadImageView);



                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AddPlantActivity.this, "Upload Failed!", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    /**
     * addPlant() builds and uploads a plant object onto the database
     */
    public void addPlant(View view) {
        createPlant = new Plant(plantNameText.getText().toString(), plantSciNameText.getText().toString(),
                plantDescText.getText().toString(),
                edibilityCheckBox.isChecked(), false, latitude, longitude, downloadUrlString);
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
    Uri photoUri;

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

    static final int REQUEST_TAKE_PHOTO = 1;

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
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }
}
