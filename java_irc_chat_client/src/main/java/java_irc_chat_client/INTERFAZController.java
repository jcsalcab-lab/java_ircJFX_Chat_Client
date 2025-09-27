package java_irc_chat_client;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.Parent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class INTERFAZController {

    @FXML
    private TabPane interfazTabPane;

    @FXML
    private Tab tabOpciones;
    @FXML
    private Tab tabSkins;
    @FXML
    private Tab tabTemas;
    @FXML
    private Tab tabBarra;
    @FXML
    private Tab tabPanel;
    @FXML
    private Tab tabCanalBar;
    @FXML
    private Tab tabVarios;
    @FXML
    private Tab tabCarteles;

    private final List<Object> subControllers = new ArrayList<>();

    @FXML
    public void initialize() {
        System.out.println("Controlador de Interfaz inicializado");

        try {
            cargarSubTab("/java_irc_chat_client/JIRCHAT_SETUP_INTERFAZ_OPCIONES.fxml", tabOpciones);
            cargarSubTab("/java_irc_chat_client/JIRCHAT_SETUP_INTERFAZ_SKIN.fxml", tabSkins);
            cargarSubTab("/java_irc_chat_client/JIRCHAT_SETUP_INTERFAZ_TEMAS.fxml", tabTemas);
            cargarSubTab("/java_irc_chat_client/JIRCHAT_SETUP_INTERFAZ_BARRA.fxml", tabBarra);
            cargarSubTab("/java_irc_chat_client/JIRCHAT_SETUP_INTERFAZ_PANEL.fxml", tabPanel);
            cargarSubTab("/java_irc_chat_client/JIRCHAT_SETUP_INTERFAZ_CANALBAR.fxml", tabCanalBar);
            cargarSubTab("/java_irc_chat_client/JIRCHAT_SETUP_INTERFAZ_VARIOS.fxml", tabVarios);
            cargarSubTab("/java_irc_chat_client/JIRCHAT_SETUP_INTERFAZ_CARTELES.fxml", tabCarteles);

            cargarTodasSubpestanas();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void cargarSubTab(String fxmlPath, Tab tab) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        Parent root = loader.load();
        Object controller = loader.getController();
        root.setUserData(controller);
        tab.setContent(root);
        subControllers.add(controller);
    }

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
