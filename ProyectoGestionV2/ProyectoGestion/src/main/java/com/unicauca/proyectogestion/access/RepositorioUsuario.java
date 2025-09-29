package com.unicauca.proyectogestion.access;

import com.unicauca.proyectogestion.domain.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.unicauca.proyectogestion.service.ServicioUsuario;
import org.mindrot.jbcrypt.BCrypt;

public class RepositorioUsuario implements IRepositorioUsuario {

    private static final String URL = "jdbc:sqlite:./ProyectoGestionDB.db";

    public RepositorioUsuario() {
        initDatabase();
    }

    @Override
    public Profesor obtenerProfesorPorNombre(String nombreCompleto) {
        String sql = "SELECT * FROM Usuario WHERE (nombres || ' ' || apellidos) = ? AND rol = 'Profesor'";
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:./ProyectoGestionDB.db");
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nombreCompleto);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Profesor(
                            rs.getInt("id_usuario"),
                            rs.getString("nombres"),
                            rs.getString("apellidos"),
                            rs.getString("celular"),
                            EnumProgramas.valueOf(rs.getString("programa")),
                            EnumRoles.valueOf(rs.getString("rol")),
                            rs.getString("email"),
                            rs.getString("contrasena"),
                            null // area_investigacion no se obtiene aquí
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public boolean registrarUsuario(Usuario nuevoUsuario) throws SQLException {
        String contrasenaHash = BCrypt.hashpw(nuevoUsuario.getContrasenia(), BCrypt.gensalt());
        String sqlUsuario = "INSERT INTO Usuario (nombres, apellidos, celular, programa, rol, email, contrasena) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(URL)) {
            if (buscarEmail(conn, nuevoUsuario.getEmail())) {
                return false; // ya existe el correo
            }

            // Usamos RETURN_GENERATED_KEYS para obtener el id autoincremental
            try (PreparedStatement pstmt = conn.prepareStatement(sqlUsuario, Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setString(1, nuevoUsuario.getNombres());
                pstmt.setString(2, nuevoUsuario.getApellidos());
                pstmt.setString(3, nuevoUsuario.getCelular());
                pstmt.setString(4, nuevoUsuario.getPrograma().toString());
                pstmt.setString(5, nuevoUsuario.getRol().toString());
                pstmt.setString(6, nuevoUsuario.getEmail());
                pstmt.setString(7, contrasenaHash);

                pstmt.executeUpdate();

                // Recuperamos el ID generado
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        int idUsuario = rs.getInt(1);
                        String programa = nuevoUsuario.getPrograma().toString();

                        // Dependiendo del rol, insertamos en Estudiante o Profesor
                        if (nuevoUsuario.getRol().toString().equals("Estudiante")) {
                            System.out.println("Estudiante registrado");
                            String sqlEst = "INSERT INTO Estudiante (id_usuario) VALUES (?)";
                            try (PreparedStatement pstmtEst = conn.prepareStatement(sqlEst)) {
                                pstmtEst.setInt(1, idUsuario);
                                pstmtEst.executeUpdate();
                            }
                        } else if (nuevoUsuario.getRol().toString().equals("Profesor")) {
                            System.out.println("Profesor registrado");
                            String sqlProf = "INSERT INTO Profesor (id_usuario) VALUES (?)";
                            try (PreparedStatement pstmtProf = conn.prepareStatement(sqlProf)) {
                                pstmtProf.setInt(1, idUsuario);
                                pstmtProf.executeUpdate();
                            }
                        } else {
                            System.out.println("Coordinador registrado");
                            String sqlProf = "INSERT INTO Coordinador (id_usuario, facultad) VALUES (?, ?)";
                            try (PreparedStatement pstmtProf = conn.prepareStatement(sqlProf)) {
                                pstmtProf.setInt(1, idUsuario);
                                pstmtProf.setString(2, programa);
                                pstmtProf.executeUpdate();
                            }
                        }
                    }
                }
            }

            System.out.println("Usuario registrado correctamente.");
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ServicioUsuario.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        }
    }


    public boolean buscarEmail(Connection conn, String email) throws SQLException {
        String sqlValidacion = "SELECT email FROM Usuario WHERE email = ?";
        try (PreparedStatement statement = conn.prepareStatement(sqlValidacion)) {
            statement.setString(1, email);
            try (ResultSet rs = statement.executeQuery()) {
                return rs.next();
            }
        }
    }
    
    public String obtenerRolUsuario(String email) {
    String sql = "SELECT rol FROM Usuario WHERE email = ?";

    try (Connection conn = DriverManager.getConnection(URL);
         PreparedStatement pstmt = conn.prepareStatement(sql)) {

        pstmt.setString(1, email);

        try (ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                return rs.getString("rol"); // Puede ser "Docente" o "Estudiante"
            }
        }
    } catch (SQLException ex) {
        Logger.getLogger(ServicioUsuario.class.getName()).log(Level.SEVERE, null, ex);
    }
    return null; // Si no existe el usuario
}
    
    public Usuario obtenerUsuarioPorEmail(String email) {
        String sql = "SELECT * FROM Usuario WHERE email = ?";

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:./ProyectoGestionDB.db");
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, email);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String rol = rs.getString("rol");
                    if ("Estudiante".equals(rol)) {
                        // Consultar estado_proyecto de Estudiante
                        String sqlEst = "SELECT estado_proyecto FROM Estudiante WHERE id_usuario = ?";
                        try (PreparedStatement pstmtEst = conn.prepareStatement(sqlEst)) {
                            pstmtEst.setInt(1, rs.getInt("id_usuario"));
                            try (ResultSet rsEst = pstmtEst.executeQuery()) {
                                String estadoProyecto = rsEst.next() ? rsEst.getString("estado_proyecto") : null;
                                return new Estudiante(
                                        rs.getInt("id_usuario"),
                                        rs.getString("nombres"),
                                        rs.getString("apellidos"),
                                        rs.getString("celular"),
                                        EnumProgramas.valueOf(rs.getString("programa")),
                                        EnumRoles.valueOf(rol),
                                        rs.getString("email"),
                                        null,
                                        estadoProyecto
                                );
                            }
                        }
                    } else if ("Profesor".equals(rol)) {
                        // Consultar area_investigacion de Profesor
                        String sqlProf = "SELECT area_investigacion FROM Profesor WHERE id_usuario = ?";
                        try (PreparedStatement pstmtProf = conn.prepareStatement(sqlProf)) {
                            pstmtProf.setInt(1, rs.getInt("id_usuario"));
                            try (ResultSet rsProf = pstmtProf.executeQuery()) {
                                String areaInvestigacion = rsProf.next() ? rsProf.getString("area_investigacion") : null;
                                return new Profesor(
                                        rs.getInt("id_usuario"),
                                        rs.getString("nombres"),
                                        rs.getString("apellidos"),
                                        rs.getString("celular"),
                                        EnumProgramas.valueOf(rs.getString("programa")),
                                        EnumRoles.valueOf(rol),
                                        rs.getString("email"),
                                        null,
                                        areaInvestigacion
                                );
                            }
                        }
                    }
                    else if ("Coordinador".equals(rol)) {
                        // Consultar area_investigacion de Profesor
                        String sqlCoord = "SELECT facultad  FROM Coordinador WHERE id_usuario = ?";
                        try (PreparedStatement pstmtCoord = conn.prepareStatement(sqlCoord)) {
                            pstmtCoord.setInt(1, rs.getInt("id_usuario"));
                            try (ResultSet rsCoord = pstmtCoord.executeQuery()) {
                                String facultad = rsCoord.next() ? rsCoord.getString("facultad") : null;
                                return new Coordinador(
                                        rs.getInt("id_usuario"),
                                        rs.getString("nombres"),
                                        rs.getString("apellidos"),
                                        rs.getString("celular"),
                                        EnumProgramas.valueOf(rs.getString("programa")),
                                        EnumRoles.valueOf(rol),
                                        rs.getString("email"),
                                        null,
                                        facultad
                                );
                            }
                        }
                    }
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(ServicioUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public Usuario obtenerUsuarioPorCodigo(String codigo) {
        String sql = "SELECT * FROM Usuario WHERE codigo = ?";

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:./ProyectoGestionDB.db");
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, codigo);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String rol = rs.getString("rol");
                    if ("Estudiante".equals(rol)) {
                        // Consultar estado_proyecto de Estudiante
                        String sqlEst = "SELECT estado_proyecto FROM Estudiante WHERE id_usuario = ?";
                        try (PreparedStatement pstmtEst = conn.prepareStatement(sqlEst)) {
                            pstmtEst.setInt(1, rs.getInt("id_usuario"));
                            try (ResultSet rsEst = pstmtEst.executeQuery()) {
                                String estadoProyecto = rsEst.next() ? rsEst.getString("estado_proyecto") : null;
                                return new Estudiante(
                                        rs.getInt("id_usuario"),
                                        rs.getString("nombres"),
                                        rs.getString("apellidos"),
                                        rs.getString("celular"),
                                        EnumProgramas.valueOf(rs.getString("programa")),
                                        EnumRoles.valueOf(rol),
                                        rs.getString("email"),
                                        null,
                                        estadoProyecto
                                );
                            }
                        }
                    } else if ("Profesor".equals(rol)) {
                        // Consultar area_investigacion de Profesor
                        String sqlProf = "SELECT area_investigacion FROM Profesor WHERE id_usuario = ?";
                        try (PreparedStatement pstmtProf = conn.prepareStatement(sqlProf)) {
                            pstmtProf.setInt(1, rs.getInt("id_usuario"));
                            try (ResultSet rsProf = pstmtProf.executeQuery()) {
                                String areaInvestigacion = rsProf.next() ? rsProf.getString("area_investigacion") : null;
                                return new Profesor(
                                        rs.getInt("id_usuario"),
                                        rs.getString("nombres"),
                                        rs.getString("apellidos"),
                                        rs.getString("celular"),
                                        EnumProgramas.valueOf(rs.getString("programa")),
                                        EnumRoles.valueOf(rol),
                                        rs.getString("email"),
                                        null,
                                        areaInvestigacion
                                );
                            }
                        }
                    } else if ("Coordinador".equals(rol)) {
                        // Consultar facultad de Coordinador
                        String sqlCoord = "SELECT facultad FROM Coordinador WHERE id_usuario = ?";
                        try (PreparedStatement pstmtCoord = conn.prepareStatement(sqlCoord)) {
                            pstmtCoord.setInt(1, rs.getInt("id_usuario"));
                            try (ResultSet rsCoord = pstmtCoord.executeQuery()) {
                                String facultad = rsCoord.next() ? rsCoord.getString("facultad") : null;
                                return new Coordinador(
                                        rs.getInt("id_usuario"),
                                        rs.getString("nombres"),
                                        rs.getString("apellidos"),
                                        rs.getString("celular"),
                                        EnumProgramas.valueOf(rs.getString("programa")),
                                        EnumRoles.valueOf(rol),
                                        rs.getString("email"),
                                        null,
                                        facultad
                                );
                            }
                        }
                    }
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(ServicioUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }


    @Override
    public boolean iniciarSesion(String email, String contrasenia) {
        String sql = "SELECT contrasena FROM Usuario WHERE email = ?";

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, email);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String contrasenaHash = rs.getString("contrasena");
                    return BCrypt.checkpw(contrasenia, contrasenaHash);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(ServicioUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    @Override
    public boolean actualizarEstadoEstudiantePorCorreo(String correo, String nuevoEstado) {
        String sql = "UPDATE Estudiante SET estado_proyecto = ? " +
                "WHERE id_usuario IN ( " +
                "    SELECT u.id_usuario " +
                "    FROM Usuario u " +
                "    INNER JOIN Estudiante e ON u.id_usuario = e.id_usuario " +
                "    WHERE u.email = ? " +
                ")";

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nuevoEstado);
            pstmt.setString(2, correo);

            int rows = pstmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<Profesor> listarProfesores() {
        List<Profesor> profesores = new ArrayList<>();
        String sql = "SELECT u.id_usuario, u.nombres, u.apellidos, u.celular, u.programa, u.rol, u.email, p.area_investigacion " +
                "FROM Usuario u " +
                "INNER JOIN Profesor p ON u.id_usuario = p.id_usuario " +
                "WHERE u.rol = 'Profesor'";

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:./ProyectoGestionDB.db");
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Profesor profesor = new Profesor(
                        rs.getInt("id_usuario"),
                        rs.getString("nombres"),
                        rs.getString("apellidos"),
                        rs.getString("celular"),
                        EnumProgramas.valueOf(rs.getString("programa")),
                        EnumRoles.valueOf(rs.getString("rol")),
                        rs.getString("email"),
                        null, // no recuperamos contraseña aquí por seguridad
                        rs.getString("area_investigacion")
                );
                profesores.add(profesor);
            }

        } catch (SQLException ex) {
            Logger.getLogger(ServicioUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }
        return profesores;
    }
    @Override
    public Estudiante obtenerEstudiantePorCorreo(String email) {
        String sql = "SELECT u.*, e.estado_proyecto " +
                "FROM Usuario u " +
                "JOIN Estudiante e ON u.id_usuario = e.id_usuario " +
                "WHERE u.email = ?";

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:./ProyectoGestionDB.db");
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, email);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Estudiante(
                            rs.getInt("id_usuario"),
                            rs.getString("nombres"),
                            rs.getString("apellidos"),
                            rs.getString("celular"),
                            EnumProgramas.valueOf(rs.getString("programa")),
                            EnumRoles.valueOf(rs.getString("rol")),
                            rs.getString("email"),
                            null, // no devolvemos la contraseña
                            rs.getString("estado_proyecto")
                    );
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(ServicioUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }


    private void initDatabase() {
        // SQL statement for creating a new table
        String sqlUsuario = "CREATE TABLE IF NOT EXISTS Usuario (\n"
                + " id_usuario INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                + " nombres TEXT NOT NULL,\n"
                + " apellidos TEXT NOT NULL,\n"
                + " celular TEXT,\n"
                + " programa TEXT NOT NULL CHECK (programa IN ('Ingeniería_de_Sistemas', 'Ingeniería_Electrónica_y_Telecomunicaciones', 'Automática_industrial', 'Tecnología_en_Telemática')),\n"
                + " rol TEXT NOT NULL CHECK (rol IN ('Estudiante', 'Profesor', 'Coordinador')),\n"
                + " email TEXT UNIQUE NOT NULL,\n"
                + " contrasena TEXT NOT NULL\n"
                + ");";

        String sqlEstudiante = "CREATE TABLE IF NOT EXISTS Estudiante (\n"
                + " id_usuario INTEGER PRIMARY KEY,\n"
                + " estado_proyecto TEXT CHECK (estado_proyecto IN (\n"
                + "     'NoAsignado',\n"
                + "     'PrimerRevision',\n"
                + "     'SegundaRevision',\n"
                + "     'TercerRevision',\n"
                + "     'Rechazado',\n"
                + "     'Aprobado'\n"
                + " )) DEFAULT 'NoAsignado',\n"
                + " FOREIGN KEY (id_usuario) REFERENCES Usuario(id_usuario) ON DELETE CASCADE\n"
                + ");";


        String sqlProfesor = "CREATE TABLE IF NOT EXISTS Profesor (\n"
                + " id_usuario INTEGER PRIMARY KEY,\n"
                + " area_investigacion TEXT,\n"
                + " FOREIGN KEY (id_usuario) REFERENCES Usuario(id_usuario) ON DELETE CASCADE\n"
                + ");";

        String sqlCoordinador = "CREATE TABLE IF NOT EXISTS Coordinador (\n"
                + " id_usuario INTEGER PRIMARY KEY,\n"
                + " facultad TEXT,\n"
                + " FOREIGN KEY (id_usuario) REFERENCES Usuario(id_usuario) ON DELETE CASCADE\n"
                + ");";

        String sqlFormatoAInv = "CREATE TABLE IF NOT EXISTS FormatoAInvestigacion (\n"
                + " id_formato INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                + " titulo TEXT NOT NULL,\n"
                + " modalidad TEXT CHECK (modalidad IN ('Investigacion',  'PracticaProfesional')),\n"
                + " fecha_actual TEXT NOT NULL,\n"
                + " director TEXT,\n"
                + " codirector TEXT,\n"
                + " correo_estudiante_1 TEXT NOT NULL,\n"
                + " correo_estudiante_2 TEXT,\n"
                + " id_profesor INTEGER NOT NULL,\n"
                + " objetivo_general TEXT,\n"
                + " objetivos_especificos TEXT,\n"
                + " archivo_proyecto BLOB,\n"
                + " intento INTEGER DEFAULT 1 CHECK (intento BETWEEN 1 AND 3),"
                + " estado TEXT DEFAULT 'EnRevision' CHECK (estado IN ('EnRevision', 'Aprobado', 'Rechazado')),\n"
                + " FOREIGN KEY (correo_estudiante_1) REFERENCES Usuario(email) ON DELETE CASCADE,\n"
                + " FOREIGN KEY (correo_estudiante_2) REFERENCES Usuario(email) ON DELETE CASCADE,\n"
                + " FOREIGN KEY (id_profesor) REFERENCES Profesor(id_usuario) ON DELETE CASCADE\n"
                + ");";

        String sqlFormatoAPas = "CREATE TABLE IF NOT EXISTS FormatoAPracticaProfesional (\n"
                + " id_formato INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                + " titulo TEXT NOT NULL,\n"
                + " modalidad TEXT CHECK (modalidad IN ('Investigacion', 'PracticaProfesional')),\n"
                + " fecha_actual TEXT NOT NULL,\n"
                + " director TEXT,\n"
                + " codirector TEXT,\n"
                + " correo_estudiante TEXT NOT NULL,\n"
                + " id_profesor INTEGER NOT NULL,\n"
                + " objetivo_general TEXT,\n"
                + " objetivos_especificos TEXT,\n"
                + " archivo_proyecto BLOB,\n"
                + " carta_recomendacion BLOB,\n"
                + " intento INTEGER DEFAULT 1 CHECK (intento BETWEEN 1 AND 3),"
                + " estado TEXT DEFAULT 'EnRevision' CHECK (estado IN ('EnRevision', 'Aprobado', 'Rechazado')),\n"
                + " FOREIGN KEY (correo_estudiante) REFERENCES Usuario(email) ON DELETE CASCADE,\n"
                + " FOREIGN KEY (id_profesor) REFERENCES Profesor(id_usuario) ON DELETE CASCADE\n"
                + ");";

        String sqlDevolucionFormatoA = "CREATE TABLE IF NOT EXISTS DevolucionFormatoA (\n"
                + " id_devolucion INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                + " id_formato INTEGER NOT NULL,\n"
                + " id_profesor INTEGER NOT NULL,\n"
                + " id_coordinador INTEGER NOT NULL,\n"
                + " correo_estudiante_1 TEXT NOT NULL,\n"
                + " correo_estudiante_2 TEXT,\n"
                + " archivo_devolucion BLOB,\n"
                + " modalidad TEXT CHECK (modalidad IN ('Investigacion', 'PracticaProfesional')),\n"
                + " intento INTEGER DEFAULT 1,\n"
                + " FOREIGN KEY (id_formato) REFERENCES FormatoAInvestigacion(id_formato) ON DELETE CASCADE,\n"
                + " FOREIGN KEY (correo_estudiante_1) REFERENCES Usuario(email) ON DELETE CASCADE,\n"
                + " FOREIGN KEY (correo_estudiante_2) REFERENCES Usuario(email) ON DELETE CASCADE,\n"
                + " FOREIGN KEY (id_profesor) REFERENCES Profesor(id_usuario) ON DELETE CASCADE,\n"
                + " FOREIGN KEY (id_coordinador) REFERENCES Coordinador(id_usuario) ON DELETE CASCADE\n"
                + ");";

        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sqlUsuario);
            stmt.execute(sqlEstudiante);
            stmt.execute(sqlProfesor);
            stmt.execute(sqlCoordinador);
            stmt.execute(sqlFormatoAInv);
            stmt.execute(sqlFormatoAPas);
            stmt.execute(sqlDevolucionFormatoA);
        } catch (SQLException ex) {
            Logger.getLogger(ServicioUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
