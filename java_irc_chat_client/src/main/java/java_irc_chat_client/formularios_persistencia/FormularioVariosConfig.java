package java_irc_chat_client.formularios_persistencia;

import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "config")
public class FormularioVariosConfig {

    // ===== En #canales =====
    public boolean chkIntentaEntrarBan;
    public boolean chkReentrarSalirTodos;
    public String txtContadorKicks;
    public boolean chkUsarContadorKicks;
    public String txtUsando;
    public String txtLimiteUsuariosIAL;

    // ===== Antivirus =====
    public String txtAntivirusPath;
    public String txtSigueNick;
    public boolean chkMostrarConsejo;

    // ===== Listas de usuarios =====
    public boolean chkAutoOp;
    public boolean chkProtegidos;
    public boolean chkAlObtenerArroba;
    public boolean chkAutoVoz;
    public boolean chkAutoKick;

    // ===== Configuración general =====
    // Puedes agregar campos generales si es necesario

    // Constructor vacío requerido por JAXB
    public FormularioVariosConfig() {}
}

