package java_irc_chat_client.formularios_persistencia;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.List;

@XmlRootElement(name = "variosVariosConfig")
public class FormularioVariosVariosConfig {

    // ===== En #canales =====
    private boolean chkIntentaEntrarBan;
    private boolean chkReentrarSalirTodos;
    private String txtContadorKicks;
    private boolean chkUsarContadorKicks;
    private String txtUsando;
    private String txtLimiteUsuariosIAL;

    // ===== Antivirus =====
    private String txtAntivirusPath;
    private String txtSigueNick;
    private boolean chkMostrarConsejo;

    // ===== Listas de usuarios =====
    private boolean chkAutoOp;
    private boolean chkProtegidos;
    private boolean chkAlObtenerArroba;
    private boolean chkAutoVoz;
    private boolean chkAutoKick;

    // ===== Interfaz / Apariencia =====
    private List<String> colores;   // Lista de colores
    private boolean animacionCerrar;
    private boolean mostrarTextura;
    private int redondeoGUI;

    // ===== Getters y Setters =====
    @XmlElement public boolean isChkIntentaEntrarBan() { return chkIntentaEntrarBan; }
    public void setChkIntentaEntrarBan(boolean chkIntentaEntrarBan) { this.chkIntentaEntrarBan = chkIntentaEntrarBan; }

    @XmlElement public boolean isChkReentrarSalirTodos() { return chkReentrarSalirTodos; }
    public void setChkReentrarSalirTodos(boolean chkReentrarSalirTodos) { this.chkReentrarSalirTodos = chkReentrarSalirTodos; }

    @XmlElement public String getTxtContadorKicks() { return txtContadorKicks; }
    public void setTxtContadorKicks(String txtContadorKicks) { this.txtContadorKicks = txtContadorKicks; }

    @XmlElement public boolean isChkUsarContadorKicks() { return chkUsarContadorKicks; }
    public void setChkUsarContadorKicks(boolean chkUsarContadorKicks) { this.chkUsarContadorKicks = chkUsarContadorKicks; }

    @XmlElement public String getTxtUsando() { return txtUsando; }
    public void setTxtUsando(String txtUsando) { this.txtUsando = txtUsando; }

    @XmlElement public String getTxtLimiteUsuariosIAL() { return txtLimiteUsuariosIAL; }
    public void setTxtLimiteUsuariosIAL(String txtLimiteUsuariosIAL) { this.txtLimiteUsuariosIAL = txtLimiteUsuariosIAL; }

    @XmlElement public String getTxtAntivirusPath() { return txtAntivirusPath; }
    public void setTxtAntivirusPath(String txtAntivirusPath) { this.txtAntivirusPath = txtAntivirusPath; }

    @XmlElement public String getTxtSigueNick() { return txtSigueNick; }
    public void setTxtSigueNick(String txtSigueNick) { this.txtSigueNick = txtSigueNick; }

    @XmlElement public boolean isChkMostrarConsejo() { return chkMostrarConsejo; }
    public void setChkMostrarConsejo(boolean chkMostrarConsejo) { this.chkMostrarConsejo = chkMostrarConsejo; }

    @XmlElement public boolean isChkAutoOp() { return chkAutoOp; }
    public void setChkAutoOp(boolean chkAutoOp) { this.chkAutoOp = chkAutoOp; }

    @XmlElement public boolean isChkProtegidos() { return chkProtegidos; }
    public void setChkProtegidos(boolean chkProtegidos) { this.chkProtegidos = chkProtegidos; }

    @XmlElement public boolean isChkAlObtenerArroba() { return chkAlObtenerArroba; }
    public void setChkAlObtenerArroba(boolean chkAlObtenerArroba) { this.chkAlObtenerArroba = chkAlObtenerArroba; }

    @XmlElement public boolean isChkAutoVoz() { return chkAutoVoz; }
    public void setChkAutoVoz(boolean chkAutoVoz) { this.chkAutoVoz = chkAutoVoz; }

    @XmlElement public boolean isChkAutoKick() { return chkAutoKick; }
    public void setChkAutoKick(boolean chkAutoKick) { this.chkAutoKick = chkAutoKick; }

    @XmlElementWrapper(name = "colores")
    @XmlElement(name = "color")
    public List<String> getColores() { return colores; }
    public void setColores(List<String> colores) { this.colores = colores; }

    @XmlElement public boolean isAnimacionCerrar() { return animacionCerrar; }
    public void setAnimacionCerrar(boolean animacionCerrar) { this.animacionCerrar = animacionCerrar; }

    @XmlElement public boolean isMostrarTextura() { return mostrarTextura; }
    public void setMostrarTextura(boolean mostrarTextura) { this.mostrarTextura = mostrarTextura; }

    @XmlElement public int getRedondeoGUI() { return redondeoGUI; }
    public void setRedondeoGUI(int redondeoGUI) { this.redondeoGUI = redondeoGUI; }
}
