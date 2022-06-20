package com.example.proyectofinalgimnasio;

public class ModeloRutina {
    private int idRutina;
    private String nombre;
    private String objetivo;

    public ModeloRutina(){

    }

    public ModeloRutina(int idRutina, String nombre, String objetivo) {
        this.idRutina = idRutina;
        this.nombre = nombre;
        this.objetivo = objetivo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getObjetivo() {
        return objetivo;
    }

    public void setObjetivo(String objetivo) {
        this.objetivo = objetivo;
    }

    public int getIdRutina() {
        return idRutina;
    }

    public void setIdRutina(int idRutina) {
        this.idRutina = idRutina;
    }
}
