package java_irc_chat_client.formularios_persistencia;

import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "NickCompletionConfig")
public class FormularioNickCompletionConfig {

    @XmlElement
    public boolean activo;

    @XmlElement
    public String primerSeparador;

    @XmlElement
    public String segundoSeparador;

    @XmlElement
    public List<String> nickList = new ArrayList<>();
}
