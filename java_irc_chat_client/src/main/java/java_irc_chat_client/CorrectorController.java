
package java_irc_chat_client;

import javafx.fxml.FXML;
import javafx.scene.control.*;

public class CorrectorController {

    @FXML
    private CheckBox activoCheckBox;

    @FXML
    private CheckBox mayusculaCheckBox;

    @FXML
    private CheckBox subrayarUrlCheckBox;

    @FXML
    private Button agregarButton;

    @FXML
    private Button eliminarButton;

    @FXML
    private TableView<String> palabrasTableView;

    @FXML
    private TableColumn<String, String> reemplazarColumn;

    @FXML
    private TableColumn<String, String> porColumn;

    @FXML
    public void initialize() {
        // Configuración de tabla
        reemplazarColumn.setCellValueFactory(param -> null); // reemplazar con lógica real
        porColumn.setCellValueFactory(param -> null);

        // Acciones botones
        agregarButton.setOnAction(e -> agregarPalabra());
        eliminarButton.setOnAction(e -> eliminarPalabra());
    }

    private void agregarPalabra() {
        // Lógica para agregar palabra
        System.out.println("Agregar palabra clickeado");
    }

    private void eliminarPalabra() {
        // Lógica para eliminar palabra seleccionada
        System.out.println("Eliminar palabra clickeado");
    }
}
