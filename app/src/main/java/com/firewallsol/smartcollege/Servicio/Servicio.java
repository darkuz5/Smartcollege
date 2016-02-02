package com.firewallsol.smartcollege.Servicio;

/**
 * Created by DARKUZ5 on 02/12/2015.
 */
public class Servicio {
    String id;
    String nombre;
    String descripcion;
    String telefono;
    String correo;
    String pagina;
    String direccion;
    String coordenadas;
    String foto;
    String data;

    public Servicio(String id, String nombre, String descripcion, String telefono, String correo, String pagina,
                    String direccion, String coordenadas, String foto, String data) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.telefono = telefono;
        this.correo = correo;
        this.pagina = pagina;
        this.direccion = direccion;
        this.coordenadas = coordenadas;
        this.foto = foto;
        this.data = data;

    }
}