package java_irc_chat_client;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "cartelesConfig")
public class FormularioCartelesConfig {

    private List<String> eventos = new ArrayList<>();
    private String modoMostrarCarteles;
    private boolean desactivarAway;

    @XmlElement
    public List<String> getEventos() { return eventos; }
    public void setEventos(List<String> eventos) { this.eventos = eventos; }

    @XmlElement
    public String getModoMostrarCarteles() { return modoMostrarCarteles; }
    public void setModoMostrarCarteles(String modoMostrarCarteles) { this.modoMostrarCarteles = modoMostrarCarteles; }

    @XmlElement
    public boolean isDesactivarAway() { return desactivarAway; }
    public void setDesactivarAway(boolean desactivarAway) { this.desactivarAway = desactivarAway; }
}
