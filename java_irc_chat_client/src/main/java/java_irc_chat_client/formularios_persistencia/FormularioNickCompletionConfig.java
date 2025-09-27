package java_irc_chat_client.formularios_persistencia;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "NickCompletionConfig")
public class FormularioNickCompletionConfig {

    private boolean activo;
    private String primerSeparador;
    private String segundoSeparador;
    private List<String> nickList = new ArrayList<>();

    public FormularioNickCompletionConfig() {
    }

    @XmlElement
    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    @XmlElement
    public String getPrimerSeparador() {
        return primerSeparador;
    }

    public void setPrimerSeparador(String primerSeparador) {
        this.primerSeparador = primerSeparador;
    }

    @XmlElement
    public String getSegundoSeparador() {
        return segundoSeparador;
    }

    public void setSegundoSeparador(String segundoSeparador) {
        this.segundoSeparador = segundoSeparador;
    }

    @XmlElementWrapper(name = "NickList")
    @XmlElement(name = "Nick")
    public List<String> getNickList() {
        return nickList;
    }

    public void setNickList(List<String> nickList) {
        this.nickList = nickList;
    }
}
