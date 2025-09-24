package java_irc_chat_client;

import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "AdornosConfig")
public class FormularioAdornosConfig {

    @XmlElement
    public boolean activo;

    @XmlElement
    public String estilo;

    @XmlElement
    public boolean adornos;

    @XmlElement
    public String textField1;

    @XmlElement
    public String textField2;

    @XmlElement
    public boolean negrita;

    @XmlElement
    public boolean subrayado;

    @XmlElement
    public boolean usarColores;

    @XmlElement
    public String ficheroEstilos;

    @XmlElement
    public List<String> listaAdornos = new ArrayList<>();
}
