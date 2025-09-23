package java_irc_chat_client;

import javafx.fxml.FXML;
import javafx.scene.control.*;

public class RedesNicksController {

    @FXML
    private ComboBox<String> comboRedes;

    @FXML
    private CheckBox chkIdentificarNick, chkLiberarOcupado;

    @FXML
    private TextArea txtAreaNicks;

    @FXML
    private ListView<String> listViewNicks;

    @FXML
    private Button btnAgregar, btnQuitar, btnUtilizar, btnInfo;

    @FXML
    private void initialize() {
        // Inicialización opcional
    }

    @FXML
    private void onAgregar() {
        // lógica del botón +
    }

    @FXML
    private void onQuitar() {
        // lógica del botón -
    }

    @FXML
    private void onUtilizar() {
        // lógica del botón Utilizar
    }

    @FXML
    private void onInfo() {
        // lógica del botón Info
    }
}

