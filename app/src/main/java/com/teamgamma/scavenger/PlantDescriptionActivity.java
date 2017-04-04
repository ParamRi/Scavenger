package com.teamgamma.scavenger;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.teamgamma.scavenger.API.API;
import com.teamgamma.scavenger.plant.Plant;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by jayakrishna on 3/3/2017.
 */

public class PlantDescriptionActivity extends AppCompatActivity{

    private Plant plantInfo;
    private TextView plantNameText, sciNameText, descText;
    private ImageView plantImage;
    private Button addImageButton, findPlantButton;
    private ProgressDialog mProgress;
    private String imageURI;
    private Uri photoURI;
    private static final int CAMERA_REQUEST_CODE = 1;
    private String downloadUrlString;
    private ImageView uploadImageView;
    private String plantDescKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle b = getIntent().getExtras();
        plantInfo = b.getParcelable("Plant Info");
        plantDescKey = b.getString("key");

        mProgress = new ProgressDialog(this);

        // set the view now
        setContentView(R.layout.activity_plant_description);

        plantNameText = (TextView) findViewById(R.id.textView3);
        plantNameText.setText(plantInfo.getPlantName());
        sciNameText = (TextView) findViewById(R.id.textView4);
        sciNameText.setText(plantInfo.getSciName());
        descText = (TextView) findViewById(R.id.textView5);
        descText.setText(plantInfo.getDescription());
        plantImage = (ImageView) findViewById(R.id.imageView);
        addImageButton = (Button) findViewById(R.id.button3);
        findPlantButton = (Button) findViewById(R.id.findPlantButton);

        imageURI = plantInfo.getDownloadUrlString();

        addImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(imageURI.length() > 0) {
                    Toast.makeText(PlantDescriptionActivity.this, "Image already exists!", Toast.LENGTH_SHORT).show();
                } else {
                    dispatchTakePictureIntent();

                }
            }
        });

        if(imageURI.length() > 0) {
            Glide.with(this)
                    .load(imageURI)
                    .into(plantImage);
        }

        findPlantButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            mProgress.setMessage("Uploading...");
            mProgress.show();
            //Uri uri = data.getData();
            galleryAddPic();
            StorageReference filepath = API.getStorageReference().child("Photos").child(photoURI.getLastPathSegment());
            filepath.putFile(photoURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(PlantDescriptionActivity.this, "Upload Successful!", Toast.LENGTH_SHORT).show();
                    mProgress.dismiss();
                    @SuppressWarnings("VisibleForTests") Uri downloadUrl_temp = taskSnapshot.getDownloadUrl();
                    downloadUrlString = downloadUrl_temp.toString();
                    plantInfo.setDownloadUrlString(downloadUrlString);
                    Picasso.with(PlantDescriptionActivity.this).load(downloadUrl_temp).fit().centerCrop().into(plantImage);
                    //update plant;
                    API.getDatabaseReference().child("plants").child(plantDescKey).setValue(plantInfo);
                    Toast.makeText(PlantDescriptionActivity.this, "Plant has been updated", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(PlantDescriptionActivity.this, "Upload Failed!", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

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
