package com.android.nosh.src.login;

import com.google.firebase.database.IgnoreExtraProperties;

//User handles data transfer to Firebase using the same variable names
@IgnoreExtraProperties
public class User
{

    public String name;
    public String email;
    public boolean isEmployee;

    //Needed for FirebaseDatabase
    public User() {

    }

    public User(String name, String email, boolean isEmployee)
    {

        this.name = name;
        this.email = email;
        this.isEmployee = isEmployee;
    }
}
