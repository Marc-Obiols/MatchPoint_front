package com.example.loginlayout;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class events_list_adapter extends RecyclerView.Adapter<events_list_adapter.EventListViewHolder> {

    private Context acontext;
    private ArrayList<holder_event_card> eventList;

    public events_list_adapter (Context context, ArrayList<holder_event_card> list){
        acontext = context;
        eventList = list;
    }

    @NonNull
    @Override
    public EventListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(acontext).inflate(R.layout.card_item_event_listed, parent, false);
        return new EventListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventListViewHolder holder, int position) {
        holder_event_card current = eventList.get(position);

        String titulo = current.getEventTitle();
        String creator = current.getEventUser();
        String date = current.getEventDate();
        CircleImageView image = current.getEventImage();


        holder.titleTextView.setText(titulo);
        holder.creatorTextView.setText(creator);
        holder.dateTextView.setText(date);

        /*
        String tema = "";
        System.out.println(titulo);
        int i = 0;
        while (titulo.charAt(i) != ' ') {
            tema += titulo.charAt(i);
            i += 1;
        }
        switch (tema){
            case "futbol":
                holder.imageView.setImageResource(R.drawable.soccer);
                break;
            case "Baloncesto":
                holder.imageView.getEventImage().setImageResource(R.drawable.soccer);
                break;
            case "Tenis":
                current.getEventImage().setImageResource(R.drawable.soccer);
                break;
            case "Padel":
                current.getEventImage().setImageResource(R.drawable.soccer);
                break;
            case "Hockey":
                current.getEventImage().setImageResource(R.drawable.soccer);
                break;
            case "Golf":
                current.getEventImage().setImageResource(R.drawable.soccer);
                break;
            case "Rugby":
                current.getEventImage().setImageResource(R.drawable.soccer);
                break;
            default:
                current.getEventImage().setImageResource(R.drawable.soccer);
                break;
        }

        final String finalTema = tema;
        */
    }


    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public class EventListViewHolder extends RecyclerView.ViewHolder{

        public ImageView imageView;
        public TextView titleTextView;
        public TextView creatorTextView;
        public TextView dateTextView;

        public EventListViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.eventImage);
            titleTextView = itemView.findViewById(R.id.eventTitle);
            creatorTextView = itemView.findViewById(R.id.eventCreator);
            dateTextView = itemView.findViewById(R.id.eventDate);
        }
    }
}
