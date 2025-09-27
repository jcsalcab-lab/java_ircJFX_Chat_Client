package java_irc_chat_client;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DCCController {

    @FXML
    private TabPane dccTabPane;

    @FXML
    private Tab tabDCC;

    @FXML
    private Tab tabColaEnvios;

    // Lista para almacenar los controladores de subpestañas
    private final List<Object> subControllers = new ArrayList<>();

    @FXML
    public void initialize() {
        System.out.println("Controlador de DCC inicializado");

        try {
            // Cargar subpestañas con su controlador correspondiente
            cargarSubTab("/java_irc_chat_client/JIRCHAT_SETUP_TAB_TAB_DCC_DCC.fxml", tabDCC);
            cargarSubTab("/java_irc_chat_client/JIRCHAT_SETUP_TAB_TAB_COLADEENVIOS.fxml", tabColaEnvios);

            // Cargar la configuración de todas las subpestañas
            cargarTodasSubpestanas();

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Listeners opcionales para debug
        tabDCC.setOnSelectionChanged(event -> {
            if (tabDCC.isSelected()) System.out.println("Subpestaña DCC seleccionada");
        });

        tabColaEnvios.setOnSelectionChanged(event -> {
            if (tabColaEnvios.isSelected()) System.out.println("Subpestaña Cola de Envios seleccionada");
        });
    }

    /**
     * Carga un FXML dentro de un Tab y guarda su controlador en la lista
     */
    private void cargarSubTab(String fxmlPath, Tab tab) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        Parent root = loader.load();
        Object controller = loader.getController();
        root.setUserData(controller);
        tab.setContent(root);
        subControllers.add(controller);
    }

    /**
     * Guarda la configuración de todas las subpestañas
     */
    public void guardarTodasSubpestanas() {
        for (Object controller : subControllers) {
            if (controller == null) continue;
            try {
                controller.getClass().getMethod("guardarConfiguracion").invoke(controller);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Carga la configuración de todas las subpestañas
     */
    public void cargarTodasSubpestanas() {
        for (Object controller : subControllers) {
            if (controller == null) continue;
            try {
                controller.getClass().getMethod("loadConfig").invoke(controller);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

