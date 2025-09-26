package com.unicauca.proyectogestion.access;
import com.unicauca.proyectogestion.domain.FormatoA;
import com.unicauca.proyectogestion.utilities.FormatoATabla;

import java.io.File;
import java.io.FileInputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RepositorioFormatoA implements IRepositorioFormatoA {

    private static final String URL = "jdbc:sqlite:./ProyectoGestionDB.db";

    @Override
    public byte[] obtenerArchivoFormatoA(int idFormato, String tipo) {
        String tabla = tipo.equals("Investigacion")
                ? "FormatoAInvestigacion"
                : "FormatoAPracticaProfesional";

        String sql = "SELECT archivo_proyecto FROM " + tabla + " WHERE id_formato = ?";

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idFormato);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getBytes("archivo_proyecto"); // 👈 devuelve el archivo como byte[]
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void guardarArchivoEnBD(File fileFormato, File fileCarta, String tipo, FormatoA formato) {
        String sql = "";
        String tabla = "";

        // Definir tabla y sentencia SQL
        if (tipo.equals("formato_a")) {
            tabla = "FormatoAInvestigacion";
            sql = "INSERT INTO FormatoAInvestigacion (\n"
                    + " titulo, modalidad, fecha_actual, director, codirector,\n"
                    + " correo_estudiante, id_profesor, objetivo_general,\n"
                    + " objetivos_especificos, archivo_proyecto,\n"
                    + " intento, estado)\n"
                    + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        } else if (tipo.equals("carta_empresa")) {
            tabla = "FormatoAPracticaProfesional";
            sql = "INSERT INTO FormatoAPracticaProfesional (\n"
                    + " titulo, modalidad, fecha_actual, director, codirector,\n"
                    + " correo_estudiante, id_profesor, objetivo_general,\n"
                    + " objetivos_especificos, archivo_proyecto, carta_recomendacion,\n"
                    + " intento, estado)\n"
                    + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        }

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:./ProyectoGestionDB.db")) {

            // 🔹 Calcular el nuevo intento antes del INSERT
            int nuevoIntento = 1;
            try (PreparedStatement pstmtCheck = conn.prepareStatement(
                    "SELECT MAX(intento) FROM " + tabla + " WHERE correo_estudiante = ?")) {

                pstmtCheck.setString(1, formato.getCorreoEstudiante());
                ResultSet rs = pstmtCheck.executeQuery();
                if (rs.next()) {
                    int ultimoIntento = rs.getInt(1);
                    if (!rs.wasNull()) {
                        nuevoIntento = ultimoIntento + 1;
                    }
                }
            }

            // 🔹 Validar que no supere 3 intentos
            if (nuevoIntento > 3) {
                System.out.println("El estudiante ya alcanzó el máximo de 3 intentos. No se puede subir más archivos.");
                return; // salimos sin guardar
            }

            try (PreparedStatement pstmt = conn.prepareStatement(sql);
                 FileInputStream fis = new FileInputStream(fileFormato);
                 FileInputStream fisCarta = (tipo.equals("carta_empresa") && fileCarta != null)
                         ? new FileInputStream(fileCarta) : null) {

                // 🔹 Asignar parámetros comunes
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
                    pstmt.setInt(12, nuevoIntento);
                    pstmt.setString(13, "EnRevision"); // estado inicial
                } else { // formato_a
                    pstmt.setInt(11, nuevoIntento);
                    pstmt.setString(12, "EnRevision");
                }

                pstmt.executeUpdate();
                System.out.println("Archivo guardado correctamente en la BD con intento=" + nuevoIntento);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<FormatoATabla> obtenerFormatos() {
        List<FormatoATabla> lista = new ArrayList<>();

        String sql = "SELECT id_formato, titulo, modalidad, director, codirector, correo_estudiante, id_profesor, fecha_actual, estado, intento " +
                "FROM FormatoAInvestigacion " +
                "UNION " +
                "SELECT id_formato, titulo, modalidad, director, codirector, correo_estudiante, id_profesor, fecha_actual, estado, intento" +
                "FROM FormatoAPracticaProfesional";

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:./ProyectoGestionDB.db");
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int idFormato = rs.getInt("id_formato");

                String titulo = rs.getString("titulo");
                String correo = rs.getString("correo_estudiante");
                String director = rs.getString("director");
                String codirector = rs.getString("codirector");
                String modalidad = rs.getString("modalidad");
                String fecha = rs.getString("fecha_actual");
                String estado = rs.getString("estado");

                lista.add(new FormatoATabla(String.valueOf(idFormato), titulo, correo, director, codirector, modalidad, fecha, estado));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    @Override
    public FormatoATabla obtenerFormatoPorCorreo(String correoBuscado) {
        FormatoATabla formato = null;

        String sql = "SELECT id_formato, titulo,  correo_estudiante, director, codirector, modalidad, fecha_actual, estado, intento " +
                "FROM FormatoAInvestigacion WHERE correo_estudiante = ? " +
                "UNION " +
                "SELECT id_formato, titulo,  correo_estudiante, director, codirector, modalidad, fecha_actual, estado, intento " +
                "FROM FormatoAPracticaProfesional WHERE correo_estudiante = ?";

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:./ProyectoGestionDB.db");
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, correoBuscado);
            //pstmt.setString(2, correoBuscado);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int idFormato = rs.getInt("id_formato");
                    String titulo = rs.getString("titulo");
                    String correo = rs.getString("correo_estudiante");
                    String director = rs.getString("director");
                    String codirector = rs.getString("codirector");
                    String modalidad = rs.getString("modalidad");
                    String fecha = rs.getString("fecha_actual");
                    String estado = rs.getString("estado");

                    System.out.println("Id formato: " + idFormato + "titulo: " + titulo + "modalidad: " + modalidad);

                    formato = new FormatoATabla(String.valueOf(idFormato), titulo, correo, director, codirector, modalidad, fecha, estado);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return formato;
    }
    @Override
    public boolean actualizarEstadoFormato(int idFormato, String tipo, String nuevoEstado) {
        String tabla = tipo.equalsIgnoreCase("Investigacion")
                ? "FormatoAInvestigacion"
                : "FormatoAPracticaProfesional";

        String sql = "UPDATE " + tabla + " SET estado = ? WHERE id_formato = ?";

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nuevoEstado);
            pstmt.setInt(2, idFormato);

            int rows = pstmt.executeUpdate();
            return rows > 0; // devuelve true si se actualizó algo
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public FormatoATabla obtenerFormato(int idFormato) {
        FormatoATabla formato = null;

        String sql = "SELECT id_formato, titulo, modalidad, director, codirector, correo_estudiante, id_profesor " +
                "FROM FormatoAInvestigacion WHERE id_formato = ? " +
                "UNION " +
                "SELECT id_formato, titulo, modalidad, director, codirector, correo_estudiante, id_profesor " +
                "FROM FormatoAPracticaProfesional WHERE id_formato = ?";

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:./ProyectoGestionDB.db");
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idFormato);
            pstmt.setInt(2, idFormato);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String titulo = rs.getString("titulo");
                    String correo = rs.getString("correo_estudiante");
                    String director = rs.getString("director");
                    String codirector = rs.getString("codirector");
                    String modalidad = rs.getString("modalidad");
                    String fecha = rs.getString("fecha_actual");
                    String estado = rs.getString("estado");

                    formato = new FormatoATabla(String.valueOf(idFormato), titulo, correo, director, codirector, modalidad, fecha, estado);

                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return formato;
    }
}
