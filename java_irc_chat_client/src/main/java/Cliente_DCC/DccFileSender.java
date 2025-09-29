package Cliente_DCC;

import java.io.*;
import java.net.*;
import java.util.function.BiConsumer;

import org.pircbotx.PircBotX;

public class DccFileSender {

    private final File file;
    private final String destinatario;

    public DccFileSender(File file, String destinatario) {
        this.file = file;
        this.destinatario = destinatario;
    }

    public void send(PircBotX bot, BiConsumer<Long, Long> progressCallback) throws IOException {
        // Creamos un ServerSocket en un puerto libre
        try (ServerSocket serverSocket = new ServerSocket(0)) {
            int port = serverSocket.getLocalPort();
            String ip = InetAddress.getLocalHost().getHostAddress();
            long size = file.length();

            // Aseguramos que el nombre de archivo no rompa el formato
            String safeFilename = file.getName().replace("\"", "");
            long ipLong = ipToLong(ip);

            // Construimos el mensaje CTCP DCC SEND
            String ctcp = "\u0001DCC SEND \"" + safeFilename + "\" " + ipLong + " " + port + " " + size + "\u0001";

            // Lo enviamos al destinatario
            bot.sendRaw().rawLineNow("PRIVMSG " + destinatario + " :" + ctcp);

            // DEBUG
            System.out.println("=== DccFileSender DEBUG ===");
            System.out.println("Archivo a enviar: " + file.getAbsolutePath());
            System.out.println("Destinatario: " + destinatario);
            System.out.println("IP local: " + ip + " (long=" + ipLong + ")");
            System.out.println("Puerto: " + port);
            System.out.println("Tamaño: " + size + " bytes");
            System.out.println("Mensaje CTCP enviado: " + ctcp);

            // Esperamos que el receptor se conecte
            try (Socket socket = serverSocket.accept();
                 BufferedOutputStream out = new BufferedOutputStream(socket.getOutputStream());
                 FileInputStream fis = new FileInputStream(file)) {

                System.out.println("Conexión establecida con receptor en " +
                        socket.getInetAddress() + ":" + socket.getPort());

                byte[] buffer = new byte[4096];
                int read;
                long totalSent = 0;

                while ((read = fis.read(buffer)) != -1) {
                    out.write(buffer, 0, read);
                    totalSent += read;

                    if (progressCallback != null) {
                        progressCallback.accept(totalSent, size);
                    }
                }
                out.flush();

                System.out.println("Archivo enviado con éxito. Total: " + totalSent + " bytes");
            }
        }
    }

    // Convierte IP en formato "127.0.0.1" a número long sin signo
    private long ipToLong(String ip) {
        String[] parts = ip.split("\\.");
        long result = 0;
        for (String part : parts) {
            result = (result << 8) | (Integer.parseInt(part) & 0xFF);
        }
        return result;
    }
}
