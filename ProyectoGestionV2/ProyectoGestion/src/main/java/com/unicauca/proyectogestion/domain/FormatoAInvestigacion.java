package com.unicauca.proyectogestion.domain;

import java.time.LocalDate;

public class FormatoAInvestigacion extends FormatoA{

    private Estudiante estudiante2;

    // Constructor con id

    public FormatoAInvestigacion(int idFormato, String titulo, EnumModalidad modalidad, LocalDate fechaActual,
                                 Profesor director, Profesor codirector, Estudiante estudiante1, Estudiante estudiante2, Profesor profesor,
                                 String objetivoGeneral, String objetivosEspecificos, byte[] archivoProyecto) {
        super(idFormato, titulo, modalidad, fechaActual, director, codirector, estudiante1, profesor, objetivoGeneral, objetivosEspecificos, archivoProyecto);
        this.estudiante2 = estudiante2;
    }


    // Constructor con id
    public FormatoAInvestigacion(String titulo, EnumModalidad modalidad, LocalDate fechaActual,
                                 Profesor director, Profesor codirector,  Estudiante estudiante1, Estudiante estudiante2, Profesor profesor,
                                 String objetivoGeneral, String objetivosEspecificos) {
        super(titulo, modalidad, fechaActual, director, codirector, estudiante1, profesor, objetivoGeneral, objetivosEspecificos);
        this.estudiante2 = estudiante2;
    }

    public Estudiante getEstudiante2() {
        return estudiante2;
    }

    public String getCorreoEstudiante2() {
        return estudiante2.getEmail();
    }
}
