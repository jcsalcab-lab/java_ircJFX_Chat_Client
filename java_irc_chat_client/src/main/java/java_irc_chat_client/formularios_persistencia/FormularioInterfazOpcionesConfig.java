package java_irc_chat_client.formularios_persistencia;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "interfazOpcionesConfig")
public class FormularioInterfazOpcionesConfig {

    private String modoVentanas;

    private boolean panelCanalPv;
    private boolean abrirPrivados;
    private boolean cargarLog;
    private boolean dobleClickPriv;
    private boolean hotlinks;
    private boolean tooltips;

    private boolean whois;
    private boolean notices;
    private boolean notify;
    private boolean ctcp;

    private boolean ventanasNoticias;
    private String filtrosNoticias;

    // --- Getters y Setters ---
    @XmlElement public String getModoVentanas() { return modoVentanas; }
    public void setModoVentanas(String modoVentanas) { this.modoVentanas = modoVentanas; }

    @XmlElement public boolean isPanelCanalPv() { return panelCanalPv; }
    public void setPanelCanalPv(boolean panelCanalPv) { this.panelCanalPv = panelCanalPv; }

    @XmlElement public boolean isAbrirPrivados() { return abrirPrivados; }
    public void setAbrirPrivados(boolean abrirPrivados) { this.abrirPrivados = abrirPrivados; }

    @XmlElement public boolean isCargarLog() { return cargarLog; }
    public void setCargarLog(boolean cargarLog) { this.cargarLog = cargarLog; }

    @XmlElement public boolean isDobleClickPriv() { return dobleClickPriv; }
    public void setDobleClickPriv(boolean dobleClickPriv) { this.dobleClickPriv = dobleClickPriv; }

    @XmlElement public boolean isHotlinks() { return hotlinks; }
    public void setHotlinks(boolean hotlinks) { this.hotlinks = hotlinks; }

    @XmlElement public boolean isTooltips() { return tooltips; }
    public void setTooltips(boolean tooltips) { this.tooltips = tooltips; }

    @XmlElement public boolean isWhois() { return whois; }
    public void setWhois(boolean whois) { this.whois = whois; }

    @XmlElement public boolean isNotices() { return notices; }
    public void setNotices(boolean notices) { this.notices = notices; }

    @XmlElement public boolean isNotify() { return notify; }
    public void setNotify(boolean notify) { this.notify = notify; }

    @XmlElement public boolean isCtcp() { return ctcp; }
    public void setCtcp(boolean ctcp) { this.ctcp = ctcp; }

    @XmlElement public boolean isVentanasNoticias() { return ventanasNoticias; }
    public void setVentanasNoticias(boolean ventanasNoticias) { this.ventanasNoticias = ventanasNoticias; }

    @XmlElement public String getFiltrosNoticias() { return filtrosNoticias; }
    public void setFiltrosNoticias(String filtrosNoticias) { this.filtrosNoticias = filtrosNoticias; }
}
