package com.unicauca.proyectogestion.service;

import com.unicauca.proyectogestion.access.IRepositorioFormatoA;
import com.unicauca.proyectogestion.domain.EnumModalidad;
import com.unicauca.proyectogestion.domain.Estudiante;
import com.unicauca.proyectogestion.domain.FormatoA;
import com.unicauca.proyectogestion.domain.Profesor;
import com.unicauca.proyectogestion.utilities.DevolucionTabla;
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
    public boolean registrarDevolucionFormatoA(int idFormato, int idProfesor, int idCoordinador,
                                               String correoEstudiante1, String correoEstudiante2,
                                               byte[] archivoDevolucion, String modalidad, int intento) {
        return repositorioFormatoA.guardarDevolucionFormatoA(idFormato, idProfesor, idCoordinador,
                correoEstudiante1, correoEstudiante2,
                archivoDevolucion, modalidad, intento);
    }

    public Object[] obtenerUltimoIntentoEstado(String correo, EnumModalidad modalidad) {
        return repositorioFormatoA.obtenerUltimoIntentoEstado(correo, modalidad);
    }

    public List<DevolucionTabla> obtenerDevolucionesPorProfesor(int idProfesor) {
        return repositorioFormatoA.obtenerDevolucionesPorProfesor(idProfesor);
    }

    public int obtenerNumeroDeIntentos(int idFormato) {
        return repositorioFormatoA.obtenerNumeroDeIntentos(idFormato);
    }

    public int obtenerIdProfesorPorFormato(int idFormato, String tipoFormato) {
        return repositorioFormatoA.obtenerIdProfesorPorFormato(idFormato, tipoFormato);
    }

    public FormatoATabla obtenerFormatoCompleto(int idFormato, String modalidad) {
        return repositorioFormatoA.obtenerFormatoCompleto(idFormato, modalidad);
    }

    public FormatoATabla obtenerFormato(int idFormato){
        return repositorioFormatoA.obtenerFormato(idFormato);
    }
}
