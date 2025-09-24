package java_irc_chat_client.formularios_persistencia;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "RedesCanalesConfig")
public class FormularioRedesCanalesConfig {

    private String redSeleccionada;
    private boolean autoFundador;
    private List<String> canales;

    public FormularioRedesCanalesConfig() {
        this.canales = new ArrayList<>();
    }

    @XmlElement
    public String getRedSeleccionada() {
        return redSeleccionada;
    }

    public void setRedSeleccionada(String redSeleccionada) {
        this.redSeleccionada = redSeleccionada;
    }

    @XmlElement
    public boolean isAutoFundador() {
        return autoFundador;
    }

    public void setAutoFundador(boolean autoFundador) {
        this.autoFundador = autoFundador;
    }

    @XmlElement(name = "canal")
    public List<String> getCanales() {
        return canales;
    }

    public void setCanales(List<String> canales) {
        this.canales = canales;
    }
}
