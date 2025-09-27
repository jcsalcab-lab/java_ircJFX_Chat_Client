package java_irc_chat_client.formularios_persistencia;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "panelConfig")
public class FormularioInterfazPanelConfig {

    // ---- Campos principales ----
    private boolean activo;      // Renombrado (antes 'activa')
    private boolean mostrarLag;
    private boolean player;

    private List<Icono> iconos = new ArrayList<>();
    private String descripcion;
    private String comando;
    private List<String> apartados = new ArrayList<>();

    // ---- Getters / Setters ----

    @XmlElement
    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    @XmlElement
    public boolean isMostrarLag() {
        return mostrarLag;
    }

    public void setMostrarLag(boolean mostrarLag) {
        this.mostrarLag = mostrarLag;
    }

    @XmlElement
    public boolean isPlayer() {
        return player;
    }

    public void setPlayer(boolean player) {
        this.player = player;
    }

    @XmlElement
    public List<Icono> getIconos() {
        return iconos;
    }

    public void setIconos(List<Icono> iconos) {
        this.iconos = iconos;
    }

    @XmlElement
    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @XmlElement
    public String getComando() {
        return comando;
    }

    public void setComando(String comando) {
        this.comando = comando;
    }

    @XmlElement
    public List<String> getApartados() {
        return apartados;
    }

    public void setApartados(List<String> apartados) {
        this.apartados = apartados;
    }

    // ---- Clase interna para iconos ----
    @XmlRootElement(name = "icono")
    public static class Icono {
        private int numero;
        private String ico;
        private String descripcion;
        private String comando;

        @XmlElement
        public int getNumero() {
            return numero;
        }

        public void setNumero(int numero) {
            this.numero = numero;
        }

        @XmlElement
        public String getIco() {
            return ico;
        }

        public void setIco(String ico) {
            this.ico = ico;
        }

        @XmlElement
        public String getDescripcion() {
            return descripcion;
        }

        public void setDescripcion(String descripcion) {
            this.descripcion = descripcion;
        }

        @XmlElement
        public String getComando() {
            return comando;
        }

        public void setComando(String comando) {
            this.comando = comando;
        }
    }
}
