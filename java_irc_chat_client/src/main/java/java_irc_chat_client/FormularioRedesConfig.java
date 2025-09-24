package java_irc_chat_client;

import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "RedesConfig")
public class FormularioRedesConfig {

    @XmlElement
    public List<String> listaRedes = new ArrayList<>();

    @XmlElement
    public String nombreRed;

    @XmlElement
    public String ficheroMenus;

    @XmlElement
    public String apareceComo;

    @XmlElement
    public String mascaraIpBots;

    @XmlElement
    public String nickserv;

    @XmlElement
    public String chanserv;

    @XmlElement
    public String memoserv;

    @XmlElement
    public String pideId;

    @XmlElement
    public boolean autoCargarMenus;

    @XmlElement
    public boolean mensajeBotsPrivado;

    @XmlElement
    public boolean sinPerfil;
}

