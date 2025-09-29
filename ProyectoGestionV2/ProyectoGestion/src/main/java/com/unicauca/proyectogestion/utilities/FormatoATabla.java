package com.unicauca.proyectogestion.utilities;

import javafx.beans.property.SimpleStringProperty;

public class FormatoATabla {

    private SimpleStringProperty idFormato;
    private SimpleStringProperty correoEstudiante1;
    private SimpleStringProperty correoEstudiante2;
    private SimpleStringProperty director;
    private SimpleStringProperty codirector;
    private SimpleStringProperty tipoProyecto;
    private SimpleStringProperty titulo;
    private SimpleStringProperty fechaInicio;
    private SimpleStringProperty estadoActual;
    private SimpleStringProperty intentoActual;
    private SimpleStringProperty objetivoGeneral;
    private SimpleStringProperty objetivosEspecificos;

    // 🔹 Archivos
    private byte[] archivoProyecto;
    private byte[] cartaEmpresa; // solo si es práctica profesional

    //sencillo
    public FormatoATabla(String idFormato,String titulo,String correoEstudiante1,String correoEstudiante2, String director, String codirector, String tipoProyecto, String fechaInicio, String estadoActual) {
        this.idFormato = new SimpleStringProperty(String.valueOf(idFormato));
        this.titulo = new SimpleStringProperty(titulo);
        this.correoEstudiante1 = new SimpleStringProperty(correoEstudiante1);
        this.correoEstudiante2 = new SimpleStringProperty(correoEstudiante2);
        this.director = new SimpleStringProperty(director);
        this.codirector = new SimpleStringProperty(codirector);
        this.tipoProyecto = new SimpleStringProperty(tipoProyecto);
        this.fechaInicio = new SimpleStringProperty(fechaInicio);
        this.estadoActual = new SimpleStringProperty(estadoActual);
    }

    // 👉 Constructor para FormatoAInvestigacion
    public FormatoATabla(String idFormato, String titulo, String correoEstudiante1, String correoEstudiante2,
                         String director, String codirector, String tipoProyecto, String fechaInicio,
                         String estadoActual, String intentoActual, String objetivoGeneral, String objetivosEspecificos,
                         byte[] archivoProyecto) {
        this.idFormato = new SimpleStringProperty(idFormato);
        this.titulo = new SimpleStringProperty(titulo);
        this.correoEstudiante1 = new SimpleStringProperty(correoEstudiante1);
        this.correoEstudiante2 = new SimpleStringProperty(correoEstudiante2);
        this.director = new SimpleStringProperty(director);
        this.codirector = new SimpleStringProperty(codirector);
        this.tipoProyecto = new SimpleStringProperty(tipoProyecto);
        this.fechaInicio = new SimpleStringProperty(fechaInicio);
        this.estadoActual = new SimpleStringProperty(estadoActual);
        this.intentoActual = new SimpleStringProperty(intentoActual);
        this.objetivoGeneral = new SimpleStringProperty(objetivoGeneral);
        this.objetivosEspecificos = new SimpleStringProperty(objetivosEspecificos);
        this.archivoProyecto = archivoProyecto;
        this.cartaEmpresa = null; // no aplica
    }

    // 👉 Constructor para FormatoAPracticaProfesional
    public FormatoATabla(String idFormato, String titulo, String correoEstudiante1, String director,
                         String codirector, String tipoProyecto, String fechaInicio, String estadoActual, String intentoActual,
                         String objetivoGeneral, String objetivosEspecificos,
                         byte[] archivoProyecto, byte[] cartaEmpresa) {
        this.idFormato = new SimpleStringProperty(idFormato);
        this.titulo = new SimpleStringProperty(titulo);
        this.correoEstudiante1 = new SimpleStringProperty(correoEstudiante1);
        this.correoEstudiante2 = new SimpleStringProperty(""); // solo hay uno en práctica
        this.director = new SimpleStringProperty(director);
        this.codirector = new SimpleStringProperty(codirector);
        this.tipoProyecto = new SimpleStringProperty(tipoProyecto);
        this.fechaInicio = new SimpleStringProperty(fechaInicio);
        this.estadoActual = new SimpleStringProperty(estadoActual);
        this.intentoActual = new SimpleStringProperty(intentoActual);
        this.objetivoGeneral = new SimpleStringProperty(objetivoGeneral);
        this.objetivosEspecificos = new SimpleStringProperty(objetivosEspecificos);
        this.archivoProyecto = archivoProyecto;
        this.cartaEmpresa = cartaEmpresa;
    }

    // 🔹 Getters
    public String getIdFormato() { return idFormato.get(); }
    public String getCorreoEstudiante1() { return correoEstudiante1.get(); }
    public String getCorreoEstudiante2() { return correoEstudiante2.get(); }
    public String getDirector() { return director.get(); }
    public String getCodirector() { return codirector.get(); }
    public String getTipoProyecto() { return tipoProyecto.get(); }
    public String getTitulo() { return titulo.get(); }
    public String getFechaInicio() { return fechaInicio.get(); }
    public String getEstadoActual() { return estadoActual.get(); }
    public String getIntentoActual() { return intentoActual.get(); }
    public String getObjetivoGeneral() { return objetivoGeneral.get(); }
    public String getObjetivosEspecificos() { return objetivosEspecificos.get(); }

    // 🔹 Archivos
    public byte[] getArchivoProyecto() { return archivoProyecto; }
    public byte[] getCartaEmpresa() { return cartaEmpresa; }
}
