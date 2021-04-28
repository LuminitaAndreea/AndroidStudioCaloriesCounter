package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.DatabaseHandler;
import com.example.Food;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private EditText foodName, calories;
    private Button submit;
    private Button btnCamera;
    private ImageView image;
    private DatabaseHandler dba;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private String currentPhotoPath;

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
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);
        dba = new DatabaseHandler(MainActivity.this);
        foodName = findViewById(R.id.CaloriesFood);
        calories = findViewById(R.id.caloriesNumber);
        submit = findViewById(R.id.submitButton);
        btnCamera = findViewById(R.id.photoButton);
        image = findViewById(R.id.image);

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                        Uri photoURI = FileProvider.getUriForFile(MainActivity.this,
                                "com.example.android.fileprovider",
                                photoFile);
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                    }
                }
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveDataTODB();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            image.setImageBitmap(imageBitmap);
        }
    }

    private void saveDataTODB(){
        Food food = new Food();
        String name = foodName.getText().toString().trim();
        String cal = calories.getText().toString().trim();
        String calInt = cal.toString();

        if(name.equals("") && cal.equals("")){
            Toast.makeText(getApplicationContext(),"Please enter some value",Toast.LENGTH_LONG).show();
        }else{
            food.setFoodName(name);
            food.setCalories(calInt);

            dba.addFood(food);
            dba.close();

            foodName.setText("");
            calories.setText("");
            Toast.makeText(getApplicationContext(),"com.example.Food " + name + ", Calories" + calInt, Toast.LENGTH_LONG).show();
            startActivity((new Intent(MainActivity.this, DetailFoodActivity.class)));
        }
    }
}