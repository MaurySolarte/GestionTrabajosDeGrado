package com.unicauca.proyectogestion.domain;

import java.time.LocalDate;

public class FormatoA {
    private int idFormato;
    private String titulo;
    private EnumModalidad modalidad;
    private LocalDate fechaActual;
    private Profesor director;
    private Profesor codirector;
    private Estudiante estudiante; // Relación con Usuario por correo
    private Profesor profesor;  // Relación con Profesor por id_usuario
    private String objetivoGeneral;
    private String objetivosEspecificos;
    private byte[] archivoProyecto;

    public FormatoA(int idFormato,String titulo, EnumModalidad modalidad, LocalDate fechaActual, Profesor director, Profesor codirector, Estudiante estudiante, Profesor profesor, String objetivoGeneral, String objetivosEspecificos, byte[] archivoProyecto) {
        this.idFormato = idFormato;
        this.titulo = titulo;
        this.modalidad = modalidad;
        this.fechaActual = fechaActual;
        this.director = director;
        this.codirector = codirector;
        this.estudiante = estudiante;
        this.profesor = profesor;
        this.objetivoGeneral = objetivoGeneral;
        this.objetivosEspecificos = objetivosEspecificos;
        this.archivoProyecto = archivoProyecto;
    }

    public FormatoA(String titulo, EnumModalidad modalidad, LocalDate fechaActual, Profesor director, Profesor codirector, Estudiante estudiante, Profesor profesor, String objetivoGeneral, String objetivosEspecificos) {
        this.titulo = titulo;
        this.modalidad = modalidad;
        this.fechaActual = fechaActual;
        this.director = director;
        this.codirector = codirector;
        this.estudiante = estudiante;
        this.profesor = profesor;
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

    public Profesor getDirector() {
        return director;
    }

    public void setDirector(Profesor director) {
        this.director = director;
    }

    public Profesor getCodirector() {
        return codirector;
    }

    public void setCodirector(Profesor codirector) {
        this.codirector = codirector;
    }

    public Estudiante getEstudiante() {
        return estudiante;
    }

    public void setEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
    }

    public Profesor getProfesor() {
        return profesor;
    }

    public void setProfesor(Profesor profesor) {
        this.profesor = profesor;
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
    // === GETTERS PERSONALIZADOS ===

    // Nombre completo del Director
    public String getNombreDirector() {
        return director.getNombres() + " " + director.getApellidos();
    }

    // Nombre completo del Codirector
    public String getNombreCodirector() {
        return (codirector != null) ? codirector.getNombres() + " " + codirector.getApellidos() : "Sin codirector";
    }

    // Nombre completo del Profesor (si quieres usarlo como sinónimo de director)
    public String getNombreProfesor() {
        return profesor.getNombres() + " " + profesor.getApellidos();
    }

    // Correo del Estudiante
    public String getCorreoEstudiante() {
        return estudiante.getEmail();
    }

}
