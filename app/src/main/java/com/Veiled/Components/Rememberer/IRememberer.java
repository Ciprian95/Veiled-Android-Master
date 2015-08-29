package com.Veiled.Components.Rememberer;

import com.Veiled.Components.UserCredentials;

/**
 * Created by Laur on 3/19/2015.
 */
public interface IRememberer {
    boolean hasBeenChecked();
    UserCredentials getSavedCredentials();
}
