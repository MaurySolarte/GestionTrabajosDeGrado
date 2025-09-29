package com.unicauca.proyectogestion.utilities;

public class DevolucionTabla {
    private int idDevolucion;
    private int idFormato;
    private String tituloProyecto;
    private String tipoProyecto;
    private String correoEstudiante1;
    private String correoEstudiante2;
    private String coordinador;
    private byte[] archivo;


    public DevolucionTabla(int idDevolucion, int idFormato, String tituloProyecto, String tipoProyecto, String correo1, String correo2, String coordinador, byte[] archivo) {
        this.idDevolucion = idDevolucion;
        this.idFormato = idFormato;
        this.tituloProyecto = tituloProyecto;
        this.correoEstudiante1 = correo1;
        this.correoEstudiante2 = correo2;
        this.coordinador = coordinador;
        this.archivo = archivo;
        this.tipoProyecto= tipoProyecto;
    }

    public String getTipoProyecto(){return tipoProyecto;}
    public int getIdDevolucion() { return idDevolucion; }
    public int getIdFormato() { return idFormato; }
    public String getTituloProyecto() { return tituloProyecto; }
    public String getCorreoEstudiante1() { return correoEstudiante1; }
    public String getCorreoEstudiante2() { return correoEstudiante2; }
    public String getCoordinador() { return coordinador; }
    public byte[] getArchivo() { return archivo; }
}

