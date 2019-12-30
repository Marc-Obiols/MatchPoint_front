package com.example.loginlayout;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AdapterMensajes  extends RecyclerView.Adapter<HolderMensaje> {

    private List<LMensaje> listMensaje = new ArrayList<>();
    private Context c;

    public AdapterMensajes(Context c) {
        this.c = c;
    }

    public void addMensaje(LMensaje lm) {
        listMensaje.add(lm);
        notifyItemInserted(listMensaje.size());
    }

    @NonNull
    @Override
    public HolderMensaje onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        if (viewType == 1) {
            v = LayoutInflater.from(c).inflate(R.layout.card_view_mensaje_emisor,parent,false);
        }
        else
            v = LayoutInflater.from(c).inflate(R.layout.card_view_mensaje_receptor,parent,false);
        return new HolderMensaje(v);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderMensaje holder, int position) {

        LMensaje lMensaje = listMensaje.get(position);
        System.out.println(lMensaje.getKey());
        //System.out.println(lMensaje.getlUsuari().getKey());
        //System.out.println(lMensaje.getlUsuari().getUsuari().getNombre());
        holder.getNombre().setText(lMensaje.getlUsuari().getUsuari().getNom_usuari());
        //Uri uriFotoPerfil = Uri.parse(lMensaje.getlUsuari().getUsuari().getFotoPerfil());
        Picasso.get().load(lMensaje.getlUsuari().getUsuari().getFotoPerfil()).into(holder.getFotoMensaje());
        //holder.getFotoMensaje().setImageURI(uriFotoPerfil);
        holder.getMensaje().setText(lMensaje.getMensaje().getMensaje());

        if(lMensaje.getMensaje().isContieneFoto()) {
            holder.getFotoMensajeEnviado().setVisibility(View.VISIBLE);
            holder.getMensaje().setVisibility(View.VISIBLE);
            Glide.with(c).load(lMensaje.getMensaje().getUrlFoto()).into(holder.getFotoMensajeEnviado());
        }
        else {
            holder.getFotoMensajeEnviado().setVisibility(View.GONE);
            holder.getMensaje().setVisibility(View.VISIBLE);
        }

        /*if (lMensaje.getlUsuari().getUsuari().getFotoPerfilUrl().isEmpty()) {
            holder.getFotoMensaje().setImageResource(R.mipmap.ic_launcher);
        }
        else {
            Glide.with(c).load(lMensaje.getlUsuari().getUsuari().getFotoPerfilUrl()).into(holder.getFotoMensaje());
        }*/

        holder.getHora().setText(lMensaje.fechaDeCreacionDelMensaje());
    }

    @Override
    public int getItemCount() {
        return listMensaje.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (listMensaje.get(position).getlUsuari() != null) {
            System.out.println("NO ES NULL");
            if(listMensaje.get(position).getlUsuari().getKey().equals(UsuariSingleton.getInstance().getId())) {
                return 1;
            }
            return -1;
        }
        return -1;
    }
}
