package java_irc_chat_client;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import java_irc_chat_client.formularios_persistencia.FormularioRedesAutoEntrarConfig;

import java.io.File;

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
    private Button btnAgregar, btnQuitar, btnEntrarAhora;

    private final String CONFIG_FILE = "redes_auto_entrar_config.xml";

    @FXML
    public void initialize() {
        btnAgregar.setOnAction(e -> agregarCanal());
        btnQuitar.setOnAction(e -> quitarCanal());
        btnEntrarAhora.setOnAction(e -> entrarAhora());

        cargarConfiguracion();
    }

    // ===== Métodos botones =====
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

    private void entrarAhora() {
        String seleccionado = listViewCanales.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            System.out.println("Entrando ahora al canal: " + seleccionado);
        }
    }

    // ===== Persistencia =====
    public void guardarConfiguracion() {
        try {
            FormularioRedesAutoEntrarConfig config = new FormularioRedesAutoEntrarConfig();
            config.setRedSeleccionada(comboRedes.getValue());
            config.setAutoEntrar(chkAutoEntrar.isSelected());
            config.setRetardo(txtRetardo.getText());
            config.setCanales(FXCollections.observableArrayList(listViewCanales.getItems()));

            JAXBContext context = JAXBContext.newInstance(FormularioRedesAutoEntrarConfig.class);
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

            JAXBContext context = JAXBContext.newInstance(FormularioRedesAutoEntrarConfig.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            FormularioRedesAutoEntrarConfig config = (FormularioRedesAutoEntrarConfig) unmarshaller.unmarshal(file);

            comboRedes.setValue(config.getRedSeleccionada());
            chkAutoEntrar.setSelected(config.isAutoEntrar());
            txtRetardo.setText(config.getRetardo());
            ObservableList<String> canales = FXCollections.observableArrayList(config.getCanales());
            listViewCanales.setItems(canales);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
