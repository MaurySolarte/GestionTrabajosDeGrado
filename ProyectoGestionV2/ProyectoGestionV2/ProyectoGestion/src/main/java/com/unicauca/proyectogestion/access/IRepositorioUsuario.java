package com.unicauca.proyectogestion.access;
import com.unicauca.proyectogestion.domain.*;

public interface IRepositorioUsuario {
    
    boolean iniciarSesion(Usuario usuario);
    boolean registrarUsuario(Usuario nuevoUsuario);
    
}
