package com.unicauca.proyectogestion.domain;

import java.time.LocalDate;

public class FormatoA {
    private int idFormato;
    private String titulo;
    private EnumModalidad modalidad;
    private LocalDate fechaActual;
    private String director;
    private String codirector;
    private String correoEstudiante; // Relación con Usuario por correo
    private int idProfesor;  // Relación con Profesor por id_usuario
    private String objetivoGeneral;
    private String objetivosEspecificos;
    private byte[] archivoProyecto;

    public FormatoA(int idFormato, String titulo, EnumModalidad modalidad, LocalDate fechaActual, String director, String codirector, String correoEstudiante, int idProfesor, String objetivoGeneral, String objetivosEspecificos, byte[] archivoProyecto) {
        this.idFormato = idFormato;
        this.titulo = titulo;
        this.modalidad = modalidad;
        this.fechaActual = fechaActual;
        this.director = director;
        this.codirector = codirector;
        this.correoEstudiante = correoEstudiante;
        this.idProfesor = idProfesor;
        this.objetivoGeneral = objetivoGeneral;
        this.objetivosEspecificos = objetivosEspecificos;
        this.archivoProyecto = archivoProyecto;
    }

    public FormatoA(String titulo, EnumModalidad modalidad, LocalDate fechaActual, String director, String codirector, String correoEstudiante, int idProfesor, String objetivoGeneral, String objetivosEspecificos) {
        this.titulo = titulo;
        this.modalidad = modalidad;
        this.fechaActual = fechaActual;
        this.director = director;
        this.codirector = codirector;
        this.correoEstudiante = correoEstudiante;
        this.idProfesor = idProfesor;
        this.objetivoGeneral = objetivoGeneral;
        this.objetivosEspecificos = objetivosEspecificos;
    }

    public int getIdFormato() {
        return idFormato;
    }

    public void setIdFormato(int idFormato) {
        this.idFormato = idFormato;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public EnumModalidad getModalidad() {
        return modalidad;
    }

    public void setModalidad(EnumModalidad modalidad) {
        this.modalidad = modalidad;
    }

    public LocalDate getFechaActual() {
        return fechaActual;
    }

    public void setFechaActual(LocalDate fechaActual) {
        this.fechaActual = fechaActual;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getCodirector() {
        return codirector;
    }

    public void setCodirector(String codirector) {
        this.codirector = codirector;
    }

    public String getCorreoEstudiante() {
        return correoEstudiante;
    }

    public void setCorreoEstudiante(String correoEstudiante) {
        this.correoEstudiante = correoEstudiante;
    }

    public int getIdProfesor() {
        return idProfesor;
    }

    public void setIdProfesor(int idProfesor) {
        this.idProfesor = idProfesor;
    }

    public String getObjetivoGeneral() {
        return objetivoGeneral;
    }

    public void setObjetivoGeneral(String objetivoGeneral) {
        this.objetivoGeneral = objetivoGeneral;
    }

    public String getObjetivosEspecificos() {
        return objetivosEspecificos;
    }

    public void setObjetivosEspecificos(String objetivosEspecificos) {
        this.objetivosEspecificos = objetivosEspecificos;
    }

    public byte[] getArchivoProyecto() {
        return archivoProyecto;
    }

    public void setArchivoProyecto(byte[] archivoProyecto) {
        this.archivoProyecto = archivoProyecto;
    }
}
