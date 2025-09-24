package java_irc_chat_client;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import java.util.List;

@XmlRootElement(name = "proteccionConfig")
public class FormularioProteccionConfig {

    // ===== Protecciones Flood/DOS =====
    private boolean ctcpFlood;
    private boolean floodTexto;
    private boolean dccFlood;
    private boolean queryFlood;
    private boolean ataquesDos;

    // ===== Extras =====
    private boolean reforzarSilence;
    private boolean bloqueaTusnamis;
    private boolean floodNetTexto;

    // ===== Anti deop/ban/kick =====
    private boolean antiDeopActivo;
    private boolean usarChanserv;

    // ===== Excluir protecciones =====
    private String excluirCtcp;

    // ===== Anti-spam mensajes privados =====
    private boolean antiSpamAlAbrir;
    private boolean antiSpamSiempre;
    private List<String> listaPrivateSpam;
    private boolean logSpam;

    // ===== Anti-spam canales =====
    private boolean antiSpamCanalesActivo;
    private boolean anularSpamQuits;

    // ===== Getters y Setters =====

    @XmlElement public boolean isCtcpFlood() { return ctcpFlood; }
    public void setCtcpFlood(boolean ctcpFlood) { this.ctcpFlood = ctcpFlood; }

    @XmlElement public boolean isFloodTexto() { return floodTexto; }
    public void setFloodTexto(boolean floodTexto) { this.floodTexto = floodTexto; }

    @XmlElement public boolean isDccFlood() { return dccFlood; }
    public void setDccFlood(boolean dccFlood) { this.dccFlood = dccFlood; }

    @XmlElement public boolean isQueryFlood() { return queryFlood; }
    public void setQueryFlood(boolean queryFlood) { this.queryFlood = queryFlood; }

    @XmlElement public boolean isAtaquesDos() { return ataquesDos; }
    public void setAtaquesDos(boolean ataquesDos) { this.ataquesDos = ataquesDos; }

    @XmlElement public boolean isReforzarSilence() { return reforzarSilence; }
    public void setReforzarSilence(boolean reforzarSilence) { this.reforzarSilence = reforzarSilence; }

    @XmlElement public boolean isBloqueaTusnamis() { return bloqueaTusnamis; }
    public void setBloqueaTusnamis(boolean bloqueaTusnamis) { this.bloqueaTusnamis = bloqueaTusnamis; }

    @XmlElement public boolean isFloodNetTexto() { return floodNetTexto; }
    public void setFloodNetTexto(boolean floodNetTexto) { this.floodNetTexto = floodNetTexto; }

    @XmlElement public boolean isAntiDeopActivo() { return antiDeopActivo; }
    public void setAntiDeopActivo(boolean antiDeopActivo) { this.antiDeopActivo = antiDeopActivo; }

    @XmlElement public boolean isUsarChanserv() { return usarChanserv; }
    public void setUsarChanserv(boolean usarChanserv) { this.usarChanserv = usarChanserv; }

    @XmlElement public String getExcluirCtcp() { return excluirCtcp; }
    public void setExcluirCtcp(String excluirCtcp) { this.excluirCtcp = excluirCtcp; }

    @XmlElement public boolean isAntiSpamAlAbrir() { return antiSpamAlAbrir; }
    public void setAntiSpamAlAbrir(boolean antiSpamAlAbrir) { this.antiSpamAlAbrir = antiSpamAlAbrir; }

    @XmlElement public boolean isAntiSpamSiempre() { return antiSpamSiempre; }
    public void setAntiSpamSiempre(boolean antiSpamSiempre) { this.antiSpamSiempre = antiSpamSiempre; }

    @XmlElementWrapper(name = "listaPrivateSpam")
    @XmlElement(name = "spam")
    public List<String> getListaPrivateSpam() { return listaPrivateSpam; }
    public void setListaPrivateSpam(List<String> listaPrivateSpam) { this.listaPrivateSpam = listaPrivateSpam; }

    @XmlElement public boolean isLogSpam() { return logSpam; }
    public void setLogSpam(boolean logSpam) { this.logSpam = logSpam; }

    @XmlElement public boolean isAntiSpamCanalesActivo() { return antiSpamCanalesActivo; }
    public void setAntiSpamCanalesActivo(boolean antiSpamCanalesActivo) { this.antiSpamCanalesActivo = antiSpamCanalesActivo; }

    @XmlElement public boolean isAnularSpamQuits() { return anularSpamQuits; }
    public void setAnularSpamQuits(boolean anularSpamQuits) { this.anularSpamQuits = anularSpamQuits; }
}
