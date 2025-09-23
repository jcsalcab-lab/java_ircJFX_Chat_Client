package java_irc_chat_client;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;

public class Varios_VariosController {

    @FXML
    private CheckBox chkIntentaEntrarBan;

    @FXML
    private CheckBox chkReentrarSalirTodos;

    @FXML
    private CheckBox chkUsarContadorKicks;

    @FXML
    private TextField txtContadorKicks;

    @FXML
    private TextField txtUsando;

    @FXML
    private TextField txtLimiteUsuariosIAL;

    @FXML
    private Button btnConfigurarCanales;

    @FXML
    private void initialize() {
        // Inicializaciones si fueran necesarias
    }

    @FXML
    private void onConfigurarCanalesClicked() {
        // Aquí se implementará la lógica del botón
        System.out.println("Configurando canales...");
    }
}

