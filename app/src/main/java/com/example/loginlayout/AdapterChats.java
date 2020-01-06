package com.example.loginlayout;

import android.content.Context;
import android.content.Intent;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;
import java.util.List;

public class AdapterChats extends RecyclerView.Adapter<HolderChat> {

    private List<Pair<Chat, String>> listChat = new ArrayList<>();
    private Context c;

    public AdapterChats(Context c) {
        this.c = c;
    }

    public void addChat(Chat chat, String id) {
        Pair <Chat, String> nChat = new Pair<Chat, String>(chat, id);
        listChat.add(nChat);
        notifyItemInserted(listChat.size());
    }

    @NonNull
    @Override
    public HolderChat onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(c).inflate(R.layout.card_view_chat,parent,false);
        return new HolderChat(v);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderChat holder, final int position) {
        final Chat chat = listChat.get(position).first;
        holder.getNombre().setText(chat.getName());
        String nombreChat = chat.getName();
        int i = 0;
        String tema = "";
        System.out.println(nombreChat);
        while (nombreChat.charAt(i) != ' ') {
            tema += nombreChat.charAt(i);
            i += 1;
        }
        switch (tema){
            case "Futbol":
                holder.getFotoGrupo().setImageResource(R.drawable.futbol);
                break;
            case "Baloncesto":
                holder.getFotoGrupo().setImageResource(R.drawable.baloncesto);
                break;
            case "Tenis":
                holder.getFotoGrupo().setImageResource(R.drawable.tennis);
                break;
            case "PingPong":
                holder.getFotoGrupo().setImageResource(R.drawable.pingpong);
                break;
            case "Hockey":
                holder.getFotoGrupo().setImageResource(R.drawable.hockey);
                break;
            case "Golf":
                holder.getFotoGrupo().setImageResource(R.drawable.golf);
                break;
            case "Rugby":
                holder.getFotoGrupo().setImageResource(R.drawable.rugby);
                break;
            case "Running":
                holder.getFotoGrupo().setImageResource(R.drawable.running);
                break;
            case "Ciclismo":
                holder.getFotoGrupo().setImageResource(R.drawable.ciclismo);
                break;
            default:
                holder.getFotoGrupo().setImageResource(R.drawable.futbol);
                break;
        }

        final String finalTema = tema;
        holder.getLayoutPrincipal().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(c.getApplicationContext(), fragment_msgs.class);
                i.putExtra("id_chat", listChat.get(position).second);
                i.putExtra("nombre_grupo", chat.getName());
                i.putExtra("deporte", finalTema);
                c.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listChat.size();
    }
}
