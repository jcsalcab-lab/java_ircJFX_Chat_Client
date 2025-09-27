package java_irc_chat_client.formularios_persistencia;

import jakarta.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "FormularioVariosConfig")
@XmlAccessorType(XmlAccessType.FIELD)
public class FormularioVariosConfig {

    // ===== Subpestaña VARIOS =====
    private boolean chkIntentaEntrarBan;
    private boolean chkReentrarSalirTodos;
    private String txtContadorKicks;
    private boolean chkUsarContadorKicks;
    private String txtUsando;
    private String txtLimiteUsuariosIAL;

    private String txtAntivirusPath;
    private String txtSigueNick;
    private boolean chkMostrarConsejo;

    private boolean chkAutoOp;
    private boolean chkProtegidos;
    private boolean chkAlObtenerArroba;
    private boolean chkAutoVoz;
    private boolean chkAutoKick;

    // ===== Subpestaña IDIOMA =====
    @XmlElementWrapper(name = "idiomas")
    @XmlElement(name = "idioma")
    private List<String> idiomas = new ArrayList<>();
    private boolean chkCambiarTema;

    // ===== Subpestaña COMANDOS PERSONALIZADOS =====
    @XmlElementWrapper(name = "comandos")
    @XmlElement(name = "comando")
    private List<String> comandos = new ArrayList<>();
    private boolean chkActivo;
    private boolean chkOcultar;
    private String txtPrefijo;

    public FormularioVariosConfig() {
        idiomas = new ArrayList<>();
        comandos = new ArrayList<>();
    }

    // ===== Getters y Setters =====
    public boolean isChkIntentaEntrarBan() { return chkIntentaEntrarBan; }
    public void setChkIntentaEntrarBan(boolean chkIntentaEntrarBan) { this.chkIntentaEntrarBan = chkIntentaEntrarBan; }

    public boolean isChkReentrarSalirTodos() { return chkReentrarSalirTodos; }
    public void setChkReentrarSalirTodos(boolean chkReentrarSalirTodos) { this.chkReentrarSalirTodos = chkReentrarSalirTodos; }

    public String getTxtContadorKicks() { return txtContadorKicks; }
    public void setTxtContadorKicks(String txtContadorKicks) { this.txtContadorKicks = txtContadorKicks; }

    public boolean isChkUsarContadorKicks() { return chkUsarContadorKicks; }
    public void setChkUsarContadorKicks(boolean chkUsarContadorKicks) { this.chkUsarContadorKicks = chkUsarContadorKicks; }

    public String getTxtUsando() { return txtUsando; }
    public void setTxtUsando(String txtUsando) { this.txtUsando = txtUsando; }

    public String getTxtLimiteUsuariosIAL() { return txtLimiteUsuariosIAL; }
    public void setTxtLimiteUsuariosIAL(String txtLimiteUsuariosIAL) { this.txtLimiteUsuariosIAL = txtLimiteUsuariosIAL; }

    public String getTxtAntivirusPath() { return txtAntivirusPath; }
    public void setTxtAntivirusPath(String txtAntivirusPath) { this.txtAntivirusPath = txtAntivirusPath; }

    public String getTxtSigueNick() { return txtSigueNick; }
    public void setTxtSigueNick(String txtSigueNick) { this.txtSigueNick = txtSigueNick; }

    public boolean isChkMostrarConsejo() { return chkMostrarConsejo; }
    public void setChkMostrarConsejo(boolean chkMostrarConsejo) { this.chkMostrarConsejo = chkMostrarConsejo; }

    public boolean isChkAutoOp() { return chkAutoOp; }
    public void setChkAutoOp(boolean chkAutoOp) { this.chkAutoOp = chkAutoOp; }

    public boolean isChkProtegidos() { return chkProtegidos; }
    public void setChkProtegidos(boolean chkProtegidos) { this.chkProtegidos = chkProtegidos; }

    public boolean isChkAlObtenerArroba() { return chkAlObtenerArroba; }
    public void setChkAlObtenerArroba(boolean chkAlObtenerArroba) { this.chkAlObtenerArroba = chkAlObtenerArroba; }

    public boolean isChkAutoVoz() { return chkAutoVoz; }
    public void setChkAutoVoz(boolean chkAutoVoz) { this.chkAutoVoz = chkAutoVoz; }

    public boolean isChkAutoKick() { return chkAutoKick; }
    public void setChkAutoKick(boolean chkAutoKick) { this.chkAutoKick = chkAutoKick; }

    public List<String> getIdiomas() { return idiomas; }
    public void setIdiomas(List<String> idiomas) { this.idiomas = idiomas; }

    public boolean isChkCambiarTema() { return chkCambiarTema; }
    public void setChkCambiarTema(boolean chkCambiarTema) { this.chkCambiarTema = chkCambiarTema; }

    public List<String> getComandos() { return comandos; }
    public void setComandos(List<String> comandos) { this.comandos = comandos; }

    public boolean isChkActivo() { return chkActivo; }
    public void setChkActivo(boolean chkActivo) { this.chkActivo = chkActivo; }

    public boolean isChkOcultar() { return chkOcultar; }
    public void setChkOcultar(boolean chkOcultar) { this.chkOcultar = chkOcultar; }

    public String getTxtPrefijo() { return txtPrefijo; }
    public void setTxtPrefijo(String txtPrefijo) { this.txtPrefijo = txtPrefijo; }
}
