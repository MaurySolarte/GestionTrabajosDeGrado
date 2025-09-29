package com.unicauca.proyectogestion.access;

public class    Factory {
    
    private static Factory instancia;

    public Factory() {
    }     
    
    public static Factory getInstancia() {
        if (instancia == null){
            instancia = new Factory();
        }
        return instancia;
    }
            
    public IRepositorioUsuario obtenerRepositorioUsuario(String tipo){

        IRepositorioUsuario repositorio = null;

        switch(tipo){
            case "SQLite":
                repositorio = new RepositorioUsuario();
                break;

        }
        
        return repositorio;
    }

    public IRepositorioFormatoA obtenerRepositorioFormatoA(String tipo){

        IRepositorioFormatoA repositorio = null;

        switch(tipo){
            case "SQLite":
                repositorio = new RepositorioFormatoA();
                break;

        }

        return repositorio;
    }
}
