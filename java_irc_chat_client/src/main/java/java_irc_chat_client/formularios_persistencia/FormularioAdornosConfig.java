package java_irc_chat_client.formularios_persistencia;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "FormularioAdornosConfig")
public class FormularioAdornosConfig {

    private boolean activo;
    private String estilo;
    private boolean adornos;       // Checkbox principal
    private boolean negrita;
    private boolean subrayado;
    private boolean usarColores;
    private String ficheroEstilos;
    private List<String> adornosList = new ArrayList<>(); // Lista de adornos

    public FormularioAdornosConfig() {
    }

    @XmlElement
    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    @XmlElement
    public String getEstilo() {
        return estilo;
    }

    public void setEstilo(String estilo) {
        this.estilo = estilo;
    }

    @XmlElement
    public boolean isAdornos() {
        return adornos;
    }

    public void setAdornos(boolean adornos) {
        this.adornos = adornos;
    }

    @XmlElement
    public boolean isNegrita() {
        return negrita;
    }

    public void setNegrita(boolean negrita) {
        this.negrita = negrita;
    }

    @XmlElement
    public boolean isSubrayado() {
        return subrayado;
    }

    public void setSubrayado(boolean subrayado) {
        this.subrayado = subrayado;
    }

    @XmlElement
    public boolean isUsarColores() {
        return usarColores;
    }

    public void setUsarColores(boolean usarColores) {
        this.usarColores = usarColores;
    }

    @XmlElement
    public String getFicheroEstilos() {
        return ficheroEstilos;
    }

    public void setFicheroEstilos(String ficheroEstilos) {
        this.ficheroEstilos = ficheroEstilos;
    }

    @XmlElementWrapper(name = "AdornosList")
    @XmlElement(name = "Adorno")
    public List<String> getAdornosList() {
        return adornosList;
    }

    public void setAdornosList(List<String> adornosList) {
        this.adornosList = adornosList;
    }
}
