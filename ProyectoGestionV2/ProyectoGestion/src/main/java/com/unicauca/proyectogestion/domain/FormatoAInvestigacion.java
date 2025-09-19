package com.unicauca.proyectogestion.domain;

import java.time.LocalDate;

public class FormatoAInvestigacion extends FormatoA{


    // Constructor con id
    public FormatoAInvestigacion(int idFormato, String titulo, EnumModalidad modalidad, LocalDate fechaActual,
                    String director, String codirector, String correoEstudiante, int idProfesor,
                    String objetivoGeneral, String objetivosEspecificos, byte[] archivoProyecto) {
        super(idFormato, titulo, modalidad, fechaActual, director, codirector, correoEstudiante, idProfesor, objetivoGeneral, objetivosEspecificos, archivoProyecto);
    }


// Constructor sin id
    public FormatoAInvestigacion(String titulo, EnumModalidad modalidad, LocalDate fechaActual,
                    String director, String codirector, String correoEstudiante, int idProfesor,
                    String objetivoGeneral, String objetivosEspecificos) {
        super(titulo, modalidad, fechaActual, director, codirector, correoEstudiante, idProfesor, objetivoGeneral, objetivosEspecificos);
    }

}
