package com.Veiled.Components.Rememberer;

import com.Veiled.Components.UserCredentials;

import java.io.File;

/**
 * Created by Laur on 3/19/2015.
 */
public class Rememberer implements IRememberer {
    private String fileName = "cred.txt";
    private File file;

    public Rememberer(){
        file = new File(fileName);
    }

    @Override
    public boolean hasBeenChecked() {
        if(file.exists()) {
            if(file.length() != 0)
                return true;
        }
        return false;
    }

    @Override
    public UserCredentials getSavedCredentials() {
        return null;
    }
}
