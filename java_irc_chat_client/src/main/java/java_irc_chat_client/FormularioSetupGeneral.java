package java_irc_chat_client;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "formulario_setup_general")
public class FormularioSetupGeneral {

    private boolean invisible;
    private boolean wallOps;
    private boolean noticias;
    private String otros;
    private String nick;

    private boolean medidorLag;
    private boolean antiIdle;
    private boolean noResponderCTCP;
    private boolean avisoMencion;
    private boolean relocalizadoNicks;

    private boolean bloquearPrivados;
    private String mensajeExcusa;
    private String limitePrivados;

    @XmlElement public boolean isInvisible() { return invisible; }
    public void setInvisible(boolean invisible) { this.invisible = invisible; }

    @XmlElement public boolean isWallOps() { return wallOps; }
    public void setWallOps(boolean wallOps) { this.wallOps = wallOps; }

    @XmlElement public boolean isNoticias() { return noticias; }
    public void setNoticias(boolean noticias) { this.noticias = noticias; }

    @XmlElement public String getOtros() { return otros; }
    public void setOtros(String otros) { this.otros = otros; }

    @XmlElement public String getNick() { return nick; }
    public void setNick(String nick) { this.nick = nick; }

    @XmlElement public boolean isMedidorLag() { return medidorLag; }
    public void setMedidorLag(boolean medidorLag) { this.medidorLag = medidorLag; }

    @XmlElement public boolean isAntiIdle() { return antiIdle; }
    public void setAntiIdle(boolean antiIdle) { this.antiIdle = antiIdle; }

    @XmlElement public boolean isNoResponderCTCP() { return noResponderCTCP; }
    public void setNoResponderCTCP(boolean noResponderCTCP) { this.noResponderCTCP = noResponderCTCP; }

    @XmlElement public boolean isAvisoMencion() { return avisoMencion; }
    public void setAvisoMencion(boolean avisoMencion) { this.avisoMencion = avisoMencion; }

    @XmlElement public boolean isRelocalizadoNicks() { return relocalizadoNicks; }
    public void setRelocalizadoNicks(boolean relocalizadoNicks) { this.relocalizadoNicks = relocalizadoNicks; }

    @XmlElement public boolean isBloquearPrivados() { return bloquearPrivados; }
    public void setBloquearPrivados(boolean bloquearPrivados) { this.bloquearPrivados = bloquearPrivados; }

    @XmlElement public String getMensajeExcusa() { return mensajeExcusa; }
    public void setMensajeExcusa(String mensajeExcusa) { this.mensajeExcusa = mensajeExcusa; }

    @XmlElement public String getLimitePrivados() { return limitePrivados; }
    public void setLimitePrivados(String limitePrivados) { this.limitePrivados = limitePrivados; }
}

