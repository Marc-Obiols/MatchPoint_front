package com.example.loginlayout;

public class Usuari { //clase para el firebase

    private String nombre;
    private String correo;
    private String FotoPerfilUrl;

    public Usuari() {
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getFotoPerfilUrl() {
        return FotoPerfilUrl;
    }

    public void setFotoPerfilUrl(String fotoPerfilUrl) {
        FotoPerfilUrl = fotoPerfilUrl;
    }
}
