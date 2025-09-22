package java_irc_chat_client;

import java.io.*;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ChatLogger {

    private static final String LOGS_DIR =
            System.getProperty("user.dir") + File.separator + "logs";
    private static final DateTimeFormatter TS_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private static BufferedWriter getWriter(String name) throws IOException {
        File dir = new File(LOGS_DIR);
        if (!dir.exists()) dir.mkdirs();
        File logFile = new File(dir, name + ".log");
        return new BufferedWriter(new FileWriter(logFile, true));
    }

    public static void log(String name, String usuario, String mensaje) {
        try (BufferedWriter writer = getWriter(name)) {
            String timestamp = LocalDateTime.now().format(TS_FORMAT);
            writer.write("[" + timestamp + "] <" + usuario + "> " + mensaje);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    

    public static void logSystem(String name, String mensaje) {
        try (BufferedWriter writer = getWriter(name)) {
            String timestamp = LocalDateTime.now().format(TS_FORMAT);
            writer.write("[" + timestamp + "] * " + mensaje);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** Lista todos los logs disponibles */
    public static List<File> listarLogs() {
        File dir = new File(LOGS_DIR);
        if (!dir.exists()) dir.mkdirs();
        File[] files = dir.listFiles((d, name) -> name.endsWith(".log"));
        List<File> lista = new ArrayList<>();
        if (files != null) {
            for (File f : files) lista.add(f);
        }
        return lista;
    }

    /** Busca logs cuyo nombre contenga la cadena o cuyo contenido la contenga */
    public static List<File> buscarLogs(String query) {
        if (query == null || query.isEmpty()) return listarLogs();
        List<File> resultados = new ArrayList<>();
        for (File log : listarLogs()) {
            if (log.getName().toLowerCase().contains(query.toLowerCase()) ||
                contenidoContiene(log, query)) {
                resultados.add(log);
            }
        }
        return resultados;
    }

    /** Comprueba si un fichero contiene la cadena */
    public static boolean contenidoContiene(File file, String query) {
        if (query == null || query.isEmpty()) return true;
        try {
            return Files.lines(file.toPath())
                        .anyMatch(line -> line.toLowerCase().contains(query.toLowerCase()));
        } catch (IOException e) {
            return false;
        }
    }
}
