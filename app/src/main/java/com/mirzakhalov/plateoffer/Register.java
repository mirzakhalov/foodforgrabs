package com.mirzakhalov.plateoffer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity implements View.OnClickListener {

    EditText firstName;
    EditText lastName;
    EditText email;
    EditText phoneNumber;
    Button register;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // set UI elements
        firstName = findViewById(R.id.firstname);
        lastName = findViewById(R.id.lastname);
        email = findViewById(R.id.email);
        phoneNumber = findViewById(R.id.phoneNumber);
        register = findViewById(R.id.register);

        // get the inputs from the signin and prefill the boxes
        prefillBoxes(getIntent().getStringExtra("name"), getIntent().getStringExtra("phoneNumber"), getIntent().getStringExtra("email"));

        register.setOnClickListener(this);

    }

    private void prefillBoxes(String name, String mPhoneNumber, String mEmail) {

        // set the name first. if the name consists of two words, put them separately
        if(name != null){
            String [] names = name.split(" ");
            if(names.length > 1){
                firstName.setText(names[0]);
                lastName.setText(names[1]);
            } else{
                firstName.setText(name);
            }
        }

        // set the phone number if available
        if(mPhoneNumber != null){
            phoneNumber.setText(mPhoneNumber);
        }

        // set the email if available
        if(mEmail != null) {
            email.setText(mEmail);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.register:
                register();
                break;
            // ...
        }
    }

    private void register() {
        if(validateFields()){
            String uid = getIntent().getStringExtra("id");
            Profile profile = new Profile(uid, firstName.getText().toString(), lastName.getText().toString(), phoneNumber.getText().toString(), email.getText().toString());
            FirebaseDatabase.getInstance().getReference().child("Users/" + uid + "/profile").setValue(profile).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    //TODO add progress bar
                    Intent intent = new Intent(Register.this, Avatar.class);
                    intent.putExtra("uid", getIntent().getStringExtra("id"));
                    startActivity(intent);

                }
            });
        }
    }

    private boolean validateFields() {
        if(TextUtils.isEmpty(firstName.getText())){
            firstName.setError("Cannot be empty");
            return false;
        }
        if(TextUtils.isEmpty(lastName.getText())){
            lastName.setError("Cannot be empty");
            return false;
        }
        if(TextUtils.isEmpty(phoneNumber.getText())){
            phoneNumber.setError("Cannot be empty");
            return false;
        }
        if(TextUtils.isEmpty(email.getText())){
            email.setError("Cannot be empty");
            return false;
        }
        return true;
    }
}
