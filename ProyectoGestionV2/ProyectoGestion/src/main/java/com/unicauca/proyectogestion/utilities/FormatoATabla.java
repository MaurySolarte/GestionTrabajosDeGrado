package com.unicauca.proyectogestion.utilities;
import javafx.beans.property.SimpleStringProperty;

public class FormatoATabla {
    
        private SimpleStringProperty idFormato;
        private SimpleStringProperty correoEstudiante;
        private SimpleStringProperty director;
        private SimpleStringProperty codirector;
        private SimpleStringProperty tipoProyecto;
        private SimpleStringProperty titulo;
        private SimpleStringProperty fechaInicio;
        private SimpleStringProperty estadoActual;


        public FormatoATabla(String idFormato,String titulo,String correoEstudiante, String director, String codirector, String tipoProyecto, String fechaInicio, String estadoActual) {
            this.idFormato = new SimpleStringProperty(String.valueOf(idFormato));
            this.titulo = new SimpleStringProperty(titulo);
            this.correoEstudiante = new SimpleStringProperty(correoEstudiante);
            this.director = new SimpleStringProperty(director);
            this.codirector = new SimpleStringProperty(codirector);
            this.tipoProyecto = new SimpleStringProperty(tipoProyecto);
            this.fechaInicio = new SimpleStringProperty(fechaInicio);
            this.estadoActual = new SimpleStringProperty(estadoActual);
        }
        public String getIdFormato() {return idFormato.get();}

        public String getCorreoEstudiante() {
            return correoEstudiante.get();
        }

        public String getDirector() {
            return director.get();
        }

        public String getTipoProyecto() {
            return tipoProyecto.get();
        }

        public String getTitulo() {
            return titulo.get();
        }

        public String getFechaInicio() {return fechaInicio.get();}

        public String getEstadoActual() {return estadoActual.get();}

}
