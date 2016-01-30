package com.firewallsol.smartcollege.Gaceta;

/**
 * Created by DARKUZ5 on 02/12/2015.
 */
public class Gaceta {
    String id;
    String tutor;
    String avatar_tutor;
    String titulo;
    String texto;
    String fecha;
    String foto;
    String url;

    public Gaceta(String id, String tutor, String avatar_tutor, String titulo, String texto, String fecha,
                  String foto, String url) {
        this.id = id;
        this.tutor = tutor;
        this.avatar_tutor = avatar_tutor;
        this.titulo = titulo;
        this.texto = texto;
        this.fecha = fecha;
        this.foto = foto;
        this.url = url;

    }
}