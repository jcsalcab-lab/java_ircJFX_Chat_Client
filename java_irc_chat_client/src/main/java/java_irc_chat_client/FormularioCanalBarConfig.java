package java_irc_chat_client;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "canalBarConfig")
public class FormularioCanalBarConfig {

    private boolean activa;
    private List<ItemComando> privados = new ArrayList<>();
    private List<ItemComando> canales = new ArrayList<>();
    private String descripcion;
    private String comando;

    @XmlElement
    public boolean isActiva() { return activa; }
    public void setActiva(boolean activa) { this.activa = activa; }

    @XmlElement
    public List<ItemComando> getPrivados() { return privados; }
    public void setPrivados(List<ItemComando> privados) { this.privados = privados; }

    @XmlElement
    public List<ItemComando> getCanales() { return canales; }
    public void setCanales(List<ItemComando> canales) { this.canales = canales; }

    @XmlElement
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    @XmlElement
    public String getComando() { return comando; }
    public void setComando(String comando) { this.comando = comando; }

    public static class ItemComando {
        private String numero;
        private String comando;

        public ItemComando() {}
        public ItemComando(String numero, String comando) {
            this.numero = numero;
            this.comando = comando;
        }

        @XmlElement
        public String getNumero() { return numero; }
        public void setNumero(String numero) { this.numero = numero; }

        @XmlElement
        public String getComando() { return comando; }
        public void setComando(String comando) { this.comando = comando; }
    }
}
