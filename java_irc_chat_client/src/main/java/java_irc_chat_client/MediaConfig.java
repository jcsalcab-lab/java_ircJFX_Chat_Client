package java_irc_chat_client;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "mediaConfig")
public class MediaConfig {

    private boolean sonidoLocal;
    private String eventoSeleccionado;
    private String sonidoPath;
    private String audioDir;
    private String videoDir;

    @XmlElement
    public boolean isSonidoLocal() { return sonidoLocal; }
    public void setSonidoLocal(boolean sonidoLocal) { this.sonidoLocal = sonidoLocal; }

    @XmlElement
    public String getEventoSeleccionado() { return eventoSeleccionado; }
    public void setEventoSeleccionado(String eventoSeleccionado) { this.eventoSeleccionado = eventoSeleccionado; }

    @XmlElement
    public String getSonidoPath() { return sonidoPath; }
    public void setSonidoPath(String sonidoPath) { this.sonidoPath = sonidoPath; }

    @XmlElement
    public String getAudioDir() { return audioDir; }
    public void setAudioDir(String audioDir) { this.audioDir = audioDir; }

    @XmlElement
    public String getVideoDir() { return videoDir; }
    public void setVideoDir(String videoDir) { this.videoDir = videoDir; }
}
