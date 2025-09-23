package com.unicauca.proyectogestion.domain;

import java.time.LocalDate;

public class FormatoAInvestigacion extends FormatoA{

    // Constructor con id

    public FormatoAInvestigacion(int idFormato, String titulo, EnumModalidad modalidad, LocalDate fechaActual,
                                 Profesor director, Profesor codirector, Estudiante estudiante, Profesor profesor,
                                 String objetivoGeneral, String objetivosEspecificos, byte[] archivoProyecto) {
        super(idFormato, titulo, modalidad, fechaActual, director, codirector, estudiante, profesor, objetivoGeneral, objetivosEspecificos, archivoProyecto);
    }


    // Constructor con id
    public FormatoAInvestigacion(String titulo, EnumModalidad modalidad, LocalDate fechaActual,
                                 Profesor director, Profesor codirector, Estudiante estudiante, Profesor profesor,
                                 String objetivoGeneral, String objetivosEspecificos) {
        super(titulo, modalidad, fechaActual, director, codirector, estudiante, profesor, objetivoGeneral, objetivosEspecificos);
    }

}
