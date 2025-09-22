package java_irc_chat_client;

import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class ControllerVarios {

    @FXML
    private TabPane variosTabPane;

    @FXML
    private Tab tabVarios;

    @FXML
    private Tab tabIdioma;

    @FXML
    private Tab tabComandosPersonalizados;

    @FXML
    public void initialize() {
        System.out.println("Controlador de Varios inicializado");

        tabVarios.setOnSelectionChanged(event -> {
            if (tabVarios.isSelected()) System.out.println("Subpestaña Varios seleccionada");
        });
        tabIdioma.setOnSelectionChanged(event -> {
            if (tabIdioma.isSelected()) System.out.println("Subpestaña Idioma seleccionada");
        });
        tabComandosPersonalizados.setOnSelectionChanged(event -> {
            if (tabComandosPersonalizados.isSelected()) System.out.println("Subpestaña Comandos personalizados seleccionada");
        });
    }
}
