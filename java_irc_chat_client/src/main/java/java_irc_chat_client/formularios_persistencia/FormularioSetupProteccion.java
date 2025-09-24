package java_irc_chat_client.formularios_persistencia;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "FormularioSetupProteccion")
public class FormularioSetupProteccion {

    private boolean ctcpFlood;
    private boolean floodTexto;
    private boolean dccFlood;
    private boolean queryFlood;
    private boolean dos;

    private boolean silence;
    private boolean bloqueaTusnamis;
    private boolean floodNet;

    private boolean activoDeop;
    private boolean usarChanserv;

    private String excluirCtcp;

    // --- Getters y Setters ---
    @XmlElement
    public boolean isCtcpFlood() { return ctcpFlood; }
    public void setCtcpFlood(boolean ctcpFlood) { this.ctcpFlood = ctcpFlood; }

    @XmlElement
    public boolean isFloodTexto() { return floodTexto; }
    public void setFloodTexto(boolean floodTexto) { this.floodTexto = floodTexto; }

    @XmlElement
    public boolean isDccFlood() { return dccFlood; }
    public void setDccFlood(boolean dccFlood) { this.dccFlood = dccFlood; }

    @XmlElement
    public boolean isQueryFlood() { return queryFlood; }
    public void setQueryFlood(boolean queryFlood) { this.queryFlood = queryFlood; }

    @XmlElement
    public boolean isDos() { return dos; }
    public void setDos(boolean dos) { this.dos = dos; }

    @XmlElement
    public boolean isSilence() { return silence; }
    public void setSilence(boolean silence) { this.silence = silence; }

    @XmlElement
    public boolean isBloqueaTusnamis() { return bloqueaTusnamis; }
    public void setBloqueaTusnamis(boolean bloqueaTusnamis) { this.bloqueaTusnamis = bloqueaTusnamis; }

    @XmlElement
    public boolean isFloodNet() { return floodNet; }
    public void setFloodNet(boolean floodNet) { this.floodNet = floodNet; }

    @XmlElement
    public boolean isActivoDeop() { return activoDeop; }
    public void setActivoDeop(boolean activoDeop) { this.activoDeop = activoDeop; }

    @XmlElement
    public boolean isUsarChanserv() { return usarChanserv; }
    public void setUsarChanserv(boolean usarChanserv) { this.usarChanserv = usarChanserv; }

    @XmlElement
    public String getExcluirCtcp() { return excluirCtcp; }
    public void setExcluirCtcp(String excluirCtcp) { this.excluirCtcp = excluirCtcp; }
}
