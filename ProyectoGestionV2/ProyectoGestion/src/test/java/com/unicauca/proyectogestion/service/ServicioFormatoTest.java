
package com.unicauca.proyectogestion.service;
import com.unicauca.proyectogestion.access.IRepositorioFormatoA;
import com.unicauca.proyectogestion.access.IRepositorioUsuario;
import com.unicauca.proyectogestion.access.RepositorioFormatoA;
import com.unicauca.proyectogestion.access.RepositorioUsuario;
import com.unicauca.proyectogestion.domain.*;
import com.unicauca.proyectogestion.utilities.DevolucionTabla;
import com.unicauca.proyectogestion.utilities.FormatoATabla;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author angma
 */

public class ServicioFormatoTest {
    
    public ServicioFormatoTest() {
    }


  @Test
public void testGuardarArchivoEnBD() {
     IRepositorioFormatoA repository = new RepositorioFormatoA();
    ServicioFormatoA instance = new ServicioFormatoA(repository);
    System.out.println("guardarArchivoEnBD");
    File fileFormato = new File("src/test/resources/formatoPrueba.pdf");
    File fileCarta = new File("src/test/resources/cartaPrueba.pdf");
    Profesor profesor = new Profesor("Jose Luis", "Mendez", "3015555532", 
            EnumProgramas.Ingeniería_de_Sistemas, EnumRoles.Profesor, 
            "jose@unicauca.edu.co", "JoseLuis123?", "Mecanica");
      Profesor profesor2 = new Profesor("Luis", "Mendez", "3015555532",
              EnumProgramas.Ingeniería_de_Sistemas, EnumRoles.Profesor,
              "sito@unicauca.edu.co", "JoseLuis123?", "Mecanica");
    Estudiante estudiante = new Estudiante("Gladys", "Astudillo", "135135165", 
            EnumProgramas.Ingeniería_de_Sistemas, EnumRoles.Profesor, 
            "gladys@unicauca.edu.co","Gladys123?", "Aprobado");
    String tipo = "formato_a";
    FormatoA formato = new FormatoAInvestigacion("1",EnumModalidad.Investigacion,LocalDate.EPOCH, profesor, profesor2, estudiante, null, profesor2, "sdddddddddddddd","ddddddddddddddddd");
    formato.setTitulo("Proyecto de prueba");
    formato.setModalidad(EnumModalidad.Investigacion);
    formato.setDirector(profesor);
    formato.setEstudiante(estudiante);
    System.out.println("¿Existe fileFormato?: " + fileFormato.exists());
    System.out.println("¿Existe fileCarta?: " + fileCarta.exists());
    // Verificamos que no lance excepción
    assertDoesNotThrow(() -> {
        instance.guardarArchivoEnBD(fileFormato, fileCarta, tipo, formato);
    }, "El método lanzó una excepción inesperada");
}
    @Test
    public void testGuardarArchivoEnBD3() throws IOException {
        IRepositorioFormatoA repository = new RepositorioFormatoA();
        ServicioFormatoA instance = new ServicioFormatoA(repository);
        System.out.println("guardarArchivoEnBD");
        byte[] archivoProyecto = Files.readAllBytes(Paths.get("src/test/resources/formatoPrueba.pdf"));
        byte[] cartaEmpresa = Files.readAllBytes(Paths.get("src/test/resources/cartaPrueba.pdf"));

        Profesor profesor = new Profesor("Juan Pablo", "Escobar", "885555532",
                EnumProgramas.Ingeniería_de_Sistemas, EnumRoles.Profesor,
                "pablo@unicauca.edu.co", "Pablo123?", "Area de tecnologia y deporte");
        Profesor profesor2 = new Profesor("Manuela", "Baquero", "65415",
                EnumProgramas.Ingeniería_de_Sistemas, EnumRoles.Profesor,
                "manuela@unicauca.edu.co", "Manuela123?", "Area de division de cultura y tic");
        Estudiante estudiante = new Estudiante("Karen", "Guancha", "199135165",
                EnumProgramas.Ingeniería_de_Sistemas, EnumRoles.Estudiante,
                "karen@unicauca.edu.co","Karen123?", "Aprobado");
        String tipo = "carta_empresa";
        FormatoA formato = new FormatoAPracticaProfesional(
                0, // ID (puede ser 0 si la BD lo genera)
                "Titulo",
                EnumModalidad.Investigacion,
                LocalDate.EPOCH,
                profesor,
                profesor2,
                estudiante,
                profesor,
                "sdddddddddddddd",
                "ddddddddddddddddd",
                archivoProyecto,
                cartaEmpresa
        );
        formato.setTitulo("Proyecto de prueba");
        formato.setModalidad(EnumModalidad.PracticaProfesional);

        // Debug para verificar que leíste bien los archivos
        System.out.println("Archivo proyecto bytes: " + archivoProyecto.length);
        System.out.println("Carta empresa bytes: " + cartaEmpresa.length);
        assertDoesNotThrow(() -> {
            instance.guardarArchivoEnBD(
                    new File("src/test/resources/formatoPrueba.pdf"),
                    new File("src/test/resources/cartaPrueba.pdf"),
                    tipo,
                    formato
            );
        }, "El método lanzó una excepción inesperada");
    }

