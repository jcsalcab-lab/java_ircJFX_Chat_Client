package java_irc_chat_client.formularios_persistencia;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "awayConfig")
public class FormularioAwayConfig {

    // Sistema de Ausencia
    private boolean cambiarNick;
    private String nickAway;
    private boolean notificarEstado;
    private boolean deOpCanales;
    private boolean anunciarContestador;
    private boolean desactivarSonidos;
    private boolean contestado;
    private boolean multiserver;

    private List<String> awayList = new ArrayList<>();
    private List<String> razonesList = new ArrayList<>();

    // Opciones
    private boolean contestadorActivo;
    private String mensajeContestador;

    private boolean anunciar;
    private int cadaMinutos;
    private String textoAviso;
    private String textoVuelta;
    private String anunciarSolo;
    private String noAnunciar;

    private int autoAwayMins;
    private String razonAutoAway;
    private boolean minimizarSystray;

    // --- Getters y Setters ---
    @XmlElement public boolean isCambiarNick() { return cambiarNick; }
    public void setCambiarNick(boolean cambiarNick) { this.cambiarNick = cambiarNick; }

    @XmlElement public String getNickAway() { return nickAway; }
    public void setNickAway(String nickAway) { this.nickAway = nickAway; }

    @XmlElement public boolean isNotificarEstado() { return notificarEstado; }
    public void setNotificarEstado(boolean notificarEstado) { this.notificarEstado = notificarEstado; }

    @XmlElement public boolean isDeOpCanales() { return deOpCanales; }
    public void setDeOpCanales(boolean deOpCanales) { this.deOpCanales = deOpCanales; }

    @XmlElement public boolean isAnunciarContestador() { return anunciarContestador; }
    public void setAnunciarContestador(boolean anunciarContestador) { this.anunciarContestador = anunciarContestador; }

    @XmlElement public boolean isDesactivarSonidos() { return desactivarSonidos; }
    public void setDesactivarSonidos(boolean desactivarSonidos) { this.desactivarSonidos = desactivarSonidos; }

    @XmlElement public boolean isContestado() { return contestado; }
    public void setContestado(boolean contestado) { this.contestado = contestado; }

    @XmlElement public boolean isMultiserver() { return multiserver; }
    public void setMultiserver(boolean multiserver) { this.multiserver = multiserver; }

    @XmlElement public List<String> getAwayList() { return awayList; }
    public void setAwayList(List<String> awayList) { this.awayList = awayList; }

    @XmlElement public List<String> getRazonesList() { return razonesList; }
    public void setRazonesList(List<String> razonesList) { this.razonesList = razonesList; }

    @XmlElement public boolean isContestadorActivo() { return contestadorActivo; }
    public void setContestadorActivo(boolean contestadorActivo) { this.contestadorActivo = contestadorActivo; }

    @XmlElement public String getMensajeContestador() { return mensajeContestador; }
    public void setMensajeContestador(String mensajeContestador) { this.mensajeContestador = mensajeContestador; }

    @XmlElement public boolean isAnunciar() { return anunciar; }
    public void setAnunciar(boolean anunciar) { this.anunciar = anunciar; }

    @XmlElement public int getCadaMinutos() { return cadaMinutos; }
    public void setCadaMinutos(int cadaMinutos) { this.cadaMinutos = cadaMinutos; }

    @XmlElement public String getTextoAviso() { return textoAviso; }
    public void setTextoAviso(String textoAviso) { this.textoAviso = textoAviso; }

    @XmlElement public String getTextoVuelta() { return textoVuelta; }
    public void setTextoVuelta(String textoVuelta) { this.textoVuelta = textoVuelta; }

    @XmlElement public String getAnunciarSolo() { return anunciarSolo; }
    public void setAnunciarSolo(String anunciarSolo) { this.anunciarSolo = anunciarSolo; }

    @XmlElement public String getNoAnunciar() { return noAnunciar; }
    public void setNoAnunciar(String noAnunciar) { this.noAnunciar = noAnunciar; }

    @XmlElement public int getAutoAwayMins() { return autoAwayMins; }
    public void setAutoAwayMins(int autoAwayMins) { this.autoAwayMins = autoAwayMins; }

    @XmlElement public String getRazonAutoAway() { return razonAutoAway; }
    public void setRazonAutoAway(String razonAutoAway) { this.razonAutoAway = razonAutoAway; }

    @XmlElement public boolean isMinimizarSystray() { return minimizarSystray; }
    public void setMinimizarSystray(boolean minimizarSystray) { this.minimizarSystray = minimizarSystray; }
}
