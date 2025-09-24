package java_irc_chat_client;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import java.io.File;

public class RedesCanalesController {

    @FXML
    private ComboBox<String> comboRedes;

    @FXML
    private CheckBox chkAutoFundador;

    @FXML
    private ListView<String> listViewCanales;

    @FXML
    private Button btnAgregar, btnQuitar, btnUtilizar, btnInfo;

    private final String CONFIG_FILE = "redes_canales_config.xml";

    @FXML
    public void initialize() {
        // Inicializa eventos de botones
        btnAgregar.setOnAction(e -> agregarCanal());
        btnQuitar.setOnAction(e -> quitarCanal());
        btnUtilizar.setOnAction(e -> utilizarCanal());
        btnInfo.setOnAction(e -> infoCanal());

        // Cargar configuración al inicio
        cargarConfiguracion();
    }

    // ===== Métodos de botones =====
    private void agregarCanal() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Agregar Canal");
        dialog.setHeaderText("Introduce el nombre del canal");
        dialog.setContentText("Canal:");
        dialog.showAndWait().ifPresent(nombre -> {
            listViewCanales.getItems().add(nombre);
            guardarConfiguracion();
        });
    }

    private void quitarCanal() {
        String seleccionado = listViewCanales.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            listViewCanales.getItems().remove(seleccionado);
            guardarConfiguracion();
        }
    }

    private void utilizarCanal() {
        String seleccionado = listViewCanales.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            System.out.println("Usando canal: " + seleccionado);
        }
    }

    private void infoCanal() {
        String seleccionado = listViewCanales.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Información Canal");
            alert.setHeaderText(seleccionado);
            alert.setContentText("Aquí puedes mostrar información adicional del canal.");
            alert.showAndWait();
        }
    }

    // ===== Persistencia =====
    public void guardarConfiguracion() {
        try {
            FormularioRedesCanalesConfig config = new FormularioRedesCanalesConfig();
            config.setRedSeleccionada(comboRedes.getValue());
            config.setAutoFundador(chkAutoFundador.isSelected());
            config.setCanales(FXCollections.observableArrayList(listViewCanales.getItems()));

            JAXBContext context = JAXBContext.newInstance(FormularioRedesCanalesConfig.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(config, new File(CONFIG_FILE));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void cargarConfiguracion() {
        try {
            File file = new File(CONFIG_FILE);
            if (!file.exists()) return;

            JAXBContext context = JAXBContext.newInstance(FormularioRedesCanalesConfig.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            FormularioRedesCanalesConfig config = (FormularioRedesCanalesConfig) unmarshaller.unmarshal(file);

            comboRedes.setValue(config.getRedSeleccionada());
            chkAutoFundador.setSelected(config.isAutoFundador());
            ObservableList<String> canales = FXCollections.observableArrayList(config.getCanales());
            listViewCanales.setItems(canales);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