    @Test
    public void testGuardarArchivoEnBD2() {
        IRepositorioFormatoA repository = new RepositorioFormatoA();
        ServicioFormatoA instance = new ServicioFormatoA(repository);
        System.out.println("guardarArchivoEnBD");
        File fileFormato = new File("src/test/resources/formatoPrueba.pdf");
        File fileCarta = new File("src/test/resources/cartaPrueba.pdf");
        Profesor profesor = new Profesor("Juan Camilo", "Yacumal Zuluaga", "301556558",
                EnumProgramas.Ingeniería_de_Sistemas, EnumRoles.Profesor,
                "juanyacumal@unicauca.edu.co", "JuanYacumal123?", "Tecnología en la medicina");
        Profesor profesor2 = new Profesor("Alejandra", "Villamarin", "51353565",
                EnumProgramas.Ingeniería_de_Sistemas, EnumRoles.Profesor,
                "alejavilla@unicauca.edu.co", "Aleja123?", "Tecnologia en la enfermeria");
        Estudiante estudiante = new Estudiante("Angela", "Astudillo", "54566565545",
                EnumProgramas.Ingeniería_de_Sistemas, EnumRoles.Profesor,
                "angela@unicauca.edu.co","Angela123?", "Aprobado");
        String tipo = "formato_a";
        FormatoA formato = new FormatoAInvestigacion("1",EnumModalidad.Investigacion,LocalDate.EPOCH, profesor, profesor2, estudiante, null, profesor2, "sdddddddddddddd","ddddddddddddddddd");
        formato.setTitulo("Proyecto de prueba");
        formato.setModalidad(EnumModalidad.Investigacion);
        formato.setDirector(profesor);
        formato.setEstudiante(estudiante);
        System.out.println("¿Existe fileFormato?: " + fileFormato.exists());
        System.out.println("¿Existe fileCarta?: " + fileCarta.exists());
        // Verificamos que no lance excepción
        assertDoesNotThrow(() -> {
            instance.guardarArchivoEnBD(fileFormato, fileCarta, tipo, formato);
        }, "El método lanzó una excepción inesperada");
    }

    @Test
public void testObtenerArchivoFormatoA() {
    IRepositorioFormatoA repository = new RepositorioFormatoA();
    ServicioFormatoA instance = new ServicioFormatoA(repository);
    System.out.println("obtenerArchivoFormatoA");
    String tipo = "Investigacion";
    int idFormato = 31;
    byte[] result = assertDoesNotThrow(() -> 
        instance.obtenerArchivoFormatoA(idFormato, tipo),
        "No se pudo obtener el archivo desde la BD"
    );
   assertNotNull(result, "El archivo recuperado es nulo");
   assertTrue(result.length > 0, "El archivo recuperado está vacío");
}
@Test
    public void testObtenerArchivoFormatoA3() {
        IRepositorioFormatoA repository = new RepositorioFormatoA();
        ServicioFormatoA instance = new ServicioFormatoA(repository);
        System.out.println("obtenerArchivoFormatoA");
        String tipo = "carta_empresa";
        int idFormato = 15;
        byte[] result = assertDoesNotThrow(() ->
                        instance.obtenerArchivoFormatoA(idFormato, tipo),
                "No se pudo obtener el archivo desde la BD"
        );
        assertNotNull(result, "El archivo recuperado es nulo");
        assertTrue(result.length > 0, "El archivo recuperado está vacío");
    }

