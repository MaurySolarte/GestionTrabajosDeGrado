package com.unicauca.proyectogestion.domain;

public class Usuario {
    private int idUsuario;
    private String nombres;
    private String apellidos;    
    private String celular;
    private EnumProgramas programa;
    private EnumRoles rol;
    private String email;    
    private String contrasenia;    

    public Usuario(int idUsuario, String nombres, String apellidos, String celular, EnumProgramas programa, EnumRoles rol, String email, String contrasenia) {
        this.idUsuario = idUsuario;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.celular = celular;
        this.programa = programa;
        this.rol = rol;
        this.email = email;
        this.contrasenia = contrasenia;
    }

    public Usuario(String nombres, String apellidos, String celular, EnumProgramas programa, EnumRoles rol, String email, String contrasenia) {
        this.idUsuario = idUsuario;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.celular = celular;
        this.programa = programa;
        this.rol = rol;
        this.email = email;
        this.contrasenia = contrasenia;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public EnumProgramas getPrograma() {
        return programa;
    }

    public void setPrograma(EnumProgramas programa) {
        this.programa = programa;
    }

    public EnumRoles getRol() {
        return rol;
    }

    public void setRol(EnumRoles rol) {
        this.rol = rol;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }
    
}
