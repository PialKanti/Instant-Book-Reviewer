package com.example.pial_pc.instantbookreview;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Pial-PC on 1/29/2016.
 */
public class CopyAssetsFile {
    Context context;
    InputStream in;
    OutputStream out;

    public CopyAssetsFile(Context context) {
        this.context = context;
    }

    public void CopyTrainData() {
        AssetManager assetManager = context.getAssets();
        try {
            String[] files = assetManager.list("tessdata");
            if (files != null) {
                for (String filename : files) {
                    in = assetManager.open("tessdata/" + filename);
                    File outFile = new File(context.getExternalFilesDir(null) + "/tessdata/", filename);
                    out = new FileOutputStream(outFile);
                    copyFile(in, out);
                    in.close();
                    in = null;
                    out.flush();
                    out.close();
                    out = null;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("CopyAssetsFile", "Error copy train data from asset");
        }
    }

    public void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }
}
