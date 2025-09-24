package java_irc_chat_client.formularios_persistencia;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase que representa la configuración del formulario de Idioma.
 * Se usa para guardar y recuperar la configuración mediante JAXB (XML).
 */
@XmlRootElement(name = "idiomaConfig")
public class FormularioIdiomaConfig {

    private List<IdiomaItem> idiomas;    // Lista de idiomas en la tabla
    private boolean cambiarTema;         // Valor del checkbox

    public FormularioIdiomaConfig() {
        this.idiomas = new ArrayList<>();
    }

    @XmlElementWrapper(name = "idiomas")
    @XmlElement(name = "idioma")
    public List<IdiomaItem> getIdiomas() {
        return idiomas;
    }

    public void setIdiomas(List<IdiomaItem> idiomas) {
        this.idiomas = idiomas;
    }

    @XmlElement
    public boolean isCambiarTema() {
        return cambiarTema;
    }

    public void setCambiarTema(boolean cambiarTema) {
        this.cambiarTema = cambiarTema;
    }

    /**
     * Clase interna que representa un item de la tabla (fila).
     */
    @XmlRootElement(name = "idiomaItem")
    public static class IdiomaItem {
        private String prefIdioma;
        private String idioma;

        public IdiomaItem() {
        }

        public IdiomaItem(String prefIdioma, String idioma) {
            this.prefIdioma = prefIdioma;
            this.idioma = idioma;
        }

        @XmlElement
        public String getPrefIdioma() {
            return prefIdioma;
        }

        public void setPrefIdioma(String prefIdioma) {
            this.prefIdioma = prefIdioma;
        }

        @XmlElement
        public String getIdioma() {
            return idioma;
        }

        public void setIdioma(String idioma) {
            this.idioma = idioma;
        }
    }
}
