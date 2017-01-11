package com.example.pial_pc.instantbookreview;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Pial-PC on 12/17/2015.
 */
public class HomeFragment extends Fragment {
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final String JPEG_FILE_PREFIX = "IBR_";
    private static final String JPEG_FILE_SUFFIX = ".jpg";
    File photoFile = null;
    ImageButton camera_button;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        camera_button = (ImageButton) rootView.findViewById(R.id.camera);
        camera_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final InternetConnection internet = new InternetConnection();
                if (internet.Checking_Internet_Connection(getActivity()) == true) {
                    Intent intent = new Intent(getActivity(), ImageCapture.class);
                    startActivity(intent);
                } else if (internet.Checking_Internet_Connection(getActivity()) == false) {
                    //Showing AlertDialog if Internet Connection is off
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity());
                    alertBuilder.setTitle("Warning");
                    alertBuilder.setCancelable(false);
                    alertBuilder.setIcon(R.drawable.error_icon);
                    alertBuilder.setMessage("Internet connection is turned off. Please turn on your internet connection to continue.");
                    alertBuilder.setPositiveButton("Turn on", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));

                        }
                    });
                    alertBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });

                    AlertDialog dialog = alertBuilder.create();
                    dialog.show();
                }

                /*Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                // Ensure that there's a camera activity to handle the intent
                if (cameraIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                    // Create the File where the photo should go
                    try {
                        photoFile = createImageFile();
                    } catch (IOException ex) {
                        // Error occurred while creating the File
                        Log.i("File error", "File");
                    }
                }
                // Continue only if the File was successfully created
                if (photoFile != null) {
                    Uri fileUri = Uri.fromFile(photoFile);
                    cameraIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT,
                            fileUri);
                    Toast.makeText(getActivity(), photoFile.toString(), Toast.LENGTH_SHORT).show();
                    startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
                }*/
            }
        });

        return rootView;
    }

    /*@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {

            *//*Special Note:If you specify MediaStore.EXTRA_OUTPUT then no thumb pick is returned in intent extra - in activityOnResult
             - you have to read manually filepath you have provided as MediaStore.EXTRA_OUTPUT.*//*

            Uri fileUri = Uri.fromFile(photoFile);
            Bitmap imageBitmap = BitmapFactory.decodeFile(fileUri.getPath());
            OCR_Image ocr=new OCR_Image();
            String text=ocr.detectText(imageBitmap);
            Log.i("OCR",text);
            camera_button.setImageBitmap(imageBitmap);
        }

    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = JPEG_FILE_PREFIX + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File imageF = File.createTempFile(imageFileName, JPEG_FILE_SUFFIX, storageDir);
        return imageF;
    }*/
}
