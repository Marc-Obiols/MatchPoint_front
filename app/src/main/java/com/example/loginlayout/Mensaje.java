package com.example.loginlayout;

import com.google.firebase.database.ServerValue;

public class Mensaje { //Clase para el firebase

    private String mensaje;
    private String urlFoto;
    private boolean contieneFoto;
    private String keyEmisor;
    private Object createdTimestamp;

    public Mensaje() {
        createdTimestamp = ServerValue.TIMESTAMP;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getUrlFoto() {
        return urlFoto;
    }

    public void setUrlFoto(String urlFoto) {
        this.urlFoto = urlFoto;
    }

    public boolean isContieneFoto() {
        return contieneFoto;
    }

    public void setContieneFoto(boolean contieneFoto) {
        this.contieneFoto = contieneFoto;
    }

    public String getKeyEmisor() {
        return keyEmisor;
    }

    public void setKeyEmisor(String keyEmisor) {
        this.keyEmisor = keyEmisor;
    }

    public Object getCreatedTimestamp() {
        return createdTimestamp;
    }
}
