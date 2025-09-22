package java_irc_chat_client;

import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class ControllerRedes {

    @FXML
    private TabPane redesTabPane;

    @FXML
    private Tab tabRedes;

    @FXML
    private Tab tabNicks;

    @FXML
    private Tab tabCanales;

    @FXML
    private Tab tabAutoEntrar;

    @FXML
    public void initialize() {
        System.out.println("Controlador de Redes inicializado");

        tabRedes.setOnSelectionChanged(event -> {
            if (tabRedes.isSelected()) System.out.println("Subpestaña Redes seleccionada");
        });
        tabNicks.setOnSelectionChanged(event -> {
            if (tabNicks.isSelected()) System.out.println("Subpestaña Nicks seleccionada");
        });
        tabCanales.setOnSelectionChanged(event -> {
            if (tabCanales.isSelected()) System.out.println("Subpestaña Canales seleccionada");
        });
        tabAutoEntrar.setOnSelectionChanged(event -> {
            if (tabAutoEntrar.isSelected()) System.out.println("Subpestaña Auto-entrar seleccionada");
        });
    }
}
