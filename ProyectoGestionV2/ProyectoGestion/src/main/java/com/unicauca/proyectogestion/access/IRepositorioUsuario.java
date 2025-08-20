package com.unicauca.proyectogestion.access;
import com.unicauca.proyectogestion.domain.*;
import java.sql.SQLException;

public interface IRepositorioUsuario {
    
    boolean iniciarSesion(String email, String contrasenia);
    boolean registrarUsuario(Usuario nuevoUsuario) throws SQLException;
    public Usuario obtenerUsuarioPorEmail(String email);
}
