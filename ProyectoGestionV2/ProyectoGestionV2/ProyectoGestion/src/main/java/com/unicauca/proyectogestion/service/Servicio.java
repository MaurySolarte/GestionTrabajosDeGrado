package com.unicauca.proyectogestion.service;

import com.unicauca.proyectogestion.access.IRepositorioUsuario;
import com.unicauca.proyectogestion.domain.Usuario;

public class Servicio {
    
    private IRepositorioUsuario repositorio;

    public Servicio(IRepositorioUsuario repositorio) {
        this.repositorio = repositorio;
    }
    
    public boolean registrarUsuario(Usuario nuevoUsuario){
        
     return false;   
    }
    
    public boolean iniciarSesion(Usuario usuario){
        return false;
    }
    
    public String validarContrasenaSegura(String contrasena) {
            if (contrasena.length() < 6) {
                return "La contraseña debe tener al menos 6 caracteres.";
            }
            if (!contrasena.matches(".[A-Z].")) {
                return "La contraseña debe contener al menos una letra mayúscula.";
            }
            if (!contrasena.matches(".\\d.")) {
                return "La contraseña debe contener al menos un número.";
            }
            if (!contrasena.matches(".[!@#$%^&(),.?\":{}|<>].*")) {
                return "La contraseña debe contener al menos un carácter especial.";
            }
        return "OK";
    }
}
