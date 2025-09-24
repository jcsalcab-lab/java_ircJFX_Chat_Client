package java_irc_chat_client.formularios_persistencia;

import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "RedesNicksConfig")
public class FormularioRedesNicksConfig {

    @XmlElement
    public List<String> listaRedes = new ArrayList<>();

    @XmlElement
    public String redSeleccionada;

    @XmlElement
    public boolean identificarNick;

    @XmlElement
    public boolean liberarOcupado;

    @XmlElement
    public List<String> listaNicks = new ArrayList<>();
}
