package java_irc_chat_client.formularios_persistencia;

import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "CorrectorConfig")
public class FormularioCorrectorConfig {

    @XmlElement
    public boolean activo;

    @XmlElement
    public boolean mayuscula;

    @XmlElement
    public boolean subrayarUrl;

    @XmlElement
    public List<Palabra> palabras = new ArrayList<>();

    // Clase interna para representar pares "reemplazar - por"
    @XmlRootElement(name = "Palabra")
    public static class Palabra {
        @XmlElement
        public String reemplazar;
        @XmlElement
        public String por;

        public Palabra() {}

        public Palabra(String reemplazar, String por) {
            this.reemplazar = reemplazar;
            this.por = por;
        }
    }
}
