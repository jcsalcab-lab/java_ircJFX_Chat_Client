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
        try (Socket socket = new Socket(host, port);
             BufferedInputStream in = new BufferedInputStream(socket.getInputStream());
             FileOutputStream fos = new FileOutputStream(destFile)) {

            byte[] buffer = new byte[4096];
            int read;
            long totalRead = 0;

            while ((read = in.read(buffer)) != -1) {
                fos.write(buffer, 0, read);
                totalRead += read;
                if (progressCallback != null && fileSize > 0) {
                    progressCallback.accept(totalRead, fileSize);
                }
            }
            fos.flush();
            System.out.println("Archivo recibido: " + destFile.getAbsolutePath());
        }
    }
}
