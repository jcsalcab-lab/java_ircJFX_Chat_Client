package java_irc_chat_client;

import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

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

    @FXML
    public void initialize() {
        // Inicialización de la pestaña de interfaz
        System.out.println("Controlador de Interfaz inicializado");

        // Ejemplo: agregar contenido o listeners a los tabs
        tabOpciones.setOnSelectionChanged(event -> {
            if (tabOpciones.isSelected()) {
                System.out.println("Se seleccionó la pestaña Opciones");
            }
        });

        tabSkins.setOnSelectionChanged(event -> {
            if (tabSkins.isSelected()) {
                System.out.println("Se seleccionó la pestaña Skins");
            }
        });

        // Puedes hacer lo mismo con las otras pestañas...
    }
}
