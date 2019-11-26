package com.example.loginlayout;

public class MensajeRecibir extends Mensaje {

    private Long hora;

    public MensajeRecibir() {
    }

    public MensajeRecibir(Long hora) {
        this.hora = hora + 60000;
    }

    public MensajeRecibir(String mensaje, String nombre, String fotoPerfil, String type_mensaje, String urlFoto, Long hora) {
        super(mensaje, nombre, fotoPerfil, type_mensaje, urlFoto);
        this.hora = hora + 60000;
    }

    public Long getHora() {
        return hora;
    }

    public void Long(Long hora) {
        this.hora = hora;
    }
}
