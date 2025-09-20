package java_irc_chat_client;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "usuarios")
public class UsuariosList {

    private List<Usuario> usuarios;

    @XmlElement(name = "usuario")
    public List<Usuario> getUsuarios() { return usuarios; }
    public void setUsuarios(List<Usuario> usuarios) { this.usuarios = usuarios; }
}

