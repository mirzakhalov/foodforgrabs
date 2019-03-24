package com.mirzakhalov.plateoffer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;

public class Orders extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);


        HashMap<String, String> info = new HashMap<>();
        info.put("address", "Here at USF");
        info.put("price", "$99");
        info.put("mealItem", "Chick fill a");
        info.put("time", "Today at 12.36");

        HashMap<String, String> info2 = new HashMap<>();
        info2.put("address", "Here at UCF");
        info2.put("price", "$0.99");
        info2.put("mealItem", "Subway");
        info2.put("time", "Today at 12.36");


        ArrayList<HashMap<String, String>> box = new ArrayList<>();

        box.add(info);
        box.add(info2);

        RecyclerView recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        OrderRecyclerView adapter = new OrderRecyclerView(this, box);
        recyclerView.setAdapter(adapter);
    }
}
