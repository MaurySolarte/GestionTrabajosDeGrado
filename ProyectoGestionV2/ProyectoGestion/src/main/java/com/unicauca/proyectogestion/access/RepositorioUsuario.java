package com.unicauca.proyectogestion.access;

import com.unicauca.proyectogestion.domain.*;
import com.unicauca.proyectogestion.service.Servicio;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.mindrot.jbcrypt.BCrypt;

public class RepositorioUsuario implements IRepositorioUsuario {

    private static final String URL = "jdbc:sqlite:./ProyectoGestionDB.db";

    public RepositorioUsuario() {
        initDatabase();
    }

    @Override
    public boolean registrarUsuario(Usuario nuevoUsuario) throws SQLException {
        String contrasenaHash = BCrypt.hashpw(nuevoUsuario.getContrasenia(), BCrypt.gensalt());
        String sql = "INSERT INTO Usuario VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(URL)) {
            if (buscarEmail(conn, nuevoUsuario.getEmail())) {
                return false;
            }

            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, nuevoUsuario.getNombres());
                pstmt.setString(2, nuevoUsuario.getApellidos());
                pstmt.setDouble(3, nuevoUsuario.getCelular()); // sigue siendo double
                pstmt.setString(4, nuevoUsuario.getPrograma().toString());
                pstmt.setString(5, nuevoUsuario.getRol().toString());
                pstmt.setString(6, nuevoUsuario.getEmail());
                pstmt.setString(7, contrasenaHash);
                pstmt.executeUpdate();
            }
            System.out.println("Usuario registrado.");
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(Servicio.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        }
    }

    private boolean buscarEmail(Connection conn, String email) throws SQLException {
        String sqlValidacion = "SELECT email FROM Usuario WHERE email = ?";
        try (PreparedStatement statement = conn.prepareStatement(sqlValidacion)) {
            statement.setString(1, email);
            try (ResultSet rs = statement.executeQuery()) {
                return rs.next();
            }
        }
    }
    
    public Usuario obtenerUsuarioPorEmail(String email) {
        String sql = "SELECT * FROM Usuario WHERE email = ?";

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:./ProyectoGestionDB.db");
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, email);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Usuario(
                            rs.getString("nombres"),
                            rs.getString("apellidos"),
                            rs.getInt("celular"),
                            EnumProgramas.valueOf(rs.getString("programa")), // depende de tu enum
                            EnumRoles.valueOf(rs.getString("rol")),
                            rs.getString("email"),
                            null
                    );
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(Servicio.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null; // si no lo encuentra
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
            Logger.getLogger(Servicio.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    private void initDatabase() {
        // SQL statement for creating a new table
        String sql = "CREATE TABLE IF NOT EXISTS Usuario (\n"
                + "	nombres text NOT NULL,\n"
                + "	apellidos text NOT NULL,\n"
                + "	celular real,\n" // lo dejo como REAL
                + "	programa text NOT NULL CHECK (programa IN ('Ingeniería_de_Sistemas', 'Ingeniería_Electrónica_y_Telecomunicaciones', 'Automática_industrial', 'Tecnología_en_Telemática')),\n"
                + "	rol text NOT NULL CHECK (rol IN ('Docente', 'Estudiante')),\n"
                + "	email text PRIMARY KEY,\n"
                + "	contrasena text NOT NULL \n"                 
                + ");";

        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);

        } catch (SQLException ex) {
            Logger.getLogger(Servicio.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
