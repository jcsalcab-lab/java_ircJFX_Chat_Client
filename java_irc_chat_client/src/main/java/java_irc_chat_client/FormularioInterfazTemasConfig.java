package java_irc_chat_client;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "temasConfig")
public class FormularioInterfazTemasConfig {

    private boolean direccion;
    private boolean lusers;
    private boolean alinear;
    private String justifica;
    private String esquemaSeleccionado;
    private boolean idiomas;
    private boolean colores;
    private boolean fuentes;
    private String temaSeleccionado;

    @XmlElement public boolean isDireccion() { return direccion; }
    public void setDireccion(boolean direccion) { this.direccion = direccion; }

    @XmlElement public boolean isLusers() { return lusers; }
    public void setLusers(boolean lusers) { this.lusers = lusers; }

    @XmlElement public boolean isAlinear() { return alinear; }
    public void setAlinear(boolean alinear) { this.alinear = alinear; }

    @XmlElement public String getJustifica() { return justifica; }
    public void setJustifica(String justifica) { this.justifica = justifica; }

    @XmlElement public String getEsquemaSeleccionado() { return esquemaSeleccionado; }
    public void setEsquemaSeleccionado(String esquemaSeleccionado) { this.esquemaSeleccionado = esquemaSeleccionado; }

    @XmlElement public boolean isIdiomas() { return idiomas; }
    public void setIdiomas(boolean idiomas) { this.idiomas = idiomas; }

    @XmlElement public boolean isColores() { return colores; }
    public void setColores(boolean colores) { this.colores = colores; }

    @XmlElement public boolean isFuentes() { return fuentes; }
    public void setFuentes(boolean fuentes) { this.fuentes = fuentes; }

    @XmlElement public String getTemaSeleccionado() { return temaSeleccionado; }
    public void setTemaSeleccionado(String temaSeleccionado) { this.temaSeleccionado = temaSeleccionado; }
}
