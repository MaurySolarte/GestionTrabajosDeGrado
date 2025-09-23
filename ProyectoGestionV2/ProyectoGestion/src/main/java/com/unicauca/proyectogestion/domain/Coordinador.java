package com.unicauca.proyectogestion.domain;

public class Coordinador extends Usuario {
    private String facultad;

    // Constructor con id
    public Coordinador(int idUsuario, String nombres, String apellidos, String celular,
                       EnumProgramas programa, EnumRoles rol, String email, String password,
                       String facultad) {
        super(idUsuario, nombres, apellidos, celular, programa, rol, email, password);
        this.facultad = facultad;
    }

    // Constructor sin id (cuando aún no está en BD)
    public Coordinador(String nombres, String apellidos, String celular,
                       EnumProgramas programa, EnumRoles rol, String email, String password,
                       String facultad) {
        super(nombres, apellidos, celular, programa, rol, email, password);
        this.facultad = facultad;
    }

    // Getters y Setters
    public String getFacultad() {
        return facultad;
    }

    public void setFacultad(String facultad) {
        this.facultad = facultad;
    }
}
