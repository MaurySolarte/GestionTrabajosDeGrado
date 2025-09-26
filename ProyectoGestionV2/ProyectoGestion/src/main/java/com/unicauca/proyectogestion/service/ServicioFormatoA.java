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
    public byte[] obtenerArchivoFormatoA(int idFormato, String tipo) {
        return repositorioFormatoA.obtenerArchivoFormatoA(idFormato, tipo);
    }
    public List<FormatoATabla> obtenerFormatos(){
        return repositorioFormatoA.obtenerFormatos();
    }

    public FormatoATabla obtenerFormatoPorCorreo(String correoBuscado){
        return repositorioFormatoA.obtenerFormatoPorCorreo(correoBuscado);
    }
    public boolean actualizarEstadoFormato(int idFormato, String tipo, String nuevoEstado) {
        return repositorioFormatoA.actualizarEstadoFormato(idFormato, tipo, nuevoEstado);
    }

    public FormatoATabla obtenerFormato(int idFormato){
        return repositorioFormatoA.obtenerFormato(idFormato);
    }
}
