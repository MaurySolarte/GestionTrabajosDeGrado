package com.unicauca.proyectogestion.access;

import com.unicauca.proyectogestion.domain.FormatoA;
import com.unicauca.proyectogestion.domain.Usuario;
import com.unicauca.proyectogestion.utilities.FormatoATabla;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface IRepositorioFormatoA {

    void guardarArchivoEnBD(File fileFormato, File fileCarta, String tipo, FormatoA formato);
    List<FormatoATabla> obtenerFormatos();
    boolean actualizarEstadoFormato(int idFormato, String tipo, String nuevoEstado);
    FormatoATabla obtenerFormatoPorCorreo(String correoBuscado);
    byte[] obtenerArchivoFormatoA(int idFormato, String tipo);
    FormatoATabla obtenerFormato(int idFormato);

}
