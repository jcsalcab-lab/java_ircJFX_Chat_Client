package Cliente_DCC;

import java.io.File;
import java.net.ServerSocket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.pircbotx.PircBotX;
import java_irc_chat_client.PrivadoController;
public class DCCManager {

    // Map para archivos salientes, clave = nick
    private final Map<String, OutgoingFile> outgoingFiles = new ConcurrentHashMap<>();

    private final PrivadoController privadoController;

    public DCCManager(PrivadoController privadoController) {
        this.privadoController = privadoController;
    }

    // Añadir archivo saliente para enviar
    public void addOutgoingFile(String nick, File file, ServerSocket serverSocket, int port, long ipLong, long size) {
        OutgoingFile of = new OutgoingFile(file, serverSocket, port, ipLong, size);
        outgoingFiles.put(nick, of);
    }

    // Iniciar envío de archivo a nick
    public void startSendingFile(String nick, PircBotX bot) {
        OutgoingFile of = outgoingFiles.get(nick);
        if (of == null) {
            privadoController.appendSystemMessage("⚠ No hay archivo pendiente para enviar a " + nick);
            return;
        }

        File file = of.getFile();

        privadoController.appendSystemMessage("Transferencia aceptada de fichero " + file.getName());
        privadoController.appendSystemMessage("Iniciando transferencia de " + file.getName() + " (" + file.length() + " bytes)");

        // Mostrar barra progreso en 0 antes de empezar
        privadoController.actualizarBarraProgreso(0);

        new Thread(() -> {
            try {
                DccFileSender sender = new DccFileSender(file, nick);
                sender.send(bot, (transferred, total) -> {
                    double progreso = (double) transferred / total;
                    privadoController.actualizarBarraProgreso(progreso);
                });

                privadoController.appendSystemMessage("Transferencia del archivo " + file.getName() + " terminada.");
                privadoController.actualizarBarraProgreso(1.0); // Ocultar barra al finalizar
                outgoingFiles.remove(nick);
            } catch (Exception e) {
                privadoController.appendSystemMessage("⚠ Error enviando archivo: " + e.getMessage());
                privadoController.actualizarBarraProgreso(1.0); // Ocultar barra en error también
                e.printStackTrace();
            }
        }).start();
    }

    // Iniciar recepción de archivo entrante
    public void receiveFile(String nick, String fileName, String ip, int port, long fileSize) {
        try {
            File saveDir = new File("c:\\temp\\descargas");
            if (!saveDir.exists()) {
                saveDir.mkdirs();
            }
            File destino = new File(saveDir, fileName);

            privadoController.appendSystemMessage("Transferencia aceptada de fichero " + fileName);
            privadoController.actualizarBarraProgreso(0);

            DccFileReceiver receiver = new DccFileReceiver(ip, port, destino, fileSize);

            new Thread(() -> {
                try {
                    receiver.receive((transferred, total) -> {
                        double progreso = (double) transferred / total;
                        privadoController.actualizarBarraProgreso(progreso);
                    });

                    privadoController.appendSystemMessage("Transferencia del archivo " + fileName + " terminada en " + destino.getAbsolutePath());
                    privadoController.actualizarBarraProgreso(1.0);
                } catch (Exception e) {
                    privadoController.appendSystemMessage("⚠ Error recibiendo archivo: " + e.getMessage());
                    privadoController.actualizarBarraProgreso(1.0);
                    e.printStackTrace();
                }
            }).start();
        } catch (Exception e) {
            privadoController.appendSystemMessage("⚠ Error iniciando recepción: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public OutgoingFile getOutgoingFile(String nick) {
        return outgoingFiles.get(nick);
    }

    public void removeOutgoingFile(String nick) {
        outgoingFiles.remove(nick);
    }
    
    public void handleIncomingSend(PircBotX bot, String nick, String rawMsg, PrivadoController privadoController) throws Exception {
        try {
            // El mensaje tiene formato: "\u0001DCC SEND filename ip port size\u0001"
            String trimmed = rawMsg.trim();
            if (trimmed.startsWith("\u0001")) trimmed = trimmed.substring(1);
            if (trimmed.endsWith("\u0001")) trimmed = trimmed.substring(0, trimmed.length() - 1);

            String[] parts = trimmed.split(" ");
            if (parts.length < 6 || !parts[0].equals("DCC") || !parts[1].equals("SEND")) {
                privadoController.appendSystemMessage("Mensaje DCC SEND mal formado de " + nick);
                return;
            }

            String filename = parts[2];

            // Parsear IP que viene como número largo (long)
            long ipLong = Long.parseLong(parts[3]);
            String ip = ((ipLong >> 24) & 0xFF) + "." + ((ipLong >> 16) & 0xFF) + "." + ((ipLong >> 8) & 0xFF) + "." + (ipLong & 0xFF);

            int port = Integer.parseInt(parts[4]);
            long fileSize = Long.parseLong(parts[5]);

            privadoController.appendSystemMessage("Solicitud DCC SEND de " + nick + " para archivo " + filename);

            // Iniciar la recepción del archivo
            receiveFile(nick, filename, ip, port, fileSize);

        } catch (Exception e) {
            privadoController.appendSystemMessage("Error procesando solicitud DCC SEND: " + e.getMessage());
            throw e;
        }
    }

    public void handleIncomingAccept(PircBotX bot, String nick, String rawMsg, PrivadoController privadoController) throws Exception {
        try {
            // El mensaje tiene formato: "\u0001DCC ACCEPT filename port position\u0001"
            String trimmed = rawMsg.trim();
            if (trimmed.startsWith("\u0001")) trimmed = trimmed.substring(1);
            if (trimmed.endsWith("\u0001")) trimmed = trimmed.substring(0, trimmed.length() - 1);

            String[] parts = trimmed.split(" ");
            if (parts.length < 5 || !parts[0].equals("DCC") || !parts[1].equals("ACCEPT")) {
                privadoController.appendSystemMessage("Mensaje DCC ACCEPT mal formado de " + nick);
                return;
            }

            String filename = parts[2];
            int port = Integer.parseInt(parts[3]);
            long position = Long.parseLong(parts[4]);

            privadoController.appendSystemMessage("Solicitud DCC ACCEPT de " + nick + " para archivo " + filename + " en puerto " + port + " posición " + position);

            // Aquí, normalmente, se inicia la conexión para enviar archivo (continuar)
            startSendingFile(nick, bot);

        } catch (Exception e) {
            privadoController.appendSystemMessage("Error procesando aceptación DCC: " + e.getMessage());
            throw e;
        }
    }

}
