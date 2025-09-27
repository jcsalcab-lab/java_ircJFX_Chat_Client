package java_irc_chat_client;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SetupController {

    @FXML
    private TabPane setupTabPane;

    private final List<Object> tabControllers = new ArrayList<>();

    @FXML
    private void initialize() {
        System.out.println("SetupController inicializado.");

        try {
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
        Parent root = loader.load();
        Object controller = loader.getController();
        root.setUserData(controller);

        Tab tab = setupTabPane.getTabs().get(tabIndex);
        tab.setContent(root);

        tabControllers.add(controller);
    }

    public void setStage(Stage stage) {
        stage.setOnCloseRequest(event -> guardarTodasConfiguraciones());
    }

    private void guardarTodasConfiguraciones() {
        for (Object controller : tabControllers) {
            if (controller == null) continue;
            try {
                controller.getClass().getMethod("guardarTodasSubpestanas").invoke(controller);
            } catch (NoSuchMethodException e1) {
                // Si no tiene subpestañas, intentar guardarConfiguracion
                try {
                    controller.getClass().getMethod("guardarConfiguracion").invoke(controller);
                } catch (Exception e2) {
                    System.err.println("Error guardando configuración en " + controller.getClass().getSimpleName());
                    e2.printStackTrace();
                }
            } catch (Exception e) {
                System.err.println("Error guardando configuración en " + controller.getClass().getSimpleName());
                e.printStackTrace();
            }
        }
        System.out.println("Todas las configuraciones se han guardado correctamente.");
    }
}
