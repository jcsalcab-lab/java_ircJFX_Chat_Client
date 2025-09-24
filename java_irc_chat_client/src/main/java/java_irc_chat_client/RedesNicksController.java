package java_irc_chat_client;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import java_irc_chat_client.formularios_persistencia.FormularioRedesNicksConfig;

import java.io.File;
import java.util.ArrayList;

public class RedesNicksController {

    @FXML private ComboBox<String> comboRedes;
    @FXML private CheckBox chkIdentificarNick;
    @FXML private CheckBox chkLiberarOcupado;
    @FXML private ListView<String> listViewNicks;
    @FXML private Button btnAgregar;
    @FXML private Button btnQuitar;
    @FXML private Button btnUtilizar;
    @FXML private Button btnInfo;

    private final File configFile = new File("redes_nicks_config.xml");
    private final ObservableList<String> redesList = FXCollections.observableArrayList();
    private final ObservableList<String> nicksList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        comboRedes.setItems(redesList);
        listViewNicks.setItems(nicksList);

        // Botones
        btnAgregar.setOnAction(e -> agregarNick());
        btnQuitar.setOnAction(e -> quitarNick());
        btnUtilizar.setOnAction(e -> utilizarNick());
        btnInfo.setOnAction(e -> mostrarInfo());

        // Cargar configuración al iniciar
        FormularioRedesNicksConfig config = cargarConfig();
        if (config != null) {
            aplicarConfigAlFormulario(config);
        }
    }

    // ---------------- Agregar Nick ----------------
    private void agregarNick() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Agregar Nick");
        dialog.setHeaderText("Introduce un nuevo nick");
        dialog.setContentText("Nick:");
        dialog.showAndWait().ifPresent(nick -> nicksList.add(nick));
    }

    // ---------------- Quitar Nick ----------------
    private void quitarNick() {
        String seleccionado = listViewNicks.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            nicksList.remove(seleccionado);
        }
    }

    // ---------------- Utilizar Nick ----------------
    private void utilizarNick() {
        String seleccionado = listViewNicks.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            System.out.println("Utilizando nick: " + seleccionado);
        }
    }

    // ---------------- Mostrar Info ----------------
    private void mostrarInfo() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Info Nicks");
        alert.setHeaderText("Información de Nicks");
        alert.setContentText("Aquí se gestionan los nicks de la red seleccionada.");
        alert.showAndWait();
    }

    // ---------------- Guardar configuración ----------------
    public void guardarConfig() {
        try {
            FormularioRedesNicksConfig config = new FormularioRedesNicksConfig();
            config.listaRedes = redesList;
            config.redSeleccionada = comboRedes.getSelectionModel().getSelectedItem();
            config.identificarNick = chkIdentificarNick.isSelected();
            config.liberarOcupado = chkLiberarOcupado.isSelected();
            config.listaNicks = new ArrayList<>(nicksList);

            JAXBContext context = JAXBContext.newInstance(FormularioRedesNicksConfig.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(config, configFile);

            System.out.println("Configuración de Nicks guardada en: " + configFile.getAbsolutePath());
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    // ---------------- Cargar configuración ----------------
    private FormularioRedesNicksConfig cargarConfig() {
        try {
            if (configFile.exists()) {
                JAXBContext context = JAXBContext.newInstance(FormularioRedesNicksConfig.class);
                Unmarshaller unmarshaller = context.createUnmarshaller();
                return (FormularioRedesNicksConfig) unmarshaller.unmarshal(configFile);
            }
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return null;
    }

    // ---------------- Aplicar configuración al formulario ----------------
    private void aplicarConfigAlFormulario(FormularioRedesNicksConfig config) {
        redesList.setAll(config.listaRedes);
        comboRedes.getSelectionModel().select(config.redSeleccionada);
        chkIdentificarNick.setSelected(config.identificarNick);
        chkLiberarOcupado.setSelected(config.liberarOcupado);
        nicksList.setAll(config.listaNicks);
    }
}
