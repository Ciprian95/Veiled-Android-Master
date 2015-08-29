package com.Veiled.SqlConnection.Tables;

public class User_pref {
    public String id;
    public String user_id;
    public int preference_id;

    public User_pref(String i_user_id, int i_preference_id){
        user_id = i_user_id;
        preference_id = i_preference_id;
    }

}
