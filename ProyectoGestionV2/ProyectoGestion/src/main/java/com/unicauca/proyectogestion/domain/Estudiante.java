package com.unicauca.proyectogestion.domain;

public class Estudiante extends Usuario {
    private String estadoProyecto;

    public Estudiante(int idUsuario, String nombres, String apellidos, String celular,
                      EnumProgramas programa, EnumRoles rol, String email, String contrasenia,
                      String estadoProyecto) {
        super(idUsuario, nombres, apellidos, celular, programa, rol, email, contrasenia);
        this.estadoProyecto = estadoProyecto;
    }

    public Estudiante(String nombres, String apellidos, String celular,
                      EnumProgramas programa, EnumRoles rol, String email, String contrasenia,
                      String estadoProyecto) {
        super(nombres, apellidos, celular, programa, rol, email, contrasenia);
        this.estadoProyecto = estadoProyecto;
    }

    public String getEstadoProyecto() {
        return estadoProyecto;
    }

    public void setEstadoProyecto(String estadoProyecto) {
        this.estadoProyecto = estadoProyecto;
    }
}
