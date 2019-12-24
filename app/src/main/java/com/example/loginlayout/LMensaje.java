package com.example.loginlayout;

import org.ocpsoft.prettytime.PrettyTime;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class LMensaje { //logica del mensaje

    private Mensaje mensaje;
    private String key;
    private LUsuari lUsuari;
    //debe conectarse al singleton usuari

    public LMensaje(Mensaje mensaje, String key) {
        this.mensaje = mensaje;
        this.key = key;
    }

    public Mensaje getMensaje() {
        return mensaje;
    }

    public void setMensaje(Mensaje mensaje) {
        this.mensaje = mensaje;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Long getCreatedTimestamp() {
        return (Long) mensaje.getCreatedTimestamp();
    }

    public LUsuari getlUsuari() {
        return lUsuari;
    }

    public void setlUsuari(LUsuari lUsuari) {
        this.lUsuari = lUsuari;
    }

    public String fechaDeCreacionDelMensaje(){
        Date date = new Date(getCreatedTimestamp());
        PrettyTime prettyTime = new PrettyTime(new Date(), Locale.getDefault());
        return prettyTime.format(date);
    }
}
