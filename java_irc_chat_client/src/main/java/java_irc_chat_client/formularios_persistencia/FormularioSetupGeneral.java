package java_irc_chat_client.formularios_persistencia;

import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class FormularioSetupGeneral {
    private boolean invisible, wallOps, noticias, medidorLag, antiIdle, noResponderCTCP, avisoMencion, relocalizadoNicks, bloquearPrivados;
    private String otros, nick, mensajeExcusa, limitePrivados;

    // Getters y Setters
    public boolean isInvisible() { return invisible; }
    public void setInvisible(boolean invisible) { this.invisible = invisible; }

    public boolean isWallOps() { return wallOps; }
    public void setWallOps(boolean wallOps) { this.wallOps = wallOps; }

    public boolean isNoticias() { return noticias; }
    public void setNoticias(boolean noticias) { this.noticias = noticias; }

    public boolean isMedidorLag() { return medidorLag; }
    public void setMedidorLag(boolean medidorLag) { this.medidorLag = medidorLag; }

    public boolean isAntiIdle() { return antiIdle; }
    public void setAntiIdle(boolean antiIdle) { this.antiIdle = antiIdle; }

    public boolean isNoResponderCTCP() { return noResponderCTCP; }
    public void setNoResponderCTCP(boolean noResponderCTCP) { this.noResponderCTCP = noResponderCTCP; }

    public boolean isAvisoMencion() { return avisoMencion; }
    public void setAvisoMencion(boolean avisoMencion) { this.avisoMencion = avisoMencion; }

    public boolean isRelocalizadoNicks() { return relocalizadoNicks; }
    public void setRelocalizadoNicks(boolean relocalizadoNicks) { this.relocalizadoNicks = relocalizadoNicks; }

    public boolean isBloquearPrivados() { return bloquearPrivados; }
    public void setBloquearPrivados(boolean bloquearPrivados) { this.bloquearPrivados = bloquearPrivados; }

    public String getOtros() { return otros; }
    public void setOtros(String otros) { this.otros = otros; }

    public String getNick() { return nick; }
    public void setNick(String nick) { this.nick = nick; }

    public String getMensajeExcusa() { return mensajeExcusa; }
    public void setMensajeExcusa(String mensajeExcusa) { this.mensajeExcusa = mensajeExcusa; }

    public String getLimitePrivados() { return limitePrivados; }
    public void setLimitePrivados(String limitePrivados) { this.limitePrivados = limitePrivados; }
}
