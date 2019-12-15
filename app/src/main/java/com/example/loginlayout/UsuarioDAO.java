package com.example.loginlayout;

import com.google.firebase.auth.FirebaseAuth;

public class UsuarioDAO {

    private static UsuarioDAO usuarioDAO;

    public static UsuarioDAO getInstance() {
        if (usuarioDAO == null) {
            usuarioDAO = new UsuarioDAO();
        }
        return usuarioDAO;
    }

    public String getKeyUsuario(){
        return FirebaseAuth.getInstance().getUid();
    }
}
