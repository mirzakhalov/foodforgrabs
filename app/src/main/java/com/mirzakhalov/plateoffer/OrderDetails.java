package com.mirzakhalov.plateoffer;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class OrderDetails extends AppCompatActivity {

    TextView count;
    TextView originalPrice;
    TextView offeredPrice;
    TextView address;
    TextView remaining;
    TextView mealItem;

    ImageView logo;

    Button checkAvailibility;
    Button reserve;

    EditText quantity;
    EditText eta;
    LinearLayout input;
    LinearLayout page;
    FrameLayout waitingPage;

    ProgressBar progressBar;
    Button cancel;

    ValueEventListener postListener;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        count = findViewById(R.id.count);
        originalPrice = findViewById(R.id.originalPrice);
        offeredPrice = findViewById(R.id.offeredPrice);
        address = findViewById(R.id.address);
        remaining = findViewById(R.id.remaining);
        mealItem = findViewById(R.id.mealItem);
        logo = findViewById(R.id.logo);

        checkAvailibility = findViewById(R.id.checkAvailibility);
        reserve = findViewById(R.id.reserve);

        cancel = findViewById(R.id.cancel);
        progressBar = findViewById(R.id.progressBar);
        page = findViewById(R.id.page);
        waitingPage = findViewById(R.id.waitingBox);

        quantity = findViewById(R.id.quantity);
        eta = findViewById(R.id.eta);
        input = findViewById(R.id.input);



        count.setText("Count: " + getIntent().getStringExtra("count"));
        originalPrice.setText("Original Price: " + getIntent().getStringExtra("originalPrice"));
        offeredPrice.setText("Offer Price: " + getIntent().getStringExtra("offeredPrice"));
        address.setText("Address: " + getIntent().getStringExtra("address"));
        remaining.setText("Time remaining: " + getIntent().getStringExtra("remaining"));
        mealItem.setText(getIntent().getStringExtra("mealItem"));

        final String companyName = getIntent().getStringExtra("company");
        final String offerID = getIntent().getStringExtra("offerID");


        if(companyName.equals("Chick Fill A")){

            logo.setImageResource(R.drawable.chickfila);

        } else if (companyName.equals("Panda Express")) {

            logo.setImageResource(R.drawable.panda);
        }

       postListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){
                    String response = dataSnapshot.getValue(String.class);
                    if(response.equals("yes")){

                       waitingPage.setVisibility(View.GONE);
                       page.setVisibility(View.VISIBLE);
                       checkAvailibility.setVisibility(View.GONE);
                       reserve.setVisibility(View.VISIBLE);

                    } else{
                        postListener = null;
                        Toast.makeText(OrderDetails.this, "Sorry this offer expired or not available anymore", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(OrderDetails.this, LandingPage.class);
                        finish();
                        startActivity(intent);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        checkAvailibility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mQuantity = quantity.getText().toString();
                String mEta = eta.getText().toString();
                HashMap<String, String> offer = new HashMap<>();
                offer.put("quantity", mQuantity);
                offer.put("eta", mEta);
                offer.put("name", LandingPage.NAME);
                offer.put("time", String.valueOf(System.currentTimeMillis()));
                FirebaseDatabase.getInstance().getReference().child("Companies/" + companyName + "/" + offerID + "/" + LandingPage.USERID).setValue(offer).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        page.setVisibility(View.INVISIBLE);
                        waitingPage.setVisibility(View.VISIBLE);
                        FirebaseDatabase.getInstance().getReference().child("Companies/" + companyName + "/" + offerID + "/" + LandingPage.USERID + "/response/").addValueEventListener(postListener);
                    }
                });
            }
        });

        reserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                page.setVisibility(View.VISIBLE);
                waitingPage.setVisibility(View.GONE);
                postListener = null;
            }
        });




    }
}
