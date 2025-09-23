
package java_irc_chat_client;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;

public class IdiomaController {

    @FXML
    private TableView<?> tblIdiomas;

    @FXML
    private TableColumn<?, ?> colPrefIdioma;

    @FXML
    private TableColumn<?, ?> colIdioma;

    @FXML
    private Label lblInfo;

    @FXML
    private CheckBox chkCambiarTema;

    @FXML
    private Button btnDescargarIdiomas;

    @FXML
    private void initialize() {
        // Inicialización si es necesaria
    }

    // Aquí se pueden añadir métodos para manejar eventos de botones o checkbox
}
