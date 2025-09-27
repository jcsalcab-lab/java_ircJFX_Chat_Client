package Cliente_DCC;


import java.io.*;
import java.net.*;
import java.nio.file.Files;

import org.pircbotx.PircBotX;

public class DccFileSender {

    private final File file;
    private final String destinatario;
    private final String filename;

    public DccFileSender(File file, String destinatario) {
        this.file = file;
        this.destinatario = destinatario;
        this.filename = file.getName();
    }

    public void send(PircBotX bot) throws IOException {
        ServerSocket serverSocket = new ServerSocket(0); // puerto libre
        int port = serverSocket.getLocalPort();
        String ip = InetAddress.getLocalHost().getHostAddress();
        long size = file.length();

        // Aseguramos que el filename vaya entre comillas (y escapamos comillas internas)
        String safeFilename = filename.replace("\"", "\\\""); 
        long ipLong = ipToLong(ip);

        // Payload DCC (sin \u0001; lo añadimos a la raw line)
        String dccPayload = "DCC SEND \"" + safeFilename + "\" " + ipLong + " " + port + " " + size;

        // Construimos el CTCP crudo
        String ctcp = "\u0001" + dccPayload + "\u0001";

        // Enviar la línea cruda PRIVMSG (CTCP)
        String rawLine = "PRIVMSG " + destinatario + " :" + ctcp;
        bot.sendRaw().rawLineNow(rawLine);

        System.out.println("CTCP DCC enviado a " + destinatario + ": " + dccPayload);
        System.out.println("Esperando conexión DCC para: " + filename + " puerto: " + port);

        try (Socket socket = serverSocket.accept();
             BufferedOutputStream out = new BufferedOutputStream(socket.getOutputStream());
             FileInputStream fis = new FileInputStream(file)) {

            byte[] buffer = new byte[4096];
            int read;
            while ((read = fis.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            out.flush();
            System.out.println("Archivo enviado correctamente a " + destinatario);
        } finally {
            serverSocket.close();
        }
    }


    // Convierte IP a long (requerido por protocolo DCC)
    private long ipToLong(String ip) {
        String[] parts = ip.split("\\.");
        long result = 0;
        for (String part : parts) {
            result = result << 8;
            result |= Integer.parseInt(part) & 0xFF;
        }
        return result;
    }
}
