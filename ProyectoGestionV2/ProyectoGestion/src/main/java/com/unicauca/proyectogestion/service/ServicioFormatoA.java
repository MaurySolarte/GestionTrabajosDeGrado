package com.unicauca.proyectogestion.service;

import com.unicauca.proyectogestion.access.IRepositorioFormatoA;
import com.unicauca.proyectogestion.domain.Estudiante;
import com.unicauca.proyectogestion.domain.FormatoA;
import com.unicauca.proyectogestion.domain.Profesor;
import com.unicauca.proyectogestion.utilities.FormatoATabla;

import java.io.File;
import java.util.List;

public class ServicioFormatoA {
    private IRepositorioFormatoA repositorioFormatoA;

    public ServicioFormatoA(IRepositorioFormatoA repositorioFormatoA) {
        this.repositorioFormatoA = repositorioFormatoA;
    }
    public void guardarArchivoEnBD(File fileFormato, File fileCarta, String tipo, FormatoA formato) {
        repositorioFormatoA.guardarArchivoEnBD(fileFormato,fileCarta, tipo, formato);
    }
    
    public List<FormatoATabla> obtenerFormatos(){
        return repositorioFormatoA.obtenerFormatos();
    }

}
