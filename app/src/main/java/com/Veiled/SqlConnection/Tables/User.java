package com.Veiled.SqlConnection.Tables;

public class User {

    public String id;
    public String email;
    public String fname;
    public String lname;
    public String password;

    public User(String i_email, String i_fname, String i_lname, String i_password){
        email = i_email;
        fname = i_fname;
        lname = i_lname;
        password = i_password;
    }


}
