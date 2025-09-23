package java_irc_chat_client;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class RedesAutoEntrarController {

    @FXML
    private ComboBox<String> comboRedes;

    @FXML
    private CheckBox chkAutoEntrar;

    @FXML
    private TextField txtRetardo;

    @FXML
    private ListView<String> listViewCanales;

    @FXML
    private Button btnAgregar;

    @FXML
    private Button btnQuitar;

    @FXML
    private Button btnEntrarAhora;

    @FXML
    public void initialize() {
        // Inicialización de ComboBox, ListView, etc.
    }

    @FXML
    private void onAgregarClicked() {
        // Lógica para agregar canal a listViewCanales
    }

    @FXML
    private void onQuitarClicked() {
        // Lógica para quitar canal de listViewCanales
    }

    @FXML
    private void onEntrarAhoraClicked() {
        // Lógica para entrar ahora a los canales seleccionados
    }
}
