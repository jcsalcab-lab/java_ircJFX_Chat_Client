package java_irc_chat_client;

public class Usuarios_Ignorados {
    private String nick;
    private String userHost;
    private String flags;

    public Usuarios_Ignorados(String nick, String userHost, String flags) {
        this.nick = nick;
        this.userHost = userHost;
        this.flags = flags;
    }

    // ----- Getters -----
    public String getNick() {
        return nick;
    }

    public String getUserHost() {
        return userHost;
    }

    public String getFlags() {
        return flags;
    }

    // ----- Setters -----
    public void setNick(String nick) {
        this.nick = nick;
    }

    public void setUserHost(String userHost) {
        this.userHost = userHost;
    }

    public void setFlags(String flags) {
        this.flags = flags;
    }
}

