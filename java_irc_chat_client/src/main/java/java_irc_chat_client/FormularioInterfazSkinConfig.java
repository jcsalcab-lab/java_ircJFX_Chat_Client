package java_irc_chat_client;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "skinConfig")
public class FormularioInterfazSkinConfig {

    private boolean temaTexto;
    private boolean temaSonido;
    private boolean colores;
    private boolean fuentes;
    private boolean fondos;

    private String skinSeleccionada;

    @XmlElement public boolean isTemaTexto() { return temaTexto; }
    public void setTemaTexto(boolean temaTexto) { this.temaTexto = temaTexto; }

    @XmlElement public boolean isTemaSonido() { return temaSonido; }
    public void setTemaSonido(boolean temaSonido) { this.temaSonido = temaSonido; }

    @XmlElement public boolean isColores() { return colores; }
    public void setColores(boolean colores) { this.colores = colores; }

    @XmlElement public boolean isFuentes() { return fuentes; }
    public void setFuentes(boolean fuentes) { this.fuentes = fuentes; }

    @XmlElement public boolean isFondos() { return fondos; }
    public void setFondos(boolean fondos) { this.fondos = fondos; }

    @XmlElement public String getSkinSeleccionada() { return skinSeleccionada; }
    public void setSkinSeleccionada(String skinSeleccionada) { this.skinSeleccionada = skinSeleccionada; }
}
