package java_irc_chat_client.formularios_persistencia;

import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAccessType;
import java.util.ArrayList;
import java.util.List;

/**
 * Wrapper principal para guardar todas las configuraciones de redes.
 */
@XmlRootElement(name = "RedesConfig")
@XmlAccessorType(XmlAccessType.FIELD)
public class RedesConfigWrapper {

    @XmlElement(name = "FormularioRedesConfig") // Cada red
    private List<FormularioRedesConfig> redes = new ArrayList<>();

    public List<FormularioRedesConfig> getRedes() { return redes; }
    public void setRedes(List<FormularioRedesConfig> redes) { this.redes = redes; }
}

