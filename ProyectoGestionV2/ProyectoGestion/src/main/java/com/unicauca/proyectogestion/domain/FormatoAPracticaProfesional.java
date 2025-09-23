package com.unicauca.proyectogestion.domain;

import java.time.LocalDate;

public class FormatoAPracticaProfesional extends FormatoA {

    private byte[] cartaEmpresa;
    // Constructor con id
    public FormatoAPracticaProfesional(int idFormato, String titulo, EnumModalidad modalidad, LocalDate fechaActual,
                                       Profesor director, Profesor codirector, Estudiante estudiante, Profesor profesor,
                                       String objetivoGeneral, String objetivosEspecificos,byte[] archivoProyecto, byte[] cartaEmpresa) {
        super(idFormato,titulo, modalidad, fechaActual, director, codirector, estudiante, profesor,
                objetivoGeneral, objetivosEspecificos,archivoProyecto);
        this.cartaEmpresa = cartaEmpresa;
    }
    // Constructor sin id
    public FormatoAPracticaProfesional(String titulo, EnumModalidad modalidad, LocalDate fechaActual,
                                       Profesor director, Profesor codirector, Estudiante estudiante, Profesor profesor,
                                       String objetivoGeneral, String objetivosEspecificos, byte[] cartaEmpresa) {
        super(titulo, modalidad, fechaActual, director, codirector, estudiante, profesor,
                objetivoGeneral, objetivosEspecificos);
        this.cartaEmpresa = cartaEmpresa;
    }

}
