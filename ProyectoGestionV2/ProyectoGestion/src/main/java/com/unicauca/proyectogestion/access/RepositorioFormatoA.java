package com.unicauca.proyectogestion.access;
import com.unicauca.proyectogestion.domain.FormatoA;
import com.unicauca.proyectogestion.service.Servicio;
import com.unicauca.proyectogestion.utilities.FormatoATabla;

import java.io.File;
import java.io.FileInputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RepositorioFormatoA implements IRepositorioFormatoA {

    private static final String URL = "jdbc:sqlite:./ProyectoGestionDB.db";

    @Override
    public void guardarArchivoEnBD(File fileFormato, File fileCarta, String tipo, FormatoA formato) {
        String sql = "";
        if (tipo.equals("formato_a")) {
            sql = "INSERT INTO FormatoAInvestigacion (titulo, modalidad, fecha_actual, director, codirector, correo_estudiante, id_profesor, objetivo_general, objetivos_especificos, archivo_proyecto) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        } else if (tipo.equals("carta_empresa")) {
            sql = "INSERT INTO FormatoAPracticaProfesional (titulo, modalidad, fecha_actual, director, codirector, correo_estudiante, id_profesor, objetivo_general, objetivos_especificos, archivo_proyecto, carta_recomendacion) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        }

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:./ProyectoGestionDB.db");
             PreparedStatement pstmt = conn.prepareStatement(sql);
             FileInputStream fis = new FileInputStream(fileFormato);
             FileInputStream fisCarta = (tipo.equals("carta_empresa") && fileCarta != null) ? new FileInputStream(fileCarta) : null) {

            pstmt.setString(1, formato.getTitulo());
            pstmt.setString(2, formato.getModalidad().toString());
            pstmt.setString(3, formato.getFechaActual().toString());
            pstmt.setString(4, formato.getNombreDirector());
            pstmt.setString(5, formato.getNombreCodirector());
            pstmt.setString(6, formato.getCorreoEstudiante());
            pstmt.setInt(7, formato.getProfesor().getIdUsuario());
            pstmt.setString(8, formato.getObjetivoGeneral());
            pstmt.setString(9, formato.getObjetivosEspecificos());
            pstmt.setBinaryStream(10, fis, (int) fileFormato.length());

            if (tipo.equals("carta_empresa")) {
                pstmt.setBinaryStream(11, fisCarta, (int) fileCarta.length());
            }

            pstmt.executeUpdate();
            System.out.println("Archivo guardado correctamente en la BD.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<FormatoATabla> obtenerFormatos() {
        List<FormatoATabla> lista = new ArrayList<>();

        String sql = "SELECT titulo, modalidad, fecha_actual, director, codirector, correo_estudiante, id_profesor, objetivo_general, objetivos_especificos " +
                "FROM FormatoAInvestigacion " +
                "UNION " +
                "SELECT titulo, modalidad, fecha_actual, director, codirector, correo_estudiante, id_profesor, objetivo_general, objetivos_especificos " +
                "FROM FormatoAPracticaProfesional";

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:./ProyectoGestionDB.db");
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String correo = rs.getString("correo_estudiante");
                String director = rs.getString("director");
                String modalidad = rs.getString("modalidad");

                lista.add(new FormatoATabla(correo, director, modalidad, "Sin evaluar"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}
