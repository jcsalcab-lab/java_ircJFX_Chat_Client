package java_irc_chat_client.formularios_persistencia;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "CorrectorConfig")
public class FormularioCorrectorConfig {

    private boolean activo;
    private boolean mayuscula;
    private boolean subrayarUrl;
    private List<Palabra> palabras = new ArrayList<>();

    public FormularioCorrectorConfig() {
    }

    @XmlElement
    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    @XmlElement
    public boolean isMayuscula() {
        return mayuscula;
    }

    public void setMayuscula(boolean mayuscula) {
        this.mayuscula = mayuscula;
    }

    @XmlElement
    public boolean isSubrayarUrl() {
        return subrayarUrl;
    }

    public void setSubrayarUrl(boolean subrayarUrl) {
        this.subrayarUrl = subrayarUrl;
    }

    @XmlElementWrapper(name = "Palabras")
    @XmlElement(name = "Palabra")
    public List<Palabra> getPalabras() {
        return palabras;
    }

    public void setPalabras(List<Palabra> palabras) {
        this.palabras = palabras;
    }

    // Clase interna para cada palabra de reemplazo
    public static class Palabra {

        private String reemplazar;
        private String por;

        public Palabra() {
        }

        public Palabra(String reemplazar, String por) {
            this.reemplazar = reemplazar;
            this.por = por;
        }

        @XmlElement
        public String getReemplazar() {
            return reemplazar;
        }

        public void setReemplazar(String reemplazar) {
            this.reemplazar = reemplazar;
        }

        @XmlElement
        public String getPor() {
            return por;
        }

        public void setPor(String por) {
            this.por = por;
        }
    }
}


