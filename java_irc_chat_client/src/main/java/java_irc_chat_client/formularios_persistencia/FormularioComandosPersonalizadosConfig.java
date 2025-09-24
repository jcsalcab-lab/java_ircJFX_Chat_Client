package java_irc_chat_client.formularios_persistencia;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase que representa la configuración de los comandos personalizados.
 */
@XmlRootElement(name = "comandosPersonalizadosConfig")
public class FormularioComandosPersonalizadosConfig {

    private List<ComandoItem> comandos;  // Lista de comandos
    private boolean activo;              // Valor del checkbox chkActivo
    private boolean ocultar;             // Valor del checkbox chkOcultar
    private String prefijo;              // Valor del TextField txtPrefijo

    public FormularioComandosPersonalizadosConfig() {
        this.comandos = new ArrayList<>();
        this.prefijo = ".";
    }

    @XmlElementWrapper(name = "comandos")
    @XmlElement(name = "comando")
    public List<ComandoItem> getComandos() {
        return comandos;
    }

    public void setComandos(List<ComandoItem> comandos) {
        this.comandos = comandos;
    }

    @XmlElement
    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    @XmlElement
    public boolean isOcultar() {
        return ocultar;
    }

    public void setOcultar(boolean ocultar) {
        this.ocultar = ocultar;
    }

    @XmlElement
    public String getPrefijo() {
        return prefijo;
    }

    public void setPrefijo(String prefijo) {
        this.prefijo = prefijo;
    }

    /**
     * Clase interna que representa un comando personalizado (una fila en la tabla)
     */
    @XmlRootElement(name = "comandoItem")
    public static class ComandoItem {
        private String comando;
        private String descripcion;

        public ComandoItem() {
        }

        public ComandoItem(String comando, String descripcion) {
            this.comando = comando;
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
        public String getDescripcion() {
            return descripcion;
        }

        public void setDescripcion(String descripcion) {
            this.descripcion = descripcion;
        }
    }
}
