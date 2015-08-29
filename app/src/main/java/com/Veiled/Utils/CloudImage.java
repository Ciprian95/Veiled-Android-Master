package com.Veiled.Utils;

import android.graphics.Bitmap;

import com.cloudinary.Cloudinary;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class CloudImage {
    private static Cloudinary cloudinary;

    public static Cloudinary getCloudinary(){
        if( cloudinary == null){
            initCloudinary();
        }
        return cloudinary;
    }

    public static void initCloudinary(){
        Map config = new HashMap();
        config.put("cloud_name", "veiled");
        config.put("api_key", "936962392616979");
        config.put("api_secret", "fs7zB1RsuL-aPOehH-EE4o8T5-g");
        cloudinary = new Cloudinary(config);
    }

    public static String parseUrl(String url) {
        StringTokenizer st = new StringTokenizer(url, "'");
        String result = new String();
        if (st != null)
            st.nextToken();
        if (st.hasMoreTokens())
            result = st.nextToken().toString();
        return result;
    }
}
