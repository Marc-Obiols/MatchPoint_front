package com.example.loginlayout;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatHolder extends RecyclerView.ViewHolder {


    private CircleImageView civFotoChat;
    private TextView txtNombreChat;
    private LinearLayout layoutPrincipal;

    public ChatHolder(@NonNull View itemView) {
        super(itemView);
        civFotoChat = itemView.findViewById(R.id.civFotoChat);
        txtNombreChat = itemView.findViewById(R.id.txtNombreChat);
        layoutPrincipal = itemView.findViewById(R.id.layoutPrincipal);
    }

    public CircleImageView getCivFotoChat() {
        return civFotoChat;
    }

    public void setCivFotoChat(CircleImageView civFotoChat) {
        this.civFotoChat = civFotoChat;
    }

    public TextView getTxtNombreChat() {
        return txtNombreChat;
    }

    public void setTxtNombreChat(TextView txtNombreChat) {
        this.txtNombreChat = txtNombreChat;
    }

    public LinearLayout getLayoutPrincipal() {
        return layoutPrincipal;
    }

    public void setLayoutPrincipal(LinearLayout layoutPrincipal) {
        this.layoutPrincipal = layoutPrincipal;
    }
}