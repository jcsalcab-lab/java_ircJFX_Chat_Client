package java_irc_chat_client;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import java_irc_chat_client.formularios_persistencia.FormularioAdornosConfig;

import java.io.File;
import java.util.ArrayList;

public class AdornosController {

    @FXML private CheckBox activoCheckBox;
    @FXML private TextField estiloField;
    @FXML private CheckBox adornosCheckBox;
    @FXML private TextField textField1;
    @FXML private TextField textField2;
    @FXML private CheckBox negritaCheckBox;
    @FXML private CheckBox subrayadoCheckBox;
    @FXML private CheckBox usarColoresCheckBox;
    @FXML private TextField ficheroEstilosField;

    @FXML private Button estilosTextoButton;
    @FXML private Button estilosColorButton;
    @FXML private Button ojoButton;
    @FXML private Button botonExclamacion;
    @FXML private Button masEstilosButton;

    @FXML private ListView<String> adornosListView;
    @FXML private Button agregarButton;
    @FXML private Button eliminarButton;

    private final File configFile = new File("adornos_config.xml");

    private ObservableList<String> adornosList = FXCollections.observableArrayList();

    // ---------------- Inicialización ----------------
    @FXML
    public void initialize() {
        adornosListView.setItems(adornosList);

        agregarButton.setOnAction(e -> agregarAdorno());
        eliminarButton.setOnAction(e -> eliminarAdorno());

        // Cargar configuración persistente
        FormularioAdornosConfig config = cargarConfig();
        if (config != null) {
            aplicarConfigAlFormulario(config);
        }
    }

    // ---------------- Cargar configuración ----------------
    private FormularioAdornosConfig cargarConfig() {
        try {
            if (configFile.exists()) {
                JAXBContext context = JAXBContext.newInstance(FormularioAdornosConfig.class);
                Unmarshaller unmarshaller = context.createUnmarshaller();
                return (FormularioAdornosConfig) unmarshaller.unmarshal(configFile);
            }
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return null;
    }

    // ---------------- Aplicar configuración al formulario ----------------
    private void aplicarConfigAlFormulario(FormularioAdornosConfig config) {
        activoCheckBox.setSelected(config.activo);
        estiloField.setText(config.estilo);
        adornosCheckBox.setSelected(config.adornos);
        textField1.setText(config.textField1);
        textField2.setText(config.textField2);
        negritaCheckBox.setSelected(config.negrita);
        subrayadoCheckBox.setSelected(config.subrayado);
        usarColoresCheckBox.setSelected(config.usarColores);
        ficheroEstilosField.setText(config.ficheroEstilos);
        if (config.listaAdornos != null) {
            adornosList.setAll(config.listaAdornos);
        }
    }

    // ---------------- Guardar configuración ----------------
    public void guardarConfig() {
        try {
            FormularioAdornosConfig config = new FormularioAdornosConfig();
            config.activo = activoCheckBox.isSelected();
            config.estilo = estiloField.getText();
            config.adornos = adornosCheckBox.isSelected();
            config.textField1 = textField1.getText();
            config.textField2 = textField2.getText();
            config.negrita = negritaCheckBox.isSelected();
            config.subrayado = subrayadoCheckBox.isSelected();
            config.usarColores = usarColoresCheckBox.isSelected();
            config.ficheroEstilos = ficheroEstilosField.getText();
            config.listaAdornos = new ArrayList<>(adornosList);

            JAXBContext context = JAXBContext.newInstance(FormularioAdornosConfig.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(config, configFile);

            System.out.println("Configuración de Adornos guardada en: " + configFile.getAbsolutePath());
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    // ---------------- Acciones botones ListView ----------------
    private void agregarAdorno() {
        adornosList.add("");
    }

    private void eliminarAdorno() {
        String seleccionado = adornosListView.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            adornosList.remove(seleccionado);
        }
    }

    @FXML
    private void guardarEstilo() {
        guardarConfig();
    }
}
