
package java_irc_chat_client;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java_irc_chat_client.formularios_persistencia.FormularioCorrectorConfig;

public class CorrectorController {

    @FXML private CheckBox crActivoCheckBox;
    @FXML private CheckBox crMayusculaCheckBox;
    @FXML private CheckBox crSubrayarUrlCheckBox;

    @FXML private TableView<FormularioCorrectorConfig.Palabra> crPalabrasTableView;
    @FXML private TableColumn<FormularioCorrectorConfig.Palabra, String> crColReemplazar;
    @FXML private TableColumn<FormularioCorrectorConfig.Palabra, String> crColPor;

    @FXML private Button crAgregarButton;
    @FXML private Button crEliminarButton;

    private final File correctorFile = new File(System.getProperty("user.home"), "corrector_config.xml");

    private final ObservableList<FormularioCorrectorConfig.Palabra> palabrasData = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Cargar configuración
        loadCorrectorConfig();

        // Listeners para CheckBoxes
        crActivoCheckBox.selectedProperty().addListener((obs, oldVal, newVal) -> saveCorrectorConfig());
        crMayusculaCheckBox.selectedProperty().addListener((obs, oldVal, newVal) -> saveCorrectorConfig());
        crSubrayarUrlCheckBox.selectedProperty().addListener((obs, oldVal, newVal) -> saveCorrectorConfig());

        // Inicializar TableView
        crPalabrasTableView.setItems(palabrasData);
        crColReemplazar.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getReemplazar()));
        crColPor.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getPor()));

        // Listener para cambios en lista de palabras
        palabrasData.addListener((javafx.collections.ListChangeListener.Change<? extends FormularioCorrectorConfig.Palabra> c) -> saveCorrectorConfig());

        // Botones
        crAgregarButton.setOnAction(e -> agregarPalabra());
        crEliminarButton.setOnAction(e -> eliminarPalabra());
    }

    // ---------------- Cargar configuración ----------------
    public void loadCorrectorConfig() {
        try {
            if (correctorFile.exists()) {
                JAXBContext context = JAXBContext.newInstance(FormularioCorrectorConfig.class);
                Unmarshaller unmarshaller = context.createUnmarshaller();
                FormularioCorrectorConfig config = (FormularioCorrectorConfig) unmarshaller.unmarshal(correctorFile);

                // Aplicar valores a los controles
                crActivoCheckBox.setSelected(config.isActivo());
                crMayusculaCheckBox.setSelected(config.isMayuscula());
                crSubrayarUrlCheckBox.setSelected(config.isSubrayarUrl());

                List<FormularioCorrectorConfig.Palabra> lista = config.getPalabras();
                if (lista != null) {
                    palabrasData.setAll(lista);
                }
            }
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    // ---------------- Guardar configuración ----------------
    public void saveCorrectorConfig() {
        try {
            FormularioCorrectorConfig config = new FormularioCorrectorConfig();
            config.setActivo(crActivoCheckBox.isSelected());
            config.setMayuscula(crMayusculaCheckBox.isSelected());
            config.setSubrayarUrl(crSubrayarUrlCheckBox.isSelected());
            config.setPalabras(new ArrayList<>(palabrasData));

            JAXBContext context = JAXBContext.newInstance(FormularioCorrectorConfig.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(config, correctorFile);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    // ---------------- Agregar palabra ----------------
    private void agregarPalabra() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Agregar palabra");
        dialog.setHeaderText("Agregar reemplazo");
        dialog.setContentText("Formato: palabra|por");
        dialog.showAndWait().ifPresent(input -> {
            String[] partes = input.split("\\|", 2);
            if (partes.length == 2) {
                FormularioCorrectorConfig.Palabra p = new FormularioCorrectorConfig.Palabra(partes[0], partes[1]);
                palabrasData.add(p);
            }
        });
    }

    // ---------------- Eliminar palabra ----------------
    private void eliminarPalabra() {
        FormularioCorrectorConfig.Palabra selected = crPalabrasTableView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            palabrasData.remove(selected);
        }
    }
}
