package Cliente_DCC;

import java.util.HashMap;
import java.util.Map;

import java_irc_chat_client.PrivadoController;

public class DCCManager {

    private static final Map<String, java.io.File> outgoingFiles = new HashMap<>();

    public static void addOutgoingFile(String nick, java.io.File file) {
        outgoingFiles.put(nick, file);
    }
    
    
    private PrivadoController privadoController;

    public DCCManager(PrivadoController privadoController) {
        this.privadoController = privadoController;
    }

    public static void startSendingFile(String nick, String fileName) {
        java.io.File file = outgoingFiles.get(nick);
        if (file == null) return;

        new Thread(() -> {
            try (var in = new java.io.FileInputStream(file)) {
                // Aquí deberías abrir un socket DCC y enviar bytes
                // Simulación: mostramos mensajes de inicio y fin
                System.out.println("Iniciando envío a " + nick + ": " + fileName);
                Thread.sleep(1000); // Simulación
                System.out.println("Finalizado envío a " + nick + ": " + fileName);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public static void receiveFile(String nick, String fileName) {
        new Thread(() -> {
            try {
                // Aquí abrirías un socket DCC y guardarías en descargas
                System.out.println("Recibiendo " + fileName + " de " + nick);
                Thread.sleep(1000); // Simulación
                System.out.println("Archivo recibido de " + nick + ": " + fileName);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}
