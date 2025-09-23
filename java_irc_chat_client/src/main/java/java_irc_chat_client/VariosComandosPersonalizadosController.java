package java_irc_chat_client;

import javafx.fxml.FXML;
import javafx.scene.control.*;

public class VariosComandosPersonalizadosController {

    @FXML
    private TableView<?> tblComandos;

    @FXML
    private TableColumn<?, ?> colComando;

    @FXML
    private TableColumn<?, ?> colDescripcion;

    @FXML
    private CheckBox chkActivo;

    @FXML
    private CheckBox chkOcultar;

    @FXML
    private TextField txtPrefijo;

    @FXML
    private Button btnAgregar;

    @FXML
    private Button btnEliminar;

    @FXML
    private Button btnEditar;

    @FXML
    public void initialize() {
        // Inicialización de la tabla, si se desea
        // colComando.setCellValueFactory(...);
        // colDescripcion.setCellValueFactory(...);
    }
}
