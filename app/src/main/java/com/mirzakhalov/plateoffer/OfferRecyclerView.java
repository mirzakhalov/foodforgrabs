package com.mirzakhalov.plateoffer;

import android.content.Context;
import android.content.Intent;
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

        holder.count.setText("Count: " + data.get(position).get("count"));
        holder.originalPrice.setText("Original price: $" + data.get(position).get("originalPrice"));
        holder.mealItem.setText(data.get(position).get("mealItem"));
        holder.offeredPrice.setText("Offer price: $" + data.get(position).get("offeredPrice"));
        holder.remaining.setText("Offer expires in " + data.get(position).get("remaining") + " mins");

        if(data.get(position).get("company").equals("Chick Fill A")){
            holder.imgIcon.setImageResource(R.drawable.chickfila);

        } else if (data.get(position).get("company").equals("Panda Express")){
            holder.imgIcon.setImageResource(R.drawable.panda);

        }
        holder.bind(holder, position, data.get(position));

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

        public void bind(final ViewHolder holder, final int position, final HashMap<String, String> data){

            holder.container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("Click", "Clicked " + position);
                    Intent intent = new Intent(context, OrderDetails.class);
                    intent.putExtra("count", data.get("count"));
                    intent.putExtra("originalPrice", data.get("originalPrice"));
                    intent.putExtra("offeredPrice", data.get("offeredPrice"));
                    intent.putExtra("remaining", data.get("remaining"));
                    intent.putExtra("mealItem", data.get("mealItem"));
                    intent.putExtra("address", data.get("address"));
                    intent.putExtra("company", data.get("company"));
                    intent.putExtra("offerID", data.get("offerID"));
                    context.startActivity(intent);
                }
            });
        }
    }


}

