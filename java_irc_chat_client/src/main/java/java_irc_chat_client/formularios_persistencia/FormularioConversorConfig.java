package java_irc_chat_client.formularios_persistencia;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "ConversorConfig")
public class FormularioConversorConfig {

    private boolean activo;
    private String cantidadMedida;
    private String resultado;
    private String euroDolares;

    public FormularioConversorConfig() {
    }

    @XmlElement
    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    @XmlElement
    public String getCantidadMedida() {
        return cantidadMedida;
    }

    public void setCantidadMedida(String cantidadMedida) {
        this.cantidadMedida = cantidadMedida;
    }

    @XmlElement
    public String getResultado() {
        return resultado;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }

    @XmlElement
    public String getEuroDolares() {
        return euroDolares;
    }

    public void setEuroDolares(String euroDolares) {
        this.euroDolares = euroDolares;
    }
}
