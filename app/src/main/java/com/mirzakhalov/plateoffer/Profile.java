package com.mirzakhalov.plateoffer;

import java.io.Serializable;

public class Profile implements Serializable {

    public String firstName;
    public String lastName;
    public String phoneNumber;
    public String email;
    public String uid;


    public Profile(String uid, String firstName, String lastName, String phoneNumber, String email){

        this.uid = uid;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.email = email;


    }






}
