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

public class events_recycleview_adapter extends RecyclerView.Adapter<events_recycleview_adapter.EventViewHolder> {

    Context eventContext;
    List<holder_event_card> eventsList;

    public events_recycleview_adapter(Context eventContext, List<holder_event_card> eventsList) {
        this.eventContext = eventContext;
        this.eventsList = eventsList;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(eventContext).inflate(R.layout.card_item_event_listed, parent, false);
        EventViewHolder eviewHolder = new EventViewHolder(v);

        return eviewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        holder.tv_title.setText(eventsList.get(position).getEventTitle());
        holder.tv_creator.setText(eventsList.get(position).getEventUser());
        holder.tv_date.setText(eventsList.get(position).getEventDate());

        switch (holder.tv_title.toString()){
            case "futbol":
                holder.iv_image.setImageResource(R.drawable.soccer);
                break;
            case "Baloncesto":
                holder.iv_image.setImageResource(R.drawable.soccer);
                break;
            case "Tenis":
                holder.iv_image.setImageResource(R.drawable.soccer);
                break;
            case "Padel":
                holder.iv_image.setImageResource(R.drawable.soccer);
                break;
            case "Hockey":
                holder.iv_image.setImageResource(R.drawable.soccer);
                break;
            case "Golf":
                holder.iv_image.setImageResource(R.drawable.soccer);
                break;
            case "Rugby":
                holder.iv_image.setImageResource(R.drawable.soccer);
                break;
            default:
                holder.iv_image.setImageResource(R.drawable.soccer);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return eventsList.size();
    }

    public static class EventViewHolder extends RecyclerView.ViewHolder{

        private TextView tv_title;
        private TextView tv_creator;
        private TextView tv_date;
        private ImageView iv_image;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);

            iv_image = itemView.findViewById(R.id.eventImage);
            tv_title = itemView.findViewById(R.id.eventTitle);
            tv_creator = itemView.findViewById(R.id.eventCreator);
            tv_date = itemView.findViewById(R.id.eventDate);
        }
    }
}
