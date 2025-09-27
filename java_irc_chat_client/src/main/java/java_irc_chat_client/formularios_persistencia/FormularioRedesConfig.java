package java_irc_chat_client.formularios_persistencia;

import jakarta.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "FormularioRedesConfig")
@XmlAccessorType(XmlAccessType.FIELD)
public class FormularioRedesConfig {

    private String nombreRed;
    private String ficheroMenus;
    private String apareceComo;
    private String mascaraIpBots;
    private String nickserv;
    private String chanserv;
    private String memoserv;
    private String pideId;

    private boolean autoCargarMenus;
    private boolean mensajeBotsPrivado;
    private boolean sinPerfil;

    private boolean identificarNick;
    private boolean liberarOcupado;
    private boolean autoFundador;

    private boolean autoEntrar; // checkbox "Auto-entrar"
    private String retardo;

    @XmlElementWrapper(name = "nicks")
    @XmlElement(name = "nick")
    private List<String> nicks = new ArrayList<>();

    @XmlElementWrapper(name = "canales")
    @XmlElement(name = "canal")
    private List<String> canales = new ArrayList<>();

    @XmlElementWrapper(name = "canalesAutoEntrar") // antes estaba "autoEntrar"
    @XmlElement(name = "canal")
    private List<String> autoEntrarList = new ArrayList<>();

    public FormularioRedesConfig() {
        nicks = new ArrayList<>();
        canales = new ArrayList<>();
        autoEntrarList = new ArrayList<>();
    }

    // ----------------- Getters y Setters -----------------
    public String getNombreRed() { return nombreRed; }
    public void setNombreRed(String nombreRed) { this.nombreRed = nombreRed; }

    public String getFicheroMenus() { return ficheroMenus; }
    public void setFicheroMenus(String ficheroMenus) { this.ficheroMenus = ficheroMenus; }

    public String getApareceComo() { return apareceComo; }
    public void setApareceComo(String apareceComo) { this.apareceComo = apareceComo; }

    public String getMascaraIpBots() { return mascaraIpBots; }
    public void setMascaraIpBots(String mascaraIpBots) { this.mascaraIpBots = mascaraIpBots; }

    public String getNickserv() { return nickserv; }
    public void setNickserv(String nickserv) { this.nickserv = nickserv; }

    public String getChanserv() { return chanserv; }
    public void setChanserv(String chanserv) { this.chanserv = chanserv; }

    public String getMemoserv() { return memoserv; }
    public void setMemoserv(String memoserv) { this.memoserv = memoserv; }

    public String getPideId() { return pideId; }
    public void setPideId(String pideId) { this.pideId = pideId; }

    public boolean isAutoCargarMenus() { return autoCargarMenus; }
    public void setAutoCargarMenus(boolean autoCargarMenus) { this.autoCargarMenus = autoCargarMenus; }

    public boolean isMensajeBotsPrivado() { return mensajeBotsPrivado; }
    public void setMensajeBotsPrivado(boolean mensajeBotsPrivado) { this.mensajeBotsPrivado = mensajeBotsPrivado; }

    public boolean isSinPerfil() { return sinPerfil; }
    public void setSinPerfil(boolean sinPerfil) { this.sinPerfil = sinPerfil; }

    public boolean isIdentificarNick() { return identificarNick; }
    public void setIdentificarNick(boolean identificarNick) { this.identificarNick = identificarNick; }

    public boolean isLiberarOcupado() { return liberarOcupado; }
    public void setLiberarOcupado(boolean liberarOcupado) { this.liberarOcupado = liberarOcupado; }

    public boolean isAutoFundador() { return autoFundador; }
    public void setAutoFundador(boolean autoFundador) { this.autoFundador = autoFundador; }

    public boolean isAutoEntrar() { return autoEntrar; }
    public void setAutoEntrar(boolean autoEntrar) { this.autoEntrar = autoEntrar; }

    public String getRetardo() { return retardo; }
    public void setRetardo(String retardo) { this.retardo = retardo; }

    public List<String> getNicks() { return nicks; }
    public void setNicks(List<String> nicks) { this.nicks = nicks; }

    public List<String> getCanales() { return canales; }
    public void setCanales(List<String> canales) { this.canales = canales; }

    public List<String> getAutoEntrarList() { return autoEntrarList; }
    public void setAutoEntrarList(List<String> autoEntrarList) { this.autoEntrarList = autoEntrarList; }
}
