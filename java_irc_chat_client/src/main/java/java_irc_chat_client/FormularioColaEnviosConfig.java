package java_irc_chat_client;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "colaEnviosConfig")
public class FormularioColaEnviosConfig {

    private int maxEnvios;
    private int maxPorUsuario;
    private int slotsCola;
    private int slotsPorUsuario;
    private int saltarCola;

    // --- Getters y Setters ---
    @XmlElement
    public int getMaxEnvios() { return maxEnvios; }
    public void setMaxEnvios(int maxEnvios) { this.maxEnvios = maxEnvios; }

    @XmlElement
    public int getMaxPorUsuario() { return maxPorUsuario; }
    public void setMaxPorUsuario(int maxPorUsuario) { this.maxPorUsuario = maxPorUsuario; }

    @XmlElement
    public int getSlotsCola() { return slotsCola; }
    public void setSlotsCola(int slotsCola) { this.slotsCola = slotsCola; }

    @XmlElement
    public int getSlotsPorUsuario() { return slotsPorUsuario; }
    public void setSlotsPorUsuario(int slotsPorUsuario) { this.slotsPorUsuario = slotsPorUsuario; }

    @XmlElement
    public int getSaltarCola() { return saltarCola; }
    public void setSaltarCola(int saltarCola) { this.saltarCola = saltarCola; }
}
