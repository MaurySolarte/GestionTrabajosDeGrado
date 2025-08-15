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

public class RepositorioUsuario implements IRepositorioUsuario{
    private Connection conn;
    
    public boolean registrarUsuario(Usuario nuevoUsuario){
        String contrasenaHash = BCrypt.hashpw(nuevoUsuario.getContrasenia(), BCrypt.gensalt());
        
        try{
            //this.connect();
            String sql = "INSERT INTO Usuario VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, nuevoUsuario.getNombres());
            pstmt.setString(2, nuevoUsuario.getApellidos());
            pstmt.setInt(3, nuevoUsuario.getCelular());
            pstmt.setString(4, nuevoUsuario.getPrograma().toString());
            pstmt.setString(5, nuevoUsuario.getRol().toString());
            pstmt.setString(6, nuevoUsuario.getEmail());
            pstmt.setString(7, contrasenaHash);
            pstmt.executeUpdate();
            
            System.out.println("Usuario registrado.");
            return true;
        } catch(SQLException ex){
            Logger.getLogger(Servicio.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
     
    }
    
    public boolean iniciarSesion(Usuario usuario){
        
        try{
            String sql = "SELECT contrasena FROM Usuario WHERE email = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, usuario.getEmail());
            ResultSet rs = pstmt.executeQuery();
            
            if(rs.next()){
                String contrasenaHash = rs.getString("contrasena");
                return BCrypt.checkpw(usuario.getContrasenia(), contrasenaHash);
            }
            
        } catch(SQLException ex){
            Logger.getLogger(Servicio.class.getName()).log(Level.SEVERE, null, ex);
        }            
        
        return false;
    }
    
    private void initDatabase() {
        // SQL statement for creating a new table
        String sql = "CREATE TABLE IF NOT EXISTS Usuario (\n"
                + "	nombres text NOT NULL,\n"
                + "	apellidos text NOT NULL,\n"
                + "	celular int,\n"
                + "	programa text NOT NULL CHECK (programa IN ('Ingeniería_de_Sistemas', 'Ingeniería_Electrónica_y_Telecomunicaciones', 'Automática_industrial', 'Tecnología_en_Telemática')),\n"
                + "	rol text CHECK NOT NULL(rol IN ('Docente', 'Estudiante')),\n"
                + "	email text PRIMARY KEY,\n"
                + "	contrasena text NOT NULL,\n"                 
                + ");";

        try {
            this.connect();
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
            //this.disconnect();

        } catch (SQLException ex) {
            Logger.getLogger(Servicio.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void connect() {
        // SQLite connection string
        String url = "jdbc:sqlite:./ProyectoGestionDB.db";
        //String url = "jdbc:sqlite::memory:";

        try {
            conn = DriverManager.getConnection(url);

        } catch (SQLException ex) {
            Logger.getLogger(Servicio.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void disconnect() {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

    }

}
