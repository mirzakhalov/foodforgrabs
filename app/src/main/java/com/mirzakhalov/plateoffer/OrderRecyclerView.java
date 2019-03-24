package com.mirzakhalov.plateoffer;

import android.content.Context;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;


public class OrderRecyclerView extends RecyclerView.Adapter<OrderRecyclerView.ViewHolder> {

    private ArrayList<HashMap<String, String>> mData;

    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    OrderRecyclerView(Context context, ArrayList<HashMap<String, String>> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.single_order, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.dateTime.setText(mData.get(position).get("time"));
        holder.address.setText(mData.get(position).get("address"));
        holder.mealItem.setText(mData.get(position).get("mealItem"));
        holder.price.setText(mData.get(position).get("price"));
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView logo;
        TextView dateTime;
        TextView mealItem;
        TextView price;
        TextView address;

        ViewHolder(View itemView) {
            super(itemView);
            logo = itemView.findViewById(R.id.logo);
            dateTime = itemView.findViewById(R.id.time_date);
            mealItem = itemView.findViewById(R.id.meal_item);
            price = itemView.findViewById(R.id.discounted_price);
            address = itemView.findViewById(R.id.address);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    HashMap<String, String> getItem(int id) {
        return mData.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}