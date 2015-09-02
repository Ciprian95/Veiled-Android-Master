package com.Veiled.Utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;

import com.Veiled.Components.UserCredentials;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;


/**
 * Created by Laur on 4/27/2015.
 */
public class PreferencesManipulation {

    private static UserCredentials enrolled_user;
    private static int[] userPrefs;

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static void savePreferences(Context context){
        if(enrolled_user == null)
            enrolled_user= UserCredentials.getUserCredentialsInstance();

        // first install on other device
        if(enrolled_user.getPreferences() != null)
            userPrefs = enrolled_user.getPreferences();
        String prefMask = intArrayToStringMask();
        String fileName = GlobalData.fileNamePrefs;

       FileOutputStream outputStream;
       try {
            outputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            outputStream.write(prefMask.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static void readPreferences(Context context){

        if(enrolled_user == null)
            enrolled_user = UserCredentials.getUserCredentialsInstance();

        String fileName = GlobalData.fileNamePrefs;

        try {
            InputStream inputStream = context.openFileInput(fileName);

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                String line = bufferedReader.readLine();
                String[] prefsChars = line.split("(?!^)");

                int numPrefs = prefsChars.length;
                userPrefs = new int[numPrefs];

                for(int i = 0 ; i < numPrefs ; i++){
                    userPrefs[i] = Integer.parseInt(prefsChars[i]);
                }

                enrolled_user.setPreferences(userPrefs);

                inputStream.close();
            }
        }
        catch (FileNotFoundException e) {
            userPrefs = new int[]{1,1,1,1,1,1,1,1,1,1};  //empty array -- no pref selected
            enrolled_user.setPreferences(userPrefs);
        }
        catch (IOException e) {
            userPrefs = new int[]{1,1,1,1,1,1,1,1,1,1};  //empty array -- no pref selected
            enrolled_user.setPreferences(userPrefs);
        }
    }

    private static String intArrayToStringMask(){
        String mask = "";

        if(userPrefs == null){
            userPrefs = new int[]{1,1,1,1,1,1,1,1,1,1};  //empty array -- no pref selected
            enrolled_user.setPreferences(userPrefs);
        }

        for(int i = 0 ; i < userPrefs.length ; i++)
            mask += userPrefs[i] + "";
        return mask;
    }

    public static void changeCurrentPreference(int pref_index, int value){
        if(userPrefs == null){
            userPrefs = new int[]{1,1,1,1,1,1,1,1,1,1};  //empty array -- no pref selected
        }

        userPrefs[pref_index] = value;
    }

}
