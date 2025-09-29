package com.unicauca.proyectogestion.access;
import com.unicauca.proyectogestion.domain.*;
import com.unicauca.proyectogestion.utilities.DevolucionTabla;
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
                    + " correo_estudiante_1, correo_estudiante_2, id_profesor, objetivo_general,\n"
                    + " objetivos_especificos, archivo_proyecto,\n"
                    + " intento, estado)\n"
                    + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
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

            if(tipo.equals("formato_a") && ((FormatoAInvestigacion) formato).getEstudiante2() != null){
                try (PreparedStatement pstmtCheck = conn.prepareStatement(
                        "SELECT MAX(intento) FROM " + tabla + " WHERE correo_estudiante_1 = ? AND correo_estudiante_2 = ?")) {

                    pstmtCheck.setString(1, formato.getCorreoEstudiante());
                    pstmtCheck.setString(2, ((FormatoAInvestigacion) formato).getCorreoEstudiante2());
                    ResultSet rs = pstmtCheck.executeQuery();
                    if (rs.next()) {
                        int ultimoIntento = rs.getInt(1);
                        if (!rs.wasNull()) {
                            nuevoIntento = ultimoIntento + 1;
                        }
                    }
                }

            }else if(tipo.equals("formato_a")){
                try (PreparedStatement pstmtCheck = conn.prepareStatement(
                        "SELECT MAX(intento) FROM " + tabla + " WHERE correo_estudiante_1 = ?")) {

                    pstmtCheck.setString(1, formato.getCorreoEstudiante());
                    ResultSet rs = pstmtCheck.executeQuery();
                    if (rs.next()) {
                        int ultimoIntento = rs.getInt(1);
                        if (!rs.wasNull()) {
                            nuevoIntento = ultimoIntento + 1;
                        }
                    }
                }
            } else{
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
            }

            if (nuevoIntento > 3) {
                System.out.println("Algún estudiante ya alcanzó el máximo de 3 intentos. No se puede subir más archivos.");
                return; // salimos sin guardar
            }

            try (PreparedStatement pstmt = conn.prepareStatement(sql);
                 FileInputStream fis = new FileInputStream(fileFormato);
                 FileInputStream fisCarta = (tipo.equals("carta_empresa") && fileCarta != null)
                         ? new FileInputStream(fileCarta) : null) {

                // 🔹 Asignar parámetros comunes



                if (tipo.equals("carta_empresa")) {
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
                    pstmt.setBinaryStream(11, fisCarta, (int) fileCarta.length());
                    pstmt.setInt(12, nuevoIntento);
                    pstmt.setString(13, "EnRevision"); // estado inicial
                } else { // formato_a
                    pstmt.setString(1, formato.getTitulo());
                    pstmt.setString(2, formato.getModalidad().toString());
                    pstmt.setString(3, formato.getFechaActual().toString());
                    pstmt.setString(4, formato.getNombreDirector());
                    pstmt.setString(5, formato.getNombreCodirector());
                    pstmt.setString(6, formato.getCorreoEstudiante());
                    pstmt.setString(7, ((FormatoAInvestigacion) formato).getEstudiante2() != null ? ((FormatoAInvestigacion) formato).getCorreoEstudiante2(): null);
                    pstmt.setInt(8, formato.getProfesor().getIdUsuario());
                    pstmt.setString(9, formato.getObjetivoGeneral());
                    pstmt.setString(10, formato.getObjetivosEspecificos());
                    pstmt.setBinaryStream(11, fis, (int) fileFormato.length());
                    pstmt.setInt(12, nuevoIntento);
                    pstmt.setString(13, "EnRevision"); // estado inicial
                }

                pstmt.executeUpdate();
                System.out.println("Archivo guardado correctamente en la BD con intento=" + nuevoIntento);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public FormatoATabla obtenerFormatoCompleto(int idFormato, String modalidad) {
        FormatoATabla formato = null;
        String sql = "";
        if(modalidad == "Investigacion"){
            sql = "SELECT id_formato, titulo, modalidad, fecha_actual, director, codirector, " +
                            "correo_estudiante_1, correo_estudiante_2, objetivo_general, objetivos_especificos, " +
                            "archivo_proyecto, intento, estado " +
                            "FROM FormatoAInvestigacion WHERE id_formato = ?";
        }else{
            sql = "SELECT id_formato, titulo, modalidad, fecha_actual, director, codirector, " +
                    "correo_estudiante, objetivo_general, objetivos_especificos, archivo_proyecto, carta_recomendacion, intento,  estado " +
                    "FROM FormatoAPracticaProfesional WHERE id_formato = ?";
        }

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:./ProyectoGestionDB.db")) {

            if (modalidad == "Investigacion") {
                // 1️⃣ Buscar en FormatoAInvestigacion
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setInt(1, idFormato);
                    try (ResultSet rs = pstmt.executeQuery()) {
                        if (rs.next()) {
                            formato = new FormatoATabla(
                                    String.valueOf(rs.getInt("id_formato")),
                                    rs.getString("titulo"),
                                    rs.getString("correo_estudiante_1"),
                                    rs.getString("correo_estudiante_2"),
                                    rs.getString("director"),
                                    rs.getString("codirector"),
                                    rs.getString("modalidad"),
                                    rs.getString("fecha_actual"),
                                    rs.getString("estado"),
                                    rs.getString("intento"),
                                    rs.getString("objetivo_general"),
                                    rs.getString("objetivos_especificos"),
                                    rs.getBytes("archivo_proyecto")
                            );
                            return formato;
                        }
                    }
                }
            }else{
                // 2️⃣ Buscar en FormatoAPracticaProfesional
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setInt(1, idFormato);
                    try (ResultSet rs = pstmt.executeQuery()) {
                        if (rs.next()) {
                            formato = new FormatoATabla(
                                    String.valueOf(rs.getInt("id_formato")),
                                    rs.getString("titulo"),
                                    rs.getString("correo_estudiante"), // solo un estudiante
                                    rs.getString("director"),
                                    rs.getString("codirector"),
                                    rs.getString("modalidad"),
                                    rs.getString("fecha_actual"),
                                    rs.getString("estado"),
                                    rs.getString("intento"),
                                    rs.getString("objetivo_general"),
                                    rs.getString("objetivos_especificos"),
                                    rs.getBytes("archivo_proyecto"),
                                    rs.getBytes("carta_recomendacion")
                            );
                            return formato;
                        }
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return formato;
    }

    @Override
    public Object[] obtenerUltimoIntentoEstado(String correo, EnumModalidad modalidad) {
        String sql;
        if (modalidad == EnumModalidad.Investigacion) {
            sql = "SELECT intento, estado FROM FormatoAInvestigacion " +
                    "WHERE correo_estudiante_1 = ? OR correo_estudiante_2 = ? " +
                    "ORDER BY intento DESC LIMIT 1";
        } else {
            sql = "SELECT intento, estado FROM FormatoAPracticaProfesional " +
                    "WHERE correo_estudiante = ? " +
                    "ORDER BY intento DESC LIMIT 1";
        }

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            if (modalidad == EnumModalidad.Investigacion) {
                pstmt.setString(1, correo);
                pstmt.setString(2, correo);
            } else {
                pstmt.setString(1, correo);
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int intento = rs.getInt("intento");
                    String estado = rs.getString("estado");
                    return new Object[]{intento, estado};
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // No hay registros
    }


    @Override
    public List<DevolucionTabla> obtenerDevolucionesPorProfesor(int idProfesor) {
        List<DevolucionTabla> lista = new ArrayList<>();

        String sql =
                "SELECT d.id_devolucion, d.id_formato, f.titulo, " +
                        "       d.correo_estudiante_1, d.correo_estudiante_2, " +
                        "       (u.nombres || ' ' || u.apellidos) AS coordinador, " +
                        "       d.archivo_devolucion, d.modalidad " +
                        "FROM DevolucionFormatoA d " +
                        "JOIN FormatoAInvestigacion f ON f.id_formato = d.id_formato " +
                        "JOIN Usuario u ON u.id_usuario = d.id_coordinador " +
                        "WHERE d.id_profesor = ? " +
                        "  AND f.estado = 'Rechazado' " +
                        "  AND f.intento != 3 " +
                        "  AND d.id_formato = ( " +
                        "      SELECT MAX(f3.id_formato) " +
                        "      FROM FormatoAInvestigacion f3 " +
                        "      WHERE f3.correo_estudiante_1 = d.correo_estudiante_1 " +
                        "        AND ( (f3.correo_estudiante_2 IS NULL AND d.correo_estudiante_2 IS NULL) " +
                        "              OR f3.correo_estudiante_2 = d.correo_estudiante_2 ) " +
                        "  ) " +
                        "UNION " +
                        "SELECT d.id_devolucion, d.id_formato, f.titulo, " +
                        "       d.correo_estudiante_1, d.correo_estudiante_2, " +
                        "       (u.nombres || ' ' || u.apellidos) AS coordinador, " +
                        "       d.archivo_devolucion, d.modalidad " +
                        "FROM DevolucionFormatoA d " +
                        "JOIN FormatoAPracticaProfesional f ON f.id_formato = d.id_formato " +
                        "JOIN Usuario u ON u.id_usuario = d.id_coordinador " +
                        "WHERE d.id_profesor = ? " +
                        "  AND f.estado = 'Rechazado' " +
                        "  AND f.intento != 3 " +
                        "  AND d.id_formato = ( " +
                        "      SELECT MAX(f3.id_formato) " +
                        "      FROM FormatoAPracticaProfesional f3 " +
                        "      WHERE f3.correo_estudiante = d.correo_estudiante_1 " +
                        "  );";


        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:./ProyectoGestionDB.db");
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idProfesor);
            pstmt.setInt(2, idProfesor);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int idDevolucion = rs.getInt("id_devolucion");
                    String titulo = rs.getString("titulo");
                    int idFormato = rs.getInt("id_formato");
                    String tipoProyecto = rs.getString("modalidad");
                    String correo1 = rs.getString("correo_estudiante_1");
                    String correo2 = rs.getString("correo_estudiante_2");
                    String coordinador = rs.getString("coordinador");
                    byte[] archivo = rs.getBytes("archivo_devolucion");

                    DevolucionTabla devolucion = new DevolucionTabla(
                            idDevolucion, idFormato,titulo , tipoProyecto, correo1, correo2, coordinador, archivo
                    );
                    lista.add(devolucion);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }


    @Override
    public List<FormatoATabla> obtenerFormatos() {
        List<FormatoATabla> lista = new ArrayList<>();

        String sql = "SELECT id_formato, titulo, modalidad, director, codirector," +
                "       correo_estudiante_1 AS correo1, correo_estudiante_2 AS correo2," +
                "       id_profesor, fecha_actual, estado, intento " +
                "FROM FormatoAInvestigacion " +
                "WHERE estado = 'EnRevision'" +
                "UNION\n" +
                "SELECT id_formato, titulo, modalidad, director, codirector," +
                "       correo_estudiante AS correo1, NULL AS correo2," +
                "       id_profesor, fecha_actual, estado, intento " +
                "FROM FormatoAPracticaProfesional " +
                "WHERE estado = 'EnRevision'";

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:./ProyectoGestionDB.db");
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int idFormato = rs.getInt("id_formato");
                String titulo = rs.getString("titulo");
                String correo1 = rs.getString("correo1");
                String correo2 = rs.getString("correo2");
                String director = rs.getString("director");
                String codirector = rs.getString("codirector");
                String modalidad = rs.getString("modalidad");
                String fecha = rs.getString("fecha_actual");
                String estado = rs.getString("estado");

                lista.add(new FormatoATabla(
                        String.valueOf(idFormato), titulo,
                        correo1, correo2, director, codirector,
                        modalidad, fecha, estado
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }


    @Override
    public FormatoATabla obtenerFormatoPorCorreo(String correoBuscado) {
        FormatoATabla formato = null;

        String sql = "SELECT id_formato, titulo, modalidad, director, codirector, " +
                "correo_estudiante_1 AS correo1, correo_estudiante_2 AS correo2, " +
                "fecha_actual, estado " + "FROM FormatoAInvestigacion WHERE correo_estudiante_1 = ? OR correo_estudiante_2 = ? " + "UNION " +
                "SELECT id_formato, titulo, modalidad, director, codirector, " + "correo_estudiante AS correo1, NULL AS correo2, " +
                "fecha_actual, estado " + "FROM FormatoAPracticaProfesional WHERE correo_estudiante = ?";

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:./ProyectoGestionDB.db");
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, correoBuscado);
            pstmt.setString(2, correoBuscado);
            pstmt.setString(3, correoBuscado);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int idFormato = rs.getInt("id_formato");
                    String titulo = rs.getString("titulo");
                    String correo1 = rs.getString("correo1");
                    String correo2 = rs.getString("correo2");
                    String director = rs.getString("director");
                    String codirector = rs.getString("codirector");
                    String modalidad = rs.getString("modalidad");
                    String fecha = rs.getString("fecha_actual");
                    String estado = rs.getString("estado");

                    formato = new FormatoATabla(
                            String.valueOf(idFormato), titulo,
                            correo1, correo2, director, codirector,
                            modalidad, fecha, estado
                    );
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
    public int obtenerIdProfesorPorFormato(int idFormato, String tipoFormato) {
        String sql = "";

        // Escoger tabla según modalidad
        if ("Investigacion".equalsIgnoreCase(tipoFormato)) {
            sql = "SELECT id_profesor FROM FormatoAInvestigacion WHERE id_formato = ?";
        } else if ("PracticaProfesional".equalsIgnoreCase(tipoFormato)) {
            sql = "SELECT id_profesor FROM FormatoAPracticaProfesional WHERE id_formato = ?";
        } else {
            return -1; // tipo no válido
        }

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idFormato);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id_profesor");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // no encontrado
    }

    @Override
    public int obtenerNumeroDeIntentos(int idFormato) {
        String sql = "SELECT intento FROM FormatoAInvestigacion WHERE id_formato = ? " +
                "UNION " +
                "SELECT intento FROM FormatoAPracticaProfesional WHERE id_formato = ?";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idFormato);
            pstmt.setInt(2, idFormato);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("intento");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public FormatoATabla obtenerFormato(int idFormato) {
        FormatoATabla formato = null;

        String sql = "SELECT id_formato, titulo, modalidad, director, codirector, " +
                "correo_estudiante_1 AS correo1, correo_estudiante_2 AS correo2, fecha_actual, estado " +
                "FROM FormatoAInvestigacion WHERE id_formato = ? " +
                "UNION " +
                "SELECT id_formato, titulo, modalidad, director, codirector, " +
                "correo_estudiante AS correo1, NULL AS correo2, fecha_actual, estado " +
                "FROM FormatoAPracticaProfesional WHERE id_formato = ?";

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:./ProyectoGestionDB.db");
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idFormato);
            pstmt.setInt(2, idFormato);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String titulo = rs.getString("titulo");
                    String correo1 = rs.getString("correo1");
                    String correo2 = rs.getString("correo2");
                    String director = rs.getString("director");
                    String codirector = rs.getString("codirector");
                    String modalidad = rs.getString("modalidad");
                    String fecha = rs.getString("fecha_actual");
                    String estado = rs.getString("estado");

                    formato = new FormatoATabla(
                            String.valueOf(idFormato), titulo,
                            correo1, correo2, director, codirector,
                            modalidad, fecha, estado
                    );
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return formato;
    }

    @Override
    public boolean guardarDevolucionFormatoA(int idFormato, int idProfesor, int idCoordinador,
                                             String correoEstudiante1, String correoEstudiante2,
                                             byte[] archivoDevolucion, String modalidad, int intento) {
        String sql = "INSERT INTO DevolucionFormatoA (id_formato, id_profesor, id_coordinador, " +
                "correo_estudiante_1, correo_estudiante_2, archivo_devolucion, modalidad, intento) " +
                "VALUES (?, ?, ?, ?, ?, ?,?, ?)";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idFormato);
            pstmt.setInt(2, idProfesor);
            pstmt.setInt(3, idCoordinador);
            pstmt.setString(4, correoEstudiante1);
            pstmt.setString(5, correoEstudiante2);
            pstmt.setBytes(6, archivoDevolucion);
            pstmt.setString(7, modalidad);
            pstmt.setInt(8, intento);

            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al guardar devolución: " + e.getMessage());
            return false;
        }
    }
}
