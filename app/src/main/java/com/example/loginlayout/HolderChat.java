package com.example.loginlayout;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import de.hdodenhof.circleimageview.CircleImageView;

public class HolderChat extends RecyclerView.ViewHolder {

    private TextView nombre;
    private CircleImageView fotoGrupo;
    private LinearLayout layoutPrincipal;


    public HolderChat(@NonNull View itemView) {
        super(itemView);
        nombre = (TextView) itemView.findViewById(R.id.txtNombreChat);
        fotoGrupo = (CircleImageView) itemView.findViewById(R.id.civFotoChat);
        layoutPrincipal = (LinearLayout) itemView.findViewById(R.id.layoutPrincipal);
    }

    public TextView getNombre() {
        return nombre;
    }

    public void setNombre(TextView nombre) {
        this.nombre = nombre;
    }

    public CircleImageView getFotoGrupo() {
        return fotoGrupo;
    }

    public void setFotoGrupo(CircleImageView fotoGrupo) {
        this.fotoGrupo = fotoGrupo;
    }

    public LinearLayout getLayoutPrincipal() {
        return layoutPrincipal;
    }

    public void setLayoutPrincipal(LinearLayout layoutPrincipal) {
        this.layoutPrincipal = layoutPrincipal;
    }
}
