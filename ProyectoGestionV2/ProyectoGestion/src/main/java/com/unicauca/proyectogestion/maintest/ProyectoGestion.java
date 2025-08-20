package com.unicauca.proyectogestion.maintest;

import com.unicauca.proyectogestion.access.Gestion;
import com.unicauca.proyectogestion.access.IRepositorioUsuario;
import com.unicauca.proyectogestion.access.RepositorioUsuario;
import com.unicauca.proyectogestion.domain.EnumProgramas;
import com.unicauca.proyectogestion.domain.EnumRoles;
import com.unicauca.proyectogestion.domain.Usuario;
import com.unicauca.proyectogestion.service.Servicio;
import java.sql.SQLException;

public class ProyectoGestion {

    public static void main(String[] args) {
        
        IRepositorioUsuario repository = new RepositorioUsuario();        
        
        try{
        Usuario newUser = new Usuario("Yo", "yo", "321", EnumProgramas.Ingeniería_de_Sistemas, EnumRoles.Docente, "a@tumama", "12345");                       
        
        
        repository.registrarUsuario(newUser);
        }catch(SQLException ex){
            System.out.println("sss");    
        }
    }
}
