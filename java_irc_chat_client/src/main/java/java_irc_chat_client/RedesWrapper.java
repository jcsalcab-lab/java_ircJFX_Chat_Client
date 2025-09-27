package java_irc_chat_client;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import java_irc_chat_client.formularios_persistencia.FormularioRedesConfig;

import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name="RedesWrapper")
public class RedesWrapper {

    private List<FormularioRedesConfig> redes = new ArrayList<>();

    public RedesWrapper() {}

    public RedesWrapper(List<FormularioRedesConfig> redes) {
        this.redes = redes;
    }

    @XmlElement(name="Red")
    public List<FormularioRedesConfig> getRedes() {
        return redes;
    }

    public void setRedes(List<FormularioRedesConfig> redes) {
        this.redes = redes;
    }
}
