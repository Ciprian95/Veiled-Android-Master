package com.Veiled.Utils;

import android.os.AsyncTask;
import android.util.Log;

import com.cloudinary.Transformation;

import java.io.InputStream;

public class DownloadImageTask extends AsyncTask<String, Void, InputStream> {
    String id;
    int type;

    public DownloadImageTask(String id, int type) {
        this.id = id;
        this.type = type;
    }

    protected InputStream doInBackground(String... urls) {
        String urldisplay;
        if(type == 0)
            urldisplay = CloudImage.getCloudinary().url().transformation(new Transformation().
                width(0.2).crop("scale")).imageTag(id + ".png");
        else
            if(type == 1) {
                urldisplay = CloudImage.getCloudinary().url().transformation(new Transformation()
                        .width(0.2).crop("scale")).imageTag(id);
            }
            else{
                urldisplay = CloudImage.getCloudinary().url().transformation(new Transformation()
                        ).imageTag(id);
            }
        String url = CloudImage.parseUrl(urldisplay);

        InputStream in= null;
        try {
            in = new java.net.URL(url).openStream();
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return in;
    }

    protected void onPostExecute(InputStream i) {
        ImageData.SaveImage(i,id,type);
        Log.d("DownloadImageTask",id+"mesaj");
    }
}