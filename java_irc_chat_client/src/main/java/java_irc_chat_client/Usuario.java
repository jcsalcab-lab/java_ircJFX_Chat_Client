package java_irc_chat_client;

import jakarta.xml.bind.annotation.XmlAttribute;

public class Usuario {

    private String nick;
    private String comentario;
    private String sonido;

    public Usuario() {}

    public Usuario(String nick, String comentario, String sonido) {
        this.nick = nick;
        this.comentario = comentario;
        this.sonido = sonido;
    }

    @XmlAttribute(name = "nick")
    public String getNick() { return nick; }
    public void setNick(String nick) { this.nick = nick; }

    @XmlAttribute(name = "comentario")
    public String getComentario() { return comentario; }
    public void setComentario(String comentario) { this.comentario = comentario; }

    @XmlAttribute(name = "sonido")
    public String getSonido() { return sonido; }
    public void setSonido(String sonido) { this.sonido = sonido; }
}
