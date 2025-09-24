package java_irc_chat_client;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SetupController {

    @FXML
    private TabPane setupTabPane;

    // Lista genérica de todos los controladores de Tabs
    private final List<Object> tabControllers = new ArrayList<>();

    @FXML
    private void initialize() {
        System.out.println("SetupController inicializado.");

        try {
            // Cargar todos los Tabs con sus FXML y controladores
            cargarTab("/java_irc_chat_client/JIRCHAT_SETUP_TAB_GENERAL.fxml", 0);
            cargarTab("/java_irc_chat_client/JIRCHAT_SETUP_TAB_PROTECCION.fxml", 1);
            cargarTab("/java_irc_chat_client/JIRCHAT_SETUP_TAB_AWAY.fxml", 2);
            cargarTab("/java_irc_chat_client/JIRCHAT_SETUP_TAB_MEDIA.fxml", 3);
            cargarTab("/java_irc_chat_client/JIRCHAT_SETUP_TAB_INTERFAZ.fxml", 4);
            cargarTab("/java_irc_chat_client/JIRCHAT_SETUP_TAB_DCC.fxml", 5);
            cargarTab("/java_irc_chat_client/JIRCHAT_SETUP_TAB_ESCRITURA.fxml", 6);
            cargarTab("/java_irc_chat_client/JIRCHAT_SETUP_TAB_REDES.fxml", 7);
            cargarTab("/java_irc_chat_client/JIRCHAT_SETUP_TAB_VARIOS.fxml", 8);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    
    
    private void cargarTab(String fxmlPath, int tabIndex) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        Parent tabRoot = loader.load();  // Esto inicializa todos los @FXML
        Object controller = loader.getController();
        tabRoot.setUserData(controller);

        Tab tab = setupTabPane.getTabs().get(tabIndex);
        tab.setContent(tabRoot);

        tabControllers.add(controller); // Guardamos el controlador para luego
    }


    /**
     * Debe llamarse desde quien crea el Stage para capturar el evento de cierre.
     */
    public void setStage(Stage stage) {
        stage.setOnCloseRequest(event -> {
            guardarTodasConfiguraciones();
        });
    }

    /**
     * Llama a todos los métodos de persistencia de cada controlador.
     */
    private void guardarTodasConfiguraciones() {
        for (Object controller : tabControllers) {
            if (controller == null) continue; // ignorar null
            try {
                controller.getClass().getMethod("guardarConfiguracion").invoke(controller);
            } catch (NoSuchMethodException nsme) {
                // Ignorar controladores que no tengan guardarConfiguracion
            } catch (Exception e) {
                // Log para que no rompa la aplicación
                System.err.println("Error guardando configuración en " + controller.getClass().getSimpleName());
                e.printStackTrace();
            }
        }
        System.out.println("Todas las configuraciones se han guardado correctamente.");
    }

}
