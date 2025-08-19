package com.unicauca.proyectogestion.access;
import com.unicauca.proyectogestion.domain.*;
import java.sql.SQLException;

public interface IRepositorioUsuario {
    
    boolean iniciarSesion(Usuario usuario);
    boolean registrarUsuario(Usuario nuevoUsuario) throws SQLException;
    
}
