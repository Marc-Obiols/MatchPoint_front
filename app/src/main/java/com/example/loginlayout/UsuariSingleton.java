package com.example.loginlayout;

import java.util.ArrayList;
import java.util.List;

public class UsuariSingleton {

    private String id;
    private String nom_usuari;
    private String mail;
    private List<String> eventos_part;

    private static final UsuariSingleton ourInstance = new UsuariSingleton();

    public static UsuariSingleton getInstance() {
        return ourInstance;
    }

    public boolean setId(String id) {
        this.id = id;
        return true;
    }
    public boolean setMail(String mail) {
        this.mail = mail;
        return true;
    }
    public boolean setNom_usuari(String nom_usuari) {
        this.nom_usuari = nom_usuari;
        return true;
    }

    public void user_LogOut() {
        id = null;
        nom_usuari = null;
        mail = null;
        eventos_part = null;
    }

    public String getId() {return id;}
    public String getMail() {return mail;}
    public String getNom_usuari() {return nom_usuari;}

    public void addIdEvento(String id) {
        eventos_part.add(id);
    }

    public void deleteIdEvento(String id) {
        eventos_part.remove(id);
    }

    private UsuariSingleton() {
        eventos_part = new ArrayList<>();
    }
}
