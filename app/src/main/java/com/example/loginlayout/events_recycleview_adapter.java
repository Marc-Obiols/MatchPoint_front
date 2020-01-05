package com.example.loginlayout;

import android.content.Context;
import android.content.Intent;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class events_recycleview_adapter extends RecyclerView.Adapter<events_recycleview_adapter.EventViewHolder> {

    Context eventContext;
    List<holder_event_card> eventsList;
    boolean canClick;

    public events_recycleview_adapter(Context eventContext, List<holder_event_card> eventsList, boolean Clickable) {
        this.eventContext = eventContext;
        this.eventsList = eventsList;
        this.canClick = Clickable;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(eventContext).inflate(R.layout.card_item_event_listed, parent, false);
        final EventViewHolder eviewHolder = new EventViewHolder(v);

        if(canClick) {
            eviewHolder.item_event.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(eventContext, "Test Click" + String.valueOf(eviewHolder.getAdapterPosition()), Toast.LENGTH_SHORT).show();
                    Intent myIntent = new Intent(eventContext, activity_valoracion.class);
                    eventContext.startActivity(myIntent);
                }
            });
        }
        return eviewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        holder.tv_title.setText(eventsList.get(position).getEventTitle());
        holder.tv_creator.setText(eventsList.get(position).getEventUser());
        holder.tv_date.setText(eventsList.get(position).getEventDate());


        switch (eventsList.get(position).getEventTitle()){
            case "Futbol":
                holder.iv_image.setImageResource(R.drawable.futbol);
                break;
            case "Baloncesto":
                holder.iv_image.setImageResource(R.drawable.baloncesto);
                break;
            case "Running":
                holder.iv_image.setImageResource(R.drawable.running);
                break;
            case "Ping Pong":
                holder.iv_image.setImageResource(R.drawable.pingpong);
                break;
            case "Hockey":
                holder.iv_image.setImageResource(R.drawable.hockey);
                break;
            case "Golf":
                holder.iv_image.setImageResource(R.drawable.golf);
                break;
            case "Tennis":
                holder.iv_image.setImageResource(R.drawable.tennis);
                break;
            case "Rugby":
                holder.iv_image.setImageResource(R.drawable.rugby);
                break;
            case "Ciclismo":
                holder.iv_image.setImageResource(R.drawable.ciclismo);
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
        private CardView item_event;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);

            iv_image = itemView.findViewById(R.id.eventImage);
            tv_title = itemView.findViewById(R.id.eventTitle);
            tv_creator = itemView.findViewById(R.id.eventCreator);
            tv_date = itemView.findViewById(R.id.eventDate);
            item_event = itemView.findViewById(R.id.event_item_id);
        }
    }
}
