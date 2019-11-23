package com.example.loginlayout;

public class UsuariSingleton {

    private int id;
    private String nom_usuari;
    private String mail;
    private static final UsuariSingleton ourInstance = new UsuariSingleton();

    public static UsuariSingleton getInstance() {
        return ourInstance;
    }

    public boolean setId(int id) {
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

    public int getId() {return id;}
    public String getMail() {return mail;}
    public String getNom_usuari() {return nom_usuari;}

    private UsuariSingleton() {
    }
}
