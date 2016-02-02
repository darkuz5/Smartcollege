package com.firewallsol.smartcollege.TareaModel;

/**
 * Created by DARKUZ5 on 02/12/2015.
 */
public class TareaModel {
    String id;
    String materia;
    String titulo;
    String descripcion;
    String fecha;

    public TareaModel(String id, String materia, String titulo, String descripcion,  String fecha) {
        this.id = id;
        this.materia = materia;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fecha = fecha;

    }
}