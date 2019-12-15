package com.example.loginlayout;

public class LUsuari {

    private Usuari usuari;
    private String Key;

    public LUsuari(Usuari usuari, String key) {
        this.usuari = usuari;
        Key = key;
    }

    public Usuari getUsuari() {
        return usuari;
    }

    public void setUsuari(Usuari usuari) {
        this.usuari = usuari;
    }

    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        Key = key;
    }
}
