package com.unicauca.proyectogestion.access;
import com.unicauca.proyectogestion.domain.*;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface IRepositorioUsuario {
    
    boolean iniciarSesion(String email, String contrasenia);
    boolean registrarUsuario(Usuario nuevoUsuario) throws SQLException;
    boolean buscarEmail(Connection conn, String email) throws SQLException;
    Usuario obtenerUsuarioPorEmail(String email);
    String obtenerRolUsuario(String email);
    List<Profesor> listarProfesores();
    Estudiante obtenerEstudiantePorCorreo(String email);
    Usuario obtenerUsuarioPorCodigo(String codigo);
}
