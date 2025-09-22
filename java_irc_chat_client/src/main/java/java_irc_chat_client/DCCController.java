package java_irc_chat_client;

import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class DCCController {

    @FXML
    private TabPane dccTabPane;

    @FXML
    private Tab tabDCC;

    @FXML
    private Tab tabColaEnvios;

    @FXML
    public void initialize() {
        // Inicialización de la pestaña DCC
        System.out.println("Controlador de DCC inicializado");

        // Ejemplo de listener para la pestaña DCC
        tabDCC.setOnSelectionChanged(event -> {
            if (tabDCC.isSelected()) {
                System.out.println("Pestaña DCC seleccionada");
                // Aquí podrías actualizar contenido de la pestaña
            }
        });

        // Ejemplo de listener para la pestaña Cola de Envios
        tabColaEnvios.setOnSelectionChanged(event -> {
            if (tabColaEnvios.isSelected()) {
                System.out.println("Pestaña Cola de Envios seleccionada");
                // Aquí podrías actualizar la cola de envíos
            }
        });
    }
}
