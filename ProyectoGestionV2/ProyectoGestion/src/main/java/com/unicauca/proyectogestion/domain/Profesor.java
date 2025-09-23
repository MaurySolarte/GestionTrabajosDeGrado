package com.unicauca.proyectogestion.domain;

public class Profesor extends Usuario {
    private String areaInvestigacion;

    public Profesor(int idUsuario, String nombres, String apellidos, String celular,
                    EnumProgramas programa, EnumRoles rol, String email, String contrasenia,
                    String areaInvestigacion) {
        super(idUsuario, nombres, apellidos, celular, programa, rol, email, contrasenia);
        this.areaInvestigacion = areaInvestigacion;
    }

    public Profesor(String nombres, String apellidos, String celular,
                    EnumProgramas programa, EnumRoles rol, String email, String contrasenia,
                    String areaInvestigacion) {
        super(nombres, apellidos, celular, programa, rol, email, contrasenia);
        this.areaInvestigacion = areaInvestigacion;
    }

    public String getAreaInvestigacion() {
        return areaInvestigacion;
    }

    public void setAreaInvestigacion(String areaInvestigacion) {
        this.areaInvestigacion = areaInvestigacion;
    }

    @Override
    public String toString() {
        return this.getNombres() + " " + this.getApellidos();
    }
}
