package com.Veiled.Activities;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

import com.Veiled.R;
import com.Veiled.Utils.ImageData;

import java.io.File;

public class Barcode extends Activity {

    public static final String ANDROID_RESOURCE = "android.resource://";
    public static final String FORESLASH = "/";

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.barcode);

        Bundle bundle = this.getIntent().getExtras();
        ImageView barcode = (ImageView) findViewById(R.id.barcodeIv);
        String passedCampaignId = bundle.getString("campaignId");
        Uri uri = ImageData.getUriFromFile(passedCampaignId+"_barcode", 1);
        File file = new File(uri.getPath());
        if(file.exists())
            barcode.setImageURI(uri);
        else{
            Uri image_uri = Uri.parse(ANDROID_RESOURCE + getPackageName()
                    + FORESLASH + R.drawable.default_image);
            barcode.setImageURI(image_uri);
        }
    }
}
