package com.unicauca.proyectogestion.service;

import com.unicauca.proyectogestion.access.IRepositorioUsuario;
import com.unicauca.proyectogestion.domain.Usuario;
import java.sql.SQLException;

public class Servicio {
    
    private IRepositorioUsuario repositorio;

    public Servicio(IRepositorioUsuario repositorio) {
        this.repositorio = repositorio;
    }

          
    
    public boolean registrarUsuario(Usuario nuevoUsuario) throws SQLException{
        
        if(nuevoUsuario == null || nuevoUsuario.getNombres() == null){
            return false;
        }
                
        return repositorio.registrarUsuario(nuevoUsuario); 
    }
    
    public int iniciarSesion(String email, String contrasenia){
        
        if (email == null || email.isEmpty() || contrasenia == null || contrasenia.isEmpty()){
        return 2;}
        else if (repositorio.iniciarSesion(email, contrasenia)){
        return 1;}
        else 
        return 0;
    }
    
    public String validarContrasenaSegura(String contrasena) {
        if (contrasena.length() < 6) {
            return "La contraseña debe tener al menos 6 caracteres.";
        }
        if (!contrasena.matches(".*[A-Z].*")) {
            return "La contraseña debe contener al menos una letra mayúscula.";
        }
        if (!contrasena.matches(".*\\d.*")) {
            return "La contraseña debe contener al menos un número.";
        }
        if (!contrasena.matches(".*[!@#$%^&(),.?\":{}|<>].*")) {
            return "La contraseña debe contener al menos un carácter especial.";
        }
    return "OK";
    }
}
