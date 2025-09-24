package java_irc_chat_client;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "setupConfig")
public class FormularioSetupEscrituraSets {

    // CheckBoxes principales
    private boolean correctoOrtografico;
    private boolean conversorUnidades;
    private boolean encriptador;
    private boolean nickCompletion;
    private boolean adornosColores;

    // Coloreado de nicks
    private String colorMenor;
    private String colorArroba;
    private String colorNick;
    private String colorMayor;

    // Mostrar tu nick
    private boolean mostrarNickActivo;
    private String miNickMenor;
    private String miNickArroba;
    private String miNickNick;
    private String miNickMayor;

    // Justificación de texto
    private int justificacionValor;
    private boolean incluirArrobaMas;

    // --- Getters y setters ---
    @XmlElement
    public boolean isCorrectoOrtografico() { return correctoOrtografico; }
    public void setCorrectoOrtografico(boolean correctoOrtografico) { this.correctoOrtografico = correctoOrtografico; }

    @XmlElement
    public boolean isConversorUnidades() { return conversorUnidades; }
    public void setConversorUnidades(boolean conversorUnidades) { this.conversorUnidades = conversorUnidades; }

    @XmlElement
    public boolean isEncriptador() { return encriptador; }
    public void setEncriptador(boolean encriptador) { this.encriptador = encriptador; }

    @XmlElement
    public boolean isNickCompletion() { return nickCompletion; }
    public void setNickCompletion(boolean nickCompletion) { this.nickCompletion = nickCompletion; }

    @XmlElement
    public boolean isAdornosColores() { return adornosColores; }
    public void setAdornosColores(boolean adornosColores) { this.adornosColores = adornosColores; }

    @XmlElement
    public String getColorMenor() { return colorMenor; }
    public void setColorMenor(String colorMenor) { this.colorMenor = colorMenor; }

    @XmlElement
    public String getColorArroba() { return colorArroba; }
    public void setColorArroba(String colorArroba) { this.colorArroba = colorArroba; }

    @XmlElement
    public String getColorNick() { return colorNick; }
    public void setColorNick(String colorNick) { this.colorNick = colorNick; }

    @XmlElement
    public String getColorMayor() { return colorMayor; }
    public void setColorMayor(String colorMayor) { this.colorMayor = colorMayor; }

    @XmlElement
    public boolean isMostrarNickActivo() { return mostrarNickActivo; }
    public void setMostrarNickActivo(boolean mostrarNickActivo) { this.mostrarNickActivo = mostrarNickActivo; }

    @XmlElement
    public String getMiNickMenor() { return miNickMenor; }
    public void setMiNickMenor(String miNickMenor) { this.miNickMenor = miNickMenor; }

    @XmlElement
    public String getMiNickArroba() { return miNickArroba; }
    public void setMiNickArroba(String miNickArroba) { this.miNickArroba = miNickArroba; }

    @XmlElement
    public String getMiNickNick() { return miNickNick; }
    public void setMiNickNick(String miNickNick) { this.miNickNick = miNickNick; }

    @XmlElement
    public String getMiNickMayor() { return miNickMayor; }
    public void setMiNickMayor(String miNickMayor) { this.miNickMayor = miNickMayor; }

    @XmlElement
    public int getJustificacionValor() { return justificacionValor; }
    public void setJustificacionValor(int justificacionValor) { this.justificacionValor = justificacionValor; }

    @XmlElement
    public boolean isIncluirArrobaMas() { return incluirArrobaMas; }
    public void setIncluirArrobaMas(boolean incluirArrobaMas) { this.incluirArrobaMas = incluirArrobaMas; }
}
