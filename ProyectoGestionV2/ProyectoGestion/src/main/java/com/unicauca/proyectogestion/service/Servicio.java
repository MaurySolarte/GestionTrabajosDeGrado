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
    
    public Usuario obtenerUsuarioPorEmail(String email){

        Usuario usuario = repositorio.obtenerUsuarioPorEmail(email);

        return usuario;        
    }
    
    public String obtenerRolUsuario(String email) {
        return repositorio.obtenerRolUsuario(email);
    }
    
    public String validarContrasenaSegura(String contrasena){
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
    
    public String validarCorreoInstitucional(String correo) {
        if (correo == null || correo.trim().isEmpty()) {
            return "El correo no puede estar vacío.";
        }

        // Verifica que tenga al menos un @
        if (!correo.contains("@")) {
            return "El correo debe contener el carácter '@'.";
        }

        // Verifica que termine con el dominio institucional
        if (!correo.endsWith("@unicauca.edu.co")) {
            return "El correo debe pertenecer al dominio @unicauca.edu.co.";
        }

        return "OK";
    }
    

}