    @Test
    public void testObtenerArchivoFormatoA2() {
        IRepositorioFormatoA repository = new RepositorioFormatoA();
        ServicioFormatoA instance = new ServicioFormatoA(repository);
        System.out.println("obtenerArchivoFormatoA");
        String tipo = "Investigacion";
        int idFormato = 32;
        byte[] result = assertDoesNotThrow(() ->
                        instance.obtenerArchivoFormatoA(idFormato, tipo),
                "No se pudo obtener el archivo desde la BD"
        );
        assertNotNull(result, "El archivo recuperado es nulo");
        assertTrue(result.length > 0, "El archivo recuperado está vacío");
    }



    @Test
    public void testObtenerFormatos() {
        IRepositorioFormatoA repository = new RepositorioFormatoA();
        ServicioFormatoA instance = new ServicioFormatoA(repository);

        System.out.println("obtenerFormatos");

        List<FormatoATabla> result = assertDoesNotThrow(() ->
                        instance.obtenerFormatos(),
                "Error al obtener la lista de formatos"
        );
        assertNotNull(result, "La lista de formatos es nula");
        assertFalse(result.isEmpty(), "La lista de formatos está vacía");
        result.forEach(f -> System.out.println(
                "ID: " + f.getIdFormato() + " | Título: " + f.getTitulo()
        ));
    }

    @Test
    public void testObtenerFormatoPorCorreo() {
        System.out.println("obtenerFormatoPorCorreo");
        IRepositorioFormatoA repository = new RepositorioFormatoA();
        ServicioFormatoA instance = new ServicioFormatoA(repository);
        String correoBuscado = "gladys@unicauca.edu.co"; //
        FormatoATabla result = assertDoesNotThrow(() ->
                        instance.obtenerFormatoPorCorreo(correoBuscado),
                "Error al obtener el formato por correo"
        );
        assertNotNull(result, "No se encontró ningún formato con el correo " + correoBuscado);
        System.out.println("Formato encontrado -> ID: " + result.getIdFormato() +
                " | Título: " + result.getTitulo());
    }
    @Test
    public void testObtenerFormatoPorCorreo3() {
        System.out.println("obtenerFormatoPorCorreo");
        IRepositorioFormatoA repository = new RepositorioFormatoA();
        ServicioFormatoA instance = new ServicioFormatoA(repository);
        String correoBuscado = "karen@unicauca.edu.co"; //
        FormatoATabla result = assertDoesNotThrow(() ->
                        instance.obtenerFormatoPorCorreo(correoBuscado),
                "Error al obtener el formato por correo"
        );
        assertNotNull(result, "No se encontró ningún formato con el correo " + correoBuscado);
        System.out.println("Formato encontrado -> ID: " + result.getIdFormato() +
                " | Título: " + result.getTitulo());
    }

    @Test
    public void testActualizarEstadoFormato() {
        System.out.println("actualizarEstadoFormato");

        IRepositorioFormatoA repository = new RepositorioFormatoA();
        ServicioFormatoA instance = new ServicioFormatoA(repository);
        int idFormato = 31; // Usa un ID real que ya exista en la BD
        String tipo = "Investigacion";
        String nuevoEstado = "Aprobado";
        boolean result = assertDoesNotThrow(() ->
                        instance.actualizarEstadoFormato(idFormato, tipo, nuevoEstado),
                "Error al actualizar el estado del formato"
        );
        assertTrue(result, "La actualización del estado no se realizó correctamente");
    }
    @Test
    public void testActualizarEstadoFormato3() {
        System.out.println("actualizarEstadoFormato");

        IRepositorioFormatoA repository = new RepositorioFormatoA();
        ServicioFormatoA instance = new ServicioFormatoA(repository);
        int idFormato = 15; // Usa un ID real que ya exista en la BD
        String tipo = "carta_empresa";
        String nuevoEstado = "Rechazado";
        boolean result = assertDoesNotThrow(() ->
                        instance.actualizarEstadoFormato(idFormato, tipo, nuevoEstado),
                "Error al actualizar el estado del formato"
        );
        assertTrue(result, "La actualización del estado no se realizó correctamente");
    }
    @Test
    public void testActualizarEstadoFormato2() {
        System.out.println("actualizarEstadoFormato");

        IRepositorioFormatoA repository = new RepositorioFormatoA();
        ServicioFormatoA instance = new ServicioFormatoA(repository);
        int idFormato = 32; // Usa un ID real que ya exista en la BD
        String tipo = "Investigacion";
        String nuevoEstado = "Rechazado";

        boolean result = assertDoesNotThrow(() ->
                        instance.actualizarEstadoFormato(idFormato, tipo, nuevoEstado),
                "Error al actualizar el estado del formato"
        );
        assertTrue(result, "La actualización del estado no se realizó correctamente");
    }

