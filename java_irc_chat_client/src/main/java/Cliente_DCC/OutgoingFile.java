package Cliente_DCC;

import java.io.File;
import java.net.ServerSocket;

public class OutgoingFile {
    private File file;
    private ServerSocket serverSocket;
    private int port;
    private long ipLong;
    private long size;

    public OutgoingFile(File file, ServerSocket serverSocket, int port, long ipLong, long size) {
        this.file = file;
        this.serverSocket = serverSocket;
        this.port = port;
        this.ipLong = ipLong;
        this.size = size;
    }

    // Getters y setters
    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    public void setServerSocket(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public long getIpLong() {
        return ipLong;
    }

    public void setIpLong(long ipLong) {
        this.ipLong = ipLong;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }
}
