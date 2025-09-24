package java_irc_chat_client;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "dccConfig")
public class FormularioDCCConfig {

    // Listas de control
    private List<String> acepta = new ArrayList<>();
    private List<String> rechazar = new ArrayList<>();
    private List<String> noEnviar = new ArrayList<>();

    // Auto aceptar
    private String autoAceptarChat;
    private String autoAceptarEnvios;
    private boolean visorFserve;
    private boolean cerrarQueries;
    private boolean pedirSiFalta;
    private boolean permiteDCCIP;
    private boolean dccPasivo;
    private boolean responderComandosCola;

    // Velocidades
    private int velocidadMax;
    private int cancelaEnvioDebajo;

    // --- Getters y Setters ---
    @XmlElement public List<String> getAcepta() { return acepta; }
    public void setAcepta(List<String> acepta) { this.acepta = acepta; }

    @XmlElement public List<String> getRechazar() { return rechazar; }
    public void setRechazar(List<String> rechazar) { this.rechazar = rechazar; }

    @XmlElement public List<String> getNoEnviar() { return noEnviar; }
    public void setNoEnviar(List<String> noEnviar) { this.noEnviar = noEnviar; }

    @XmlElement public String getAutoAceptarChat() { return autoAceptarChat; }
    public void setAutoAceptarChat(String autoAceptarChat) { this.autoAceptarChat = autoAceptarChat; }

    @XmlElement public String getAutoAceptarEnvios() { return autoAceptarEnvios; }
    public void setAutoAceptarEnvios(String autoAceptarEnvios) { this.autoAceptarEnvios = autoAceptarEnvios; }

    @XmlElement public boolean isVisorFserve() { return visorFserve; }
    public void setVisorFserve(boolean visorFserve) { this.visorFserve = visorFserve; }

    @XmlElement public boolean isCerrarQueries() { return cerrarQueries; }
    public void setCerrarQueries(boolean cerrarQueries) { this.cerrarQueries = cerrarQueries; }

    @XmlElement public boolean isPedirSiFalta() { return pedirSiFalta; }
    public void setPedirSiFalta(boolean pedirSiFalta) { this.pedirSiFalta = pedirSiFalta; }

    @XmlElement public boolean isPermiteDCCIP() { return permiteDCCIP; }
    public void setPermiteDCCIP(boolean permiteDCCIP) { this.permiteDCCIP = permiteDCCIP; }

    @XmlElement public boolean isDccPasivo() { return dccPasivo; }
    public void setDccPasivo(boolean dccPasivo) { this.dccPasivo = dccPasivo; }

    @XmlElement public boolean isResponderComandosCola() { return responderComandosCola; }
    public void setResponderComandosCola(boolean responderComandosCola) { this.responderComandosCola = responderComandosCola; }

    @XmlElement public int getVelocidadMax() { return velocidadMax; }
    public void setVelocidadMax(int velocidadMax) { this.velocidadMax = velocidadMax; }

    @XmlElement public int getCancelaEnvioDebajo() { return cancelaEnvioDebajo; }
    public void setCancelaEnvioDebajo(int cancelaEnvioDebajo) { this.cancelaEnvioDebajo = cancelaEnvioDebajo; }
}