    @Test
    public void testRegistrarDevolucionFormatoA() {
        System.out.println("registrarDevolucionFormatoA");

        IRepositorioFormatoA repository = new RepositorioFormatoA();
        ServicioFormatoA instance = new ServicioFormatoA(repository);

        int idFormato = 31;
        int idProfesor = 11;
        int idCoordinador = 10;
        String correoEstudiante1 = "gladys@unicauca.edu.co";
        String correoEstudiante2 = null;
        byte[] archivoDevolucion = "Correcciones prueba".getBytes();
        String modalidad = "Investigacion";
        int intento = 1;

        boolean result = assertDoesNotThrow(() ->
                        instance.registrarDevolucionFormatoA(idFormato, idProfesor, idCoordinador,
                                correoEstudiante1, correoEstudiante2, archivoDevolucion, modalidad, intento),
                "Error al registrar la devolución del formato A"
        );

        assertTrue(result, "No se pudo registrar la devolución correctamente");
    }
    @Test
    public void testRegistrarDevolucionFormatoA2() {
        System.out.println("registrarDevolucionFormatoA");

        IRepositorioFormatoA repository = new RepositorioFormatoA();
        ServicioFormatoA instance = new ServicioFormatoA(repository);

        int idFormato = 31;
        int idProfesor = 11;
        int idCoordinador = 10;
        String correoEstudiante1 = "angela@unicauca.edu.co";
        String correoEstudiante2 = null;
        byte[] archivoDevolucion = "Correcciones prueba".getBytes();
        String modalidad = "Investigacion";
        int intento = 1;

        boolean result = assertDoesNotThrow(() ->
                        instance.registrarDevolucionFormatoA(idFormato, idProfesor, idCoordinador,
                                correoEstudiante1, correoEstudiante2, archivoDevolucion, modalidad, intento),
                "Error al registrar la devolución del formato A"
        );

        assertTrue(result, "No se pudo registrar la devolución correctamente");
    }


