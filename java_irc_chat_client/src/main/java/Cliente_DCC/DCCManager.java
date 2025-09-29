package Cliente_DCC;

import java.io.File;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.Map;
import java_irc_chat_client.PrivadoController;

public class DCCManager {

	private static final Map<String, OutgoingFile> outgoingFiles = new HashMap<>();


    private final PrivadoController privadoController;

    public DCCManager(PrivadoController privadoController) {
        this.privadoController = privadoController;
    }

    public static void addOutgoingFile(String nick, File file, ServerSocket serverSocket, int port, long ipLong, long size) {
        OutgoingFile of = new OutgoingFile(file, serverSocket, port, ipLong, size);
        outgoingFiles.put(nick, of);
    }



    // Envía el archivo al destinatario
    public void startSendingFile(String nick) {
    	 OutgoingFile of = outgoingFiles.get(nick);
        if (of == null) return;

        File file = of.getFile();
        
        PrivadoController controller = privadoController;
        privadoController.appendSystemMessage("Transferencia aceptada de fichero " + file.getName());
        privadoController.iniciarBarraProgreso(file.getName(), file.length());

        new Thread(() -> {
            try {
                DccFileSender sender = new DccFileSender(file, nick);
                sender.send(privadoController.getBot(), (transferred, total) -> privadoController.actualizarBarraProgreso((double) transferred / total));
                privadoController.finalizarBarraProgreso(file.getName());
                controller.appendSystemMessage("Transferencia del archivo " + file.getName() + " terminada en c:\\temp\\descargas");
            } catch (Exception e) {
            	privadoController.finalizarBarraProgreso(file.getName());
            	privadoController.appendSystemMessage("⚠ Error enviando archivo: " + e.getMessage());

            }
        }).start();
    }

    // Recibe archivo del nick
    public void receiveFile(String nick, String fileName, String ip, int port, long fileSize) {
        try {
            File saveDir = new File("c:\\temp\\descargas");
            if (!saveDir.exists()) saveDir.mkdirs();
            File destino = new File(saveDir, fileName);

            privadoController.appendSystemMessage("Transferencia aceptada de fichero " + fileName);
            privadoController.iniciarBarraProgreso(fileName, fileSize);

            DccFileReceiver receiver = new DccFileReceiver(ip, port, destino, fileSize);
            new Thread(() -> {
                try {
                    receiver.receive((transferred, total) -> privadoController.actualizarBarraProgreso((double) transferred / total));
                    privadoController.finalizarBarraProgreso(fileName);

                    privadoController.appendSystemMessage("Transferencia del archivo " + fileName + " terminada en " + destino.getAbsolutePath());
                } catch (Exception e) {
                    privadoController.finalizarBarraProgreso(fileName);
                    privadoController.appendSystemMessage("⚠ Error recibiendo archivo: " + e.getMessage());
                }
            }).start();
        } catch (Exception e) {
            privadoController.appendSystemMessage("⚠ Error iniciando recepción: " + e.getMessage());
        }
    }
}

