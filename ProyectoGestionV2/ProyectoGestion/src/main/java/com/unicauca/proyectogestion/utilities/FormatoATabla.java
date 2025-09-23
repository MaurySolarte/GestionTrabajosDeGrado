package com.unicauca.proyectogestion.utilities;
import javafx.beans.property.SimpleStringProperty;

public class FormatoATabla {

        private SimpleStringProperty correoEstudiante;
        private SimpleStringProperty director;
        private SimpleStringProperty tipoProyecto;
        private SimpleStringProperty evaluado;

        public FormatoATabla(String correoEstudiante, String director, String tipoProyecto, String evaluado) {
            this.correoEstudiante = new SimpleStringProperty(correoEstudiante);
            this.director = new SimpleStringProperty(director);
            this.tipoProyecto = new SimpleStringProperty(tipoProyecto);
            this.evaluado = new SimpleStringProperty(evaluado);
        }

        public String getCorreoEstudiante() {
            return correoEstudiante.get();
        }

        public String getDirector() {
            return director.get();
        }

        public String getTipoProyecto() {
            return tipoProyecto.get();
        }

        public String getEvaluado() {
            return evaluado.get();
        }

}
