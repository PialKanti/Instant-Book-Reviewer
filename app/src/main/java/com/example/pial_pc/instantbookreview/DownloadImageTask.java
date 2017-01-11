package com.example.pial_pc.instantbookreview;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Pial-PC on 2/13/2016.
 */
public class DownloadImageTask extends AsyncTask<String,Void,String> {
    Context context;
    String bookTitle="",bookAuthor="",bookISBN="",imageURL="";

    public DownloadImageTask(Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(String... strings) {
        bookTitle=strings[0];
        bookTitle=bookTitle.replaceAll(" ","_");
        bookAuthor=strings[1];
        bookAuthor=bookAuthor.replaceAll(" ","_");
        bookISBN=strings[2];
        File folder=new File(Environment.getExternalStorageDirectory()+"/ReadList_books_cover/");
        if (!folder.exists()) {
            folder.mkdir();
        }
        File imageOriginalFile;
        String oriImageName=bookTitle+"_"+bookAuthor+"_"+bookISBN+".jpg";
        imageOriginalFile = new File(folder.getPath() +"/"+ oriImageName);

        //Image Download link
        imageURL = "http://covers.openlibrary.org/b/isbn/" +bookISBN+ "-M.jpg";

        try {
            URL url=new URL(imageURL);

            //Downloading file
            URLConnection ucon = url.openConnection();
            InputStream inputStream = null;
            HttpURLConnection httpConn = (HttpURLConnection)ucon;
            httpConn.setRequestMethod("GET");
            httpConn.connect();

            if (httpConn.getResponseCode() == HttpURLConnection.HTTP_OK)
            {
                inputStream = httpConn.getInputStream();
            }

            FileOutputStream fos = new FileOutputStream(imageOriginalFile);
            int totalSize = httpConn.getContentLength();
            int downloadedSize = 0;
            byte[] buffer = new byte[1024];
            int bufferLength = 0;
            while ( (bufferLength = inputStream.read(buffer)) >0 )
            {
                fos.write(buffer, 0, bufferLength);
                downloadedSize += bufferLength;
            }

            Log.i("DownloadImage","downloadedSize:"+downloadedSize+"totalSize:"+ totalSize) ;

            fos.close();
            Log.d("DownloadImage", "Image Saved in sdcard..");
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }
}
