package java_irc_chat_client;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "mediaConfig")
public class formularioMediaConfig {

    private boolean sonidoLocal;
    private String eventoSeleccionado;
    private String sonidoPath;
    private String audioDir;
    private String videoDir;

    public boolean isSonidoLocal() {
        return sonidoLocal;
    }

    @XmlElement
    public void setSonidoLocal(boolean sonidoLocal) {
        this.sonidoLocal = sonidoLocal;
    }

    public String getEventoSeleccionado() {
        return eventoSeleccionado;
    }

    @XmlElement
    public void setEventoSeleccionado(String eventoSeleccionado) {
        this.eventoSeleccionado = eventoSeleccionado;
    }

    public String getSonidoPath() {
        return sonidoPath;
    }

    @XmlElement
    public void setSonidoPath(String sonidoPath) {
        this.sonidoPath = sonidoPath;
    }

    public String getAudioDir() {
        return audioDir;
    }

    @XmlElement
    public void setAudioDir(String audioDir) {
        this.audioDir = audioDir;
    }

    public String getVideoDir() {
        return videoDir;
    }

    @XmlElement
    public void setVideoDir(String videoDir) {
        this.videoDir = videoDir;
    }
}
