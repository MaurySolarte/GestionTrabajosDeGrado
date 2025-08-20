package com.unicauca.proyectogestion.service;

import com.unicauca.proyectogestion.access.IRepositorioUsuario;
import com.unicauca.proyectogestion.access.RepositorioUsuario;
import com.unicauca.proyectogestion.domain.EnumProgramas;
import com.unicauca.proyectogestion.domain.EnumRoles;
import com.unicauca.proyectogestion.domain.Usuario;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ServicioTest {
    
    @Test
    public void testRegistrarUsuario() throws Exception {
        System.out.println("registrarUsuarioNuevo");        
        Usuario nuevoUsuario = new Usuario("Nelson Rodrigo", "Lopez Vidales",3216169841.0, EnumProgramas.Ingeniería_de_Sistemas, EnumRoles.Estudiante, "nelsonlv@unicauca.edu.co", "Nelson12?");
        IRepositorioUsuario repository = new RepositorioUsuario();
        Servicio instance = new Servicio(repository);
        boolean expResult = true;
        boolean result = instance.registrarUsuario(nuevoUsuario);
        assertEquals(expResult, result);
    }    
    
    @Test
    public void testRegistrarUsuario1() throws Exception {
        System.out.println("registrarUsuarioRepetido");        
        Usuario nuevoUsuario = new Usuario("Nelson Rodrigo", "Lopez Vidales",3216169841.0, EnumProgramas.Ingeniería_de_Sistemas, EnumRoles.Estudiante, "nelsonlv@unicauca.edu.co", "Nelson123?");
        IRepositorioUsuario repository = new RepositorioUsuario();
        Servicio instance = new Servicio(repository);
        boolean expResult = false;
        boolean result = instance.registrarUsuario(nuevoUsuario);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testIniciarSesion() {
        System.out.println("iniciarSesionValido");
        String email = "nelsonlv@unicauca.edu.co";
        String contrasenia = "Nelson123?";
        IRepositorioUsuario repository = new RepositorioUsuario();
        Servicio instance = new Servicio(repository);
        int expResult = 1;
        int result = instance.iniciarSesion(email, contrasenia);
        assertEquals(expResult, result);        
    }
    
    @Test
    public void testIniciarSesion1() {
        System.out.println("iniciarSesionCorreoInvalido");
        String email = "";
        String contrasenia = "Nelson123?";
        IRepositorioUsuario repository = new RepositorioUsuario();
        Servicio instance = new Servicio(repository);
        int expResult = 2;
        int result = instance.iniciarSesion(email, contrasenia);
        assertEquals(expResult, result);        
    }
    
    @Test
    public void testIniciarSesion2() {
        System.out.println("iniciarSesionContraseniaInvalida");
        String email = "nelsonlv@unicauca.edu.co";
        String contrasenia = "";
        IRepositorioUsuario repository = new RepositorioUsuario();
        Servicio instance = new Servicio(repository);
        int expResult = 2;
        int result = instance.iniciarSesion(email, contrasenia);
        assertEquals(expResult, result);        
    }

    @Test
    public void testIniciarSesion3() {
        System.out.println("iniciarSesionInvalido");
        String email = "n@unicauca.edu.co";
        String contrasenia = "N";
        IRepositorioUsuario repository = new RepositorioUsuario();
        Servicio instance = new Servicio(repository);
        int expResult = 0;
        int result = instance.iniciarSesion(email, contrasenia);
        assertEquals(expResult, result);        
    }
    
    @Test
    public void testValidarContrasenaSegura() {
        System.out.println("validarContrasenaSeguraCorrecta");
        String contrasena = "Nelson123?";
        IRepositorioUsuario repository = new RepositorioUsuario();
        Servicio instance = new Servicio(repository);
        String expResult = "OK";
        String result = instance.validarContrasenaSegura(contrasena);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testValidarContrasenaSegura1() {
        System.out.println("validarContrasenaSeguraErrorLongitud");
        String contrasena = "N";
        IRepositorioUsuario repository = new RepositorioUsuario();
        Servicio instance = new Servicio(repository);
        String expResult = "La contraseña debe tener al menos 6 caracteres.";
        String result = instance.validarContrasenaSegura(contrasena);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testValidarContrasenaSegura2() {
        System.out.println("validarContrasenaSeguraErrorMayus");
        String contrasena = "nelson123";
        IRepositorioUsuario repository = new RepositorioUsuario();
        Servicio instance = new Servicio(repository);
        String expResult = "La contraseña debe contener al menos una letra mayúscula.";
        String result = instance.validarContrasenaSegura(contrasena);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testValidarContrasenaSegura3() {
        System.out.println("validarContrasenaSeguraErrorNumero");
        String contrasena = "NelsonLV";
        IRepositorioUsuario repository = new RepositorioUsuario();
        Servicio instance = new Servicio(repository);
        String expResult = "La contraseña debe contener al menos un número.";
        String result = instance.validarContrasenaSegura(contrasena);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testValidarContrasenaSegura4() {
        System.out.println("validarContrasenaSeguraErrorCaracter");
        String contrasena = "Nelson123";
        IRepositorioUsuario repository = new RepositorioUsuario();
        Servicio instance = new Servicio(repository);
        String expResult = "La contraseña debe contener al menos un carácter especial.";
        String result = instance.validarContrasenaSegura(contrasena);
        assertEquals(expResult, result);
    }
    
    
    
}
