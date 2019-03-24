package com.mirzakhalov.plateoffer;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class OfferRecyclerView extends RecyclerView.Adapter<OfferRecyclerView.ViewHolder> {
    private ArrayList<HashMap<String, String>> data;
    private LayoutInflater mInflater;
    Context context;

    public OfferRecyclerView(Context context, ArrayList<HashMap<String, String>> data){
        this.mInflater = LayoutInflater.from(context);
        this.data=data;
        this.context = context;

    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.single_offer, parent, false);
        return new OfferRecyclerView.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.count.setText(data.get(position).get("count"));
        holder.originalPrice.setText(data.get(position).get("originalPrice"));
        holder.mealItem.setText(data.get(position).get("mealItem"));
        holder.offeredPrice.setText(data.get(position).get("offeredPrice"));
        holder.remaining.setText(data.get(position).get("remaining"));

        holder.bind(holder, position);

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView imgIcon;

        TextView count;
        TextView originalPrice;
        TextView mealItem;
        TextView offeredPrice;
        TextView remaining;
        LinearLayout container;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgIcon = itemView.findViewById(R.id.logo);
            count = itemView.findViewById(R.id.count);
            originalPrice = itemView.findViewById(R.id.originalPrice);
            mealItem = itemView.findViewById(R.id.mealItem);
            offeredPrice = itemView.findViewById(R.id.offeredPrice);
            remaining = itemView.findViewById(R.id.remaining);
            container = itemView.findViewById(R.id.container);

        }

        public void bind(final ViewHolder holder, final int position){

            holder.container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("Click", "Clicked " + position);
                }
            });
        }
    }


}

