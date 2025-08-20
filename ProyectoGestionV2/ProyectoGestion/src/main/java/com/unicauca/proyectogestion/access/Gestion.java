package com.unicauca.proyectogestion.access;

import com.unicauca.proyectogestion.access.*;

public class Gestion {
    
    private static Gestion instancia;

    public Gestion() {
    }     
    
    public static Gestion getInstancia() {
        if (instancia == null){
            instancia = new Gestion();
        }
        return instancia;
    }
            
    public IRepositorioUsuario obtenerRepositorio(String tipo){
        
        IRepositorioUsuario repositorio = null;
        
        switch(tipo){
            case "SQLite":
                repositorio = new RepositorioUsuario();
                break;
        }
        
        return repositorio;
    }
    
    
}
