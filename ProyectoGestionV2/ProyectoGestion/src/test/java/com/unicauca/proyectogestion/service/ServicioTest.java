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
        Usuario nuevoUsuario = new Usuario("Nelson Rodrigo", "Lopez Vidales","3216169841", EnumProgramas.Ingeniería_de_Sistemas, EnumRoles.Estudiante, "nelsonl2v@unicauca.edu.co", "Nelson12?");
        IRepositorioUsuario repository = new RepositorioUsuario();
        Servicio instance = new Servicio(repository);
        boolean expResult = true;
        boolean result = instance.registrarUsuario(nuevoUsuario);
        assertEquals(expResult, result);
    }    
    
    @Test
    public void testRegistrarUsuario1() throws Exception {
        System.out.println("registrarUsuarioRepetido");        
        Usuario nuevoUsuario = new Usuario("Nelson Rodrigo", "Lopez Vidales","3216169841", EnumProgramas.Ingeniería_de_Sistemas, EnumRoles.Estudiante, "nelsonlv@unicauca.edu.co", "Nelson123?");
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

    @Test
    public void testObtenerUsuarioPorEmail() {
        System.out.println("Obtener usuario por email existente.");
        String email = "maury@unicauca.edu.co";
        IRepositorioUsuario repository = new RepositorioUsuario();
        Servicio instance = new Servicio(repository);
        Usuario expResult = new Usuario("Mauricio", "Solarte", "3045661375", EnumProgramas.Ingeniería_de_Sistemas, EnumRoles.Estudiante, "maury@unicauca.edu.co", null);
        Usuario result = instance.obtenerUsuarioPorEmail(email);   
        assertEquals(expResult.getNombres(), result.getNombres());
        assertEquals(expResult.getApellidos(), result.getApellidos());
        assertEquals(expResult.getCelular(), result.getCelular());
        assertEquals(expResult.getPrograma(), result.getPrograma());
        assertEquals(expResult.getRol(), result.getRol());
        assertEquals(expResult.getEmail(), result.getEmail());     
        
    }
    
    @Test
    public void testObtenerUsuarioPorEmail1() {
        System.out.println("Obtener usuario por email no existente.");
        String email = "maury2@unicauca.edu.co";
        IRepositorioUsuario repository = new RepositorioUsuario();
        Servicio instance = new Servicio(repository);
        Usuario expResult = null;
        Usuario result = instance.obtenerUsuarioPorEmail(email);
        assertEquals(expResult, result);
        
    }

    @Test
    public void testObtenerRolUsuario() {
        System.out.println("Obtener rol de usuario existente.");
        String email = "maury@unicauca.edu.co";
        IRepositorioUsuario repository = new RepositorioUsuario();
        Servicio instance = new Servicio(repository);        
        String expResult = "Estudiante";
        String result = instance.obtenerRolUsuario(email);
        assertEquals(expResult, result);
        
    }
    
    public void testObtenerRolUsuario1() {
        System.out.println("Obtener rol de usuario no existente.");
        String email = "maury2@unicauca.edu.co";
        IRepositorioUsuario repository = new RepositorioUsuario();
        Servicio instance = new Servicio(repository);        
        String expResult = null;
        String result = instance.obtenerRolUsuario(email);
        assertEquals(expResult, result);
        
    }


    @Test
    public void testValidarCorreoInstitucional() {
        System.out.println("Validar correo institucional exitoso");
        String correo = "maury@unicauca.edu.co";
        IRepositorioUsuario repository = new RepositorioUsuario();
        Servicio instance = new Servicio(repository);  
        String expResult = "OK";
        String result = instance.validarCorreoInstitucional(correo);
        assertEquals(expResult, result);
        
    }
    
    @Test
    public void testValidarCorreoInstitucional1() {
        System.out.println("Validar correo institucional fallido, no pertenece a unicauca.");
        String correo = "maury@gmail.com";
        IRepositorioUsuario repository = new RepositorioUsuario();
        Servicio instance = new Servicio(repository);  
        String expResult = "El correo debe pertenecer al dominio @unicauca.edu.co.";
        String result = instance.validarCorreoInstitucional(correo);
        assertEquals(expResult, result);
        
    }
    
    @Test
    public void testValidarCorreoInstitucional2() {
        System.out.println("Validar correo institucional fallido, no contiene @");
        String correo = "maury.gmail.com";
        IRepositorioUsuario repository = new RepositorioUsuario();
        Servicio instance = new Servicio(repository);  
        String expResult = "El correo debe contener el carácter '@'.";
        String result = instance.validarCorreoInstitucional(correo);
        assertEquals(expResult, result);
        
    }
    
    @Test
    public void testValidarCorreoInstitucional3() {
        System.out.println("Validar correo institucional fallido, correo vacío.");
        String correo = "";
        IRepositorioUsuario repository = new RepositorioUsuario();
        Servicio instance = new Servicio(repository);  
        String expResult = "El correo no puede estar vacío.";
        String result = instance.validarCorreoInstitucional(correo);
        assertEquals(expResult, result);
        
    }
}
