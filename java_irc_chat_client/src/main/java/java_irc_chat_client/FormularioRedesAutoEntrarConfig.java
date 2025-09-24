package java_irc_chat_client;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "RedesAutoEntrarConfig")
public class FormularioRedesAutoEntrarConfig {

    private String redSeleccionada;
    private boolean autoEntrar;
    private String retardo;
    private List<String> canales;

    public FormularioRedesAutoEntrarConfig() {
        this.canales = new ArrayList<>();
        this.retardo = "0";
    }

    @XmlElement
    public String getRedSeleccionada() {
        return redSeleccionada;
    }

    public void setRedSeleccionada(String redSeleccionada) {
        this.redSeleccionada = redSeleccionada;
    }

    @XmlElement
    public boolean isAutoEntrar() {
        return autoEntrar;
    }

    public void setAutoEntrar(boolean autoEntrar) {
        this.autoEntrar = autoEntrar;
    }

    @XmlElement
    public String getRetardo() {
        return retardo;
    }

    public void setRetardo(String retardo) {
        this.retardo = retardo;
    }

    @XmlElement(name = "canal")
    public List<String> getCanales() {
        return canales;
    }

    public void setCanales(List<String> canales) {
        this.canales = canales;
    }
}
