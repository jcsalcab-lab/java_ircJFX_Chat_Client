package Cliente_DCC;

import java.io.*;
import java.net.Socket;
import java.util.function.BiConsumer;

public class DccFileReceiver {
    private final String host;
    private final int port;
    private final File destFile;
    private final long fileSize;

    public DccFileReceiver(String host, int port, File destFile, long fileSize) {
        this.host = host;
        this.port = port;
        this.destFile = destFile;
        this.fileSize = fileSize;
    }

    public void receive(BiConsumer<Long, Long> progressCallback) throws IOException {
        System.out.println("Iniciando recepción de archivo: " + destFile.getName() + " desde " + host + ":" + port);
        System.out.println("Tamaño esperado: " + fileSize + " bytes");

        try (Socket socket = new Socket(host, port);
             BufferedInputStream in = new BufferedInputStream(socket.getInputStream());
             FileOutputStream fos = new FileOutputStream(destFile)) {

            byte[] buffer = new byte[4096];
            int read;
            long totalRead = 0;

            while ((read = in.read(buffer)) != -1) {
                fos.write(buffer, 0, read);
                totalRead += read;

                // Depuración de progreso
                if (progressCallback != null && fileSize > 0) {
                    progressCallback.accept(totalRead, fileSize);
                }

                // Cada MB leído mostramos mensaje
                if (totalRead % (1024 * 1024) < buffer.length) {
                    System.out.println("Recibido hasta ahora: " + totalRead + " bytes de " + fileSize);
                }
            }

            fos.flush();
            System.out.println("Recepción completada: " + destFile.getAbsolutePath());

        } catch (IOException e) {
            System.out.println("⚠ Error en recepción: " + e.getMessage());
            throw e;
        }
    }

}
