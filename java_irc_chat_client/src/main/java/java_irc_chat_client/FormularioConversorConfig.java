package java_irc_chat_client;

import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlElement;

@XmlRootElement(name = "ConversorConfig")
public class FormularioConversorConfig {

    @XmlElement
    public boolean activo;

    @XmlElement
    public String cantidadMedida;

    @XmlElement
    public String resultado;

    @XmlElement
    public String euroDolares;
}
