package com.example.pial_pc.instantbookreview;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.pial_pc.instantbookreview.bitmapProcess.Tools;
import com.example.pial_pc.instantbookreview.imageCapture.FocusBoxView;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Pial-PC on 1/19/2016.
 */
public class ImageCapture extends ActionBarActivity implements SurfaceHolder.Callback {
    SurfaceView camera_frame;
    ImageButton shutter, focus;
    Camera.PictureCallback rawPictureCallback, jpegPictureCallback;
    Camera.ShutterCallback shutterCallback;
    Camera camera;
    FocusBoxView focusBox;
    Mat imageMat;
    Bitmap bmp;

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                {
                    Log.i("OpenCV", "OpenCV loaded successfully");
                    imageMat=new Mat();
                } break;
                default:
                {
                    super.onManagerConnected(status);
                } break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_capture);
        getSupportActionBar().hide();

        camera_frame = (SurfaceView) findViewById(R.id.camera_frame);
        shutter = (ImageButton) findViewById(R.id.shutter_button);
        focus = (ImageButton) findViewById(R.id.focus_button);
        focusBox=(FocusBoxView)findViewById(R.id.focus_box);
        SurfaceHolder surfaceHolder = camera_frame.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        shutter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //This code is for mute Camera shutter sound

                Camera.CameraInfo info = new Camera.CameraInfo();
                for (int id = 0; id < Camera.getNumberOfCameras(); id++) {
                    Camera.getCameraInfo(id, info);
                    if (info.canDisableShutterSound) {
                        camera.enableShutterSound(false);
                    }
                }

                camera.takePicture(shutterCallback, rawPictureCallback, jpegPictureCallback);
            }
        });

        focus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Camera.AutoFocusCallback autoFocusCallback=new Camera.AutoFocusCallback() {
                    @Override
                    public void onAutoFocus(boolean b, Camera camera) {

                    }
                };

                camera.autoFocus(autoFocusCallback);
            }
        });

        jpegPictureCallback = new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] bytes, Camera camera) {

                if (bytes == null) {
                    Log.d("OCR", "Got null data");
                    return;
                }


                bmp= Tools.getFocusedBitmap(getApplicationContext(), camera, bytes, focusBox.getBox());
                bmp=resize(bmp,400,300);

                File checkingFileDir=new File(Environment.getExternalStorageDirectory()+"/Instant_Book_Reviewer/");
                if(!checkingFileDir.exists()){
                    checkingFileDir.mkdir();
                }
                String timeStamp = new SimpleDateFormat("ddMMyyyyHHmmss").format(new Date());
                File imageOriginalFile;
                String oriImageName="Original_"+ timeStamp +".jpg";
                imageOriginalFile = new File(checkingFileDir.getPath() + File.separator + oriImageName);

                try{
                    FileOutputStream outImage=new FileOutputStream(imageOriginalFile);
                    bmp.compress(Bitmap.CompressFormat.PNG,90,outImage);
                    outImage.close();
                }catch (Exception e){
                    e.printStackTrace();
                }

                //This below code is for image processing using openCV.
                Utils.bitmapToMat(bmp, imageMat);
                Imgproc.cvtColor(imageMat, imageMat, Imgproc.COLOR_BGR2GRAY);
                Imgproc.GaussianBlur(imageMat, imageMat, new Size(3, 3), 0);
                Imgproc.threshold(imageMat, imageMat, 0, 255, Imgproc.THRESH_OTSU);
                Utils.matToBitmap(imageMat,bmp);

                //Saving Bitmap to sdcard
                /*File checkingFileDir=new File(Environment.getExternalStorageDirectory()+"/Instant_Book_Reviewer/");
                if(!checkingFileDir.exists()){
                    checkingFileDir.mkdir();
                }*/
//                String timeStamp = new SimpleDateFormat("ddMMyyyy").format(new Date());
                File imageFile;
                String mImageName="Image_"+ timeStamp +".jpg";
                imageFile = new File(checkingFileDir.getPath() + File.separator + mImageName);

                try{
                    FileOutputStream outImage=new FileOutputStream(imageFile);
                    bmp.compress(Bitmap.CompressFormat.PNG,90,outImage);
                    outImage.close();
                }catch (Exception e){
                    e.printStackTrace();
                }


                OCR_Image ocr_image=new OCR_Image(ImageCapture.this);
                ocr_image.execute(bmp);

                Log.d("OCR", "Got bitmap");


            }
        };

        shutterCallback = new Camera.ShutterCallback() {
            @Override
            public void onShutter() {

            }
        };

    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        try {
            //This checks if a device has a camera or not
            if (getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
                // this device has a camera
                camera = Camera.open();
            } else {
                // no camera on this device
                Toast.makeText(getApplicationContext(), "Your device doesn't have a Camera.", Toast.LENGTH_SHORT).show();
            }
            camera.setPreviewDisplay(surfaceHolder);
            camera.setDisplayOrientation(90);
            camera.startPreview();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }

    private Bitmap resize(Bitmap image, int maxWidth, int maxHeight) {
        if (maxHeight > 0 && maxWidth > 0) {
            int width = image.getWidth();
            int height = image.getHeight();
            float ratioBitmap = (float) width / (float) height;
            float ratioMax = (float) maxWidth / (float) maxHeight;

            int finalWidth = maxWidth;
            int finalHeight = maxHeight;
            if (ratioMax > 1) {
                finalWidth = (int) ((float)maxHeight * ratioBitmap);
            } else {
                finalHeight = (int) ((float)maxWidth / ratioBitmap);
            }
            image = Bitmap.createScaledBitmap(image, finalWidth, finalHeight, true);
            return image;
        } else {
            return image;
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            Log.d("OpenCV", "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, mLoaderCallback);
        } else {
            Log.d("OpenCV", "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if (camera != null) {
            camera.release();
        }

        SurfaceHolder surfaceHolder = camera_frame.getHolder();
        surfaceHolder.removeCallback(this);

    }
}
