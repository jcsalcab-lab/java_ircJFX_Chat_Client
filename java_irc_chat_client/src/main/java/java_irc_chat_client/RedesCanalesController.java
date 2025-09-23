package java_irc_chat_client;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;

public class RedesCanalesController {

    // Combo de selección de redes
    @FXML
    private ComboBox<String> comboRedes;

    // CheckBox Auto-fundador
    @FXML
    private CheckBox chkAutoFundador;

    // ListView de canales
    @FXML
    private ListView<String> listViewCanales;

    // Botones
    @FXML
    private Button btnAgregar;

    @FXML
    private Button btnQuitar;

    @FXML
    private Button btnUtilizar;

    @FXML
    private Button btnInfo;

    // Inicialización automática
    @FXML
    public void initialize() {
        // Aquí puedes cargar la lista de redes, inicializar componentes, etc.
    }

    // Eventos de los botones
    @FXML
    private void onAgregar() {
        // Lógica para agregar un canal
        System.out.println("Agregar canal");
    }

    @FXML
    private void onQuitar() {
        // Lógica para quitar un canal
        System.out.println("Quitar canal");
    }

    @FXML
    private void onUtilizar() {
        // Lógica para utilizar un canal seleccionado
        System.out.println("Utilizar canal");
    }

    @FXML
    private void onInfo() {
        // Lógica para mostrar información de un canal
        System.out.println("Info canal");
    }
}