    @Test
    public void testObtenerUltimoIntentoEstado() {
        System.out.println("obtenerUltimoIntentoEstado");

        IRepositorioFormatoA repository = new RepositorioFormatoA();
        ServicioFormatoA instance = new ServicioFormatoA(repository);

        String correo = "gladys@unicauca.edu.co";
        EnumModalidad modalidad = EnumModalidad.Investigacion;// Ajusta a la modalidad de ese formato

        Object[] result = assertDoesNotThrow(() ->
                        instance.obtenerUltimoIntentoEstado(correo, modalidad),
                "Error al obtener el último intento y estado del formato"
        );

        assertNotNull(result, "El resultado es nulo");
        assertEquals(2, result.length, "El resultado no contiene los dos valores esperados (intento, estado)");
        assertNotNull(result[0], "El intento devuelto es nulo");
        assertNotNull(result[1], "El estado devuelto es nulo");
        System.out.println("Intento: " + result[0] + ", Estado: " + result[1]);
    }
    @Test
    public void testObtenerUltimoIntentoEstado3() {
        System.out.println("obtenerUltimoIntentoEstado");

        IRepositorioFormatoA repository = new RepositorioFormatoA();
        ServicioFormatoA instance = new ServicioFormatoA(repository);

        String correo = "karen@unicauca.edu.co";
        EnumModalidad modalidad = EnumModalidad.PracticaProfesional;

        Object[] result = assertDoesNotThrow(() ->
                        instance.obtenerUltimoIntentoEstado(correo, modalidad),
                "Error al obtener el último intento y estado del formato"
        );

        assertNotNull(result, "El resultado es nulo");
        assertEquals(2, result.length, "El resultado no contiene los dos valores esperados (intento, estado)");
        assertNotNull(result[0], "El intento devuelto es nulo");
        assertNotNull(result[1], "El estado devuelto es nulo");
        System.out.println("Intento: " + result[0] + ", Estado: " + result[1]);
    }
    @Test
    public void testObtenerUltimoIntentoEstado2() {
        System.out.println("obtenerUltimoIntentoEstado");

        IRepositorioFormatoA repository = new RepositorioFormatoA();
        ServicioFormatoA instance = new ServicioFormatoA(repository);

        String correo = "angela@unicauca.edu.co";
        EnumModalidad modalidad = EnumModalidad.Investigacion;

        Object[] result = assertDoesNotThrow(() ->
                        instance.obtenerUltimoIntentoEstado(correo, modalidad),
                "Error al obtener el último intento y estado del formato"
        );

        assertNotNull(result, "El resultado es nulo");
        assertEquals(2, result.length, "El resultado no contiene los dos valores esperados (intento, estado)");
        assertNotNull(result[0], "El intento devuelto es nulo");
        assertNotNull(result[1], "El estado devuelto es nulo");
        System.out.println("Intento: " + result[0] + ", Estado: " + result[1]);
    }
    @Test
    public void testObtenerNumeroDeIntentos() {
        System.out.println("obtenerNumeroDeIntentos");
        IRepositorioFormatoA repository = new RepositorioFormatoA();
        ServicioFormatoA instance = new ServicioFormatoA(repository);
        int idFormato = 31;
        int result = assertDoesNotThrow(() ->
                        instance.obtenerNumeroDeIntentos(idFormato),
                "Error al obtener el número de intentos del formato"
        );
        assertTrue(result >= 0, "El número de intentos no puede ser negativo");

        // Para ver en consola el resultado
        System.out.println("Número de intentos para el formato " + idFormato + ": " + result);
    }
    @Test
    public void testObtenerNumeroDeIntentos3() {
        System.out.println("obtenerNumeroDeIntentos");
        IRepositorioFormatoA repository = new RepositorioFormatoA();
        ServicioFormatoA instance = new ServicioFormatoA(repository);
        int idFormato = 15;
        int result = assertDoesNotThrow(() ->
                        instance.obtenerNumeroDeIntentos(idFormato),
                "Error al obtener el número de intentos del formato"
        );
        assertTrue(result >= 0, "El número de intentos no puede ser negativo");

        // Para ver en consola el resultado
        System.out.println("Número de intentos para el formato " + idFormato + ": " + result);
    }
    @Test
    public void testObtenerNumeroDeIntentos2() {
        System.out.println("obtenerNumeroDeIntentos");
        IRepositorioFormatoA repository = new RepositorioFormatoA();
        ServicioFormatoA instance = new ServicioFormatoA(repository);
        int idFormato = 32;
        int result = assertDoesNotThrow(() ->
                        instance.obtenerNumeroDeIntentos(idFormato),
                "Error al obtener el número de intentos del formato"
        );
        assertTrue(result >= 0, "El número de intentos no puede ser negativo");

        // Para ver en consola el resultado
        System.out.println("Número de intentos para el formato " + idFormato + ": " + result);
    }

    @Test
    public void testObtenerIdProfesorPorFormato() {
        System.out.println("obtenerIdProfesorPorFormato");

        IRepositorioFormatoA repository = new RepositorioFormatoA();
        ServicioFormatoA instance = new ServicioFormatoA(repository);

        int idFormato = 30; // Ajusta al ID de un formato que ya exista en la BD
        String tipoFormato = "Investigacion"; // Ajusta según el tipo que uses en tu BD

        int result = assertDoesNotThrow(() ->
                        instance.obtenerIdProfesorPorFormato(idFormato, tipoFormato),
                "Error al obtener el ID del profesor para el formato"
        );

        assertTrue(result > 0, "El ID del profesor debe ser mayor que 0");

        // Para inspección manual en consola
        System.out.println("ID del profesor asociado al formato " + idFormato + ": " + result);
    }
    @Test
    public void testObtenerIdProfesorPorFormato2() {
        System.out.println("obtenerIdProfesorPorFormato");

        IRepositorioFormatoA repository = new RepositorioFormatoA();
        ServicioFormatoA instance = new ServicioFormatoA(repository);

        int idFormato = 29; // Ajusta al ID de un formato que ya exista en la BD
        String tipoFormato = "Investigacion"; // Ajusta según el tipo que uses en tu BD

        int result = assertDoesNotThrow(() ->
                        instance.obtenerIdProfesorPorFormato(idFormato, tipoFormato),
                "Error al obtener el ID del profesor para el formato"
        );

        assertTrue(result > 0, "El ID del profesor debe ser mayor que 0");

        // Para inspección manual en consola
        System.out.println("ID del profesor asociado al formato " + idFormato + ": " + result);
    }



