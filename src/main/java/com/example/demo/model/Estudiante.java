package com.example.demo.model;

public class Estudiante {
    private Long id;
    private String nombre;
    private String carnet;
    private String carrera;

    public Estudiante() {}

    public Estudiante(Long id, String nombre, String carnet, String carrera) {
        this.id = id;
        this.nombre = nombre;
        this.carnet = carnet;
        this.carrera = carrera;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getCarnet() { return carnet; }
    public void setCarnet(String carnet) { this.carnet = carnet; }

    public String getCarrera() { return carrera; }
    public void setCarrera(String carrera) { this.carrera = carrera; }

    public void setApellido(String string) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setApellido'");
    }

    public void setEmail(String string) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setEmail'");
    }
}
