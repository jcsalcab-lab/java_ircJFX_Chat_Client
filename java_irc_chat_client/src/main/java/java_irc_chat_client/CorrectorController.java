
package java_irc_chat_client;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import java_irc_chat_client.formularios_persistencia.FormularioCorrectorConfig;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CorrectorController {

    @FXML private CheckBox activoCheckBox;
    @FXML private CheckBox mayusculaCheckBox;
    @FXML private CheckBox subrayarUrlCheckBox;

    @FXML private Button agregarButton;
    @FXML private Button eliminarButton;

    @FXML private TableView<FormularioCorrectorConfig.Palabra> palabrasTableView;
    @FXML private TableColumn<FormularioCorrectorConfig.Palabra, String> reemplazarColumn;
    @FXML private TableColumn<FormularioCorrectorConfig.Palabra, String> porColumn;

    private final File configFile = new File("corrector_config.xml");

    private ObservableList<FormularioCorrectorConfig.Palabra> palabrasList = FXCollections.observableArrayList();

    // ---------------- Inicialización ----------------
    @FXML
    public void initialize() {
        // Inicializar TableView
        palabrasTableView.setItems(palabrasList);

        reemplazarColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().reemplazar));

        porColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().por));


        // Asociar botones
        agregarButton.setOnAction(e -> agregarPalabra());
        eliminarButton.setOnAction(e -> eliminarPalabra());

        // Cargar configuración
        FormularioCorrectorConfig config = cargarConfig();
        if (config != null) {
            aplicarConfigAlFormulario(config);
        }
    }

    // ---------------- Cargar configuración ----------------
    private FormularioCorrectorConfig cargarConfig() {
        try {
            if (configFile.exists()) {
                JAXBContext context = JAXBContext.newInstance(FormularioCorrectorConfig.class);
                Unmarshaller unmarshaller = context.createUnmarshaller();
                return (FormularioCorrectorConfig) unmarshaller.unmarshal(configFile);
            }
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return null;
    }

    // ---------------- Aplicar configuración al formulario ----------------
    private void aplicarConfigAlFormulario(FormularioCorrectorConfig config) {
        activoCheckBox.setSelected(config.activo);
        mayusculaCheckBox.setSelected(config.mayuscula);
        subrayarUrlCheckBox.setSelected(config.subrayarUrl);
        if (config.palabras != null) {
            palabrasList.setAll(config.palabras);
        }
    }

    // ---------------- Guardar configuración ----------------
    public void guardarConfig() {
        try {
            FormularioCorrectorConfig config = new FormularioCorrectorConfig();
            config.activo = activoCheckBox.isSelected();
            config.mayuscula = mayusculaCheckBox.isSelected();
            config.subrayarUrl = subrayarUrlCheckBox.isSelected();
            config.palabras = new ArrayList<>(palabrasList);

            JAXBContext context = JAXBContext.newInstance(FormularioCorrectorConfig.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(config, configFile);

            System.out.println("Configuración del Corrector guardada en: " + configFile.getAbsolutePath());
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    // ---------------- Acciones de botones ----------------
    private void agregarPalabra() {
        // Ejemplo: agregar palabra vacía para edición
        palabrasList.add(new FormularioCorrectorConfig.Palabra("", ""));
    }

    private void eliminarPalabra() {
        FormularioCorrectorConfig.Palabra seleccionado = palabrasTableView.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            palabrasList.remove(seleccionado);
        }
    }

    // Puedes llamar a guardarConfig() desde algún botón de guardar
    @FXML
    private void guardarEstilo() {
        guardarConfig();
    }
}
