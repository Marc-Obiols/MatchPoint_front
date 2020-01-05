package com.example.loginlayout;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class events_recycleview_adapter_simple extends RecyclerView.Adapter<events_recycleview_adapter_simple.EventViewHolderSimple> {

    Context eventContext;
    List<holder_event_card_simple> eventsList;

    public events_recycleview_adapter_simple(Context eventContext, List<holder_event_card_simple> eventsList) {
        this.eventContext = eventContext;
        this.eventsList = eventsList;
    }

    @NonNull
    @Override
    public events_recycleview_adapter_simple.EventViewHolderSimple onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(eventContext).inflate(R.layout.card_item_event_listed_simple, parent, false);
        events_recycleview_adapter_simple.EventViewHolderSimple eviewHolder = new events_recycleview_adapter_simple.EventViewHolderSimple(v);

        return eviewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolderSimple holder, int position) {
        holder.tv_title_simple.setText(eventsList.get(position).getEventTitle());
        holder.tv_date_simple.setText(eventsList.get(position).getEventDate());


        switch (eventsList.get(position).getEventTitle()){
            case "Futbol":
                holder.iv_image_simple.setImageResource(R.drawable.futbol);
                break;
            case "Baloncesto":
                holder.iv_image_simple.setImageResource(R.drawable.baloncesto);
                break;
            case "Running":
                holder.iv_image_simple.setImageResource(R.drawable.running);
                break;
            case "PingPong":
                holder.iv_image_simple.setImageResource(R.drawable.pingpong);
                break;
            case "Hockey":
                holder.iv_image_simple.setImageResource(R.drawable.hockey);
                break;
            case "Golf":
                holder.iv_image_simple.setImageResource(R.drawable.golf);
                break;
            case "Tenis":
                holder.iv_image_simple.setImageResource(R.drawable.tennis);
                break;
            case "Rugby":
                holder.iv_image_simple.setImageResource(R.drawable.rugby);
                break;
            case "Ciclismo":
                holder.iv_image_simple.setImageResource(R.drawable.ciclismo);
                break;
            default:
                holder.iv_image_simple.setImageResource(R.drawable.soccer);
                break;
        }
    }


    @Override
    public int getItemCount() {
        return eventsList.size();
    }

    public static class EventViewHolderSimple extends RecyclerView.ViewHolder{

        private TextView tv_title_simple;
        private TextView tv_date_simple;
        private ImageView iv_image_simple;

        public EventViewHolderSimple(@NonNull View itemView) {
            super(itemView);

            iv_image_simple = itemView.findViewById(R.id.eventImage);
            tv_title_simple = itemView.findViewById(R.id.eventTitle);
            tv_date_simple = itemView.findViewById(R.id.eventDate);
        }
    }
}