    @Test
    public void testObtenerFormatoCompleto() {
        System.out.println("obtenerFormatoCompleto");

        IRepositorioFormatoA repository = new RepositorioFormatoA();
        ServicioFormatoA instance = new ServicioFormatoA(repository);

        int idFormato = 31; // 🔧 Ajusta a un ID real que ya exista en tu BD
        String modalidad = "Investigacion"; // 🔧 Ajusta según el valor que tengas en la BD

        FormatoATabla result = assertDoesNotThrow(() ->
                        instance.obtenerFormatoCompleto(idFormato, modalidad),
                "Error al obtener el formato completo desde la BD"
        );

        assertNotNull(result, "El formato obtenido es nulo");
        assertEquals(String.valueOf(idFormato) , result.getIdFormato(), "El ID del formato no coincide");

        // Imprimir para inspección manual
        System.out.println("Formato recuperado: " + result.getTitulo() + " - Modalidad: ");
    }
    @Test
    public void testObtenerFormatoCompleto2() {
        System.out.println("obtenerFormatoCompleto");

        IRepositorioFormatoA repository = new RepositorioFormatoA();
        ServicioFormatoA instance = new ServicioFormatoA(repository);
        int idFormato = 29;
        String modalidad = "Investigacion";

        FormatoATabla result = assertDoesNotThrow(() ->
                        instance.obtenerFormatoCompleto(idFormato, modalidad),
                "Error al obtener el formato completo desde la BD"
        );
        assertNotNull(result, "El formato obtenido es nulo");
        assertEquals(String.valueOf(idFormato) , result.getIdFormato(), "El ID del formato no coincide");
        System.out.println("Formato recuperado: " + result.getTitulo() + " - Modalidad: ");
    }

    @Test
    public void testObtenerFormato() {
        System.out.println("obtenerFormato");

        IRepositorioFormatoA repository = new RepositorioFormatoA();
        ServicioFormatoA instance = new ServicioFormatoA(repository);

        int idFormato = 31;

        FormatoATabla result = assertDoesNotThrow(() ->
                        instance.obtenerFormato(idFormato),
                "No se pudo obtener el formato de la BD"
        );

        // Validaciones
        assertNotNull(result, "El formato recuperado es nulo");
        assertNotNull(result.getTitulo(), "El formato recuperado no tiene título");
        assertTrue(!result.getTitulo().isBlank(), "El formato recuperado tiene título vacío");

        // (Opcional) validamos que el ID coincide
        // Si getIdFormato devuelve String, parseamos:
        // assertEquals(idFormato, Integer.parseInt(result.getIdFormato()), "El ID no coincide");
    }
    @Test
    public void testObtenerFormato2() {
        System.out.println("obtenerFormato");

        IRepositorioFormatoA repository = new RepositorioFormatoA();
        ServicioFormatoA instance = new ServicioFormatoA(repository);

        int idFormato = 29;

        FormatoATabla result = assertDoesNotThrow(() ->
                        instance.obtenerFormato(idFormato),
                "No se pudo obtener el formato de la BD"
        );

        // Validaciones
        assertNotNull(result, "El formato recuperado es nulo");
        assertNotNull(result.getTitulo(), "El formato recuperado no tiene título");
        assertTrue(!result.getTitulo().isBlank(), "El formato recuperado tiene título vacío");

        // (Opcional) validamos que el ID coincide
        // Si getIdFormato devuelve String, parseamos:
        // assertEquals(idFormato, Integer.parseInt(result.getIdFormato()), "El ID no coincide");
    }


}
