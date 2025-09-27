package java_irc_chat_client;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import java_irc_chat_client.formularios_persistencia.FormularioAdornosConfig;

import java.io.File;
import java.util.ArrayList;

public class AdornosController {

    @FXML private CheckBox adActivoCheckBox;
    @FXML private TextField adEstiloField;
    @FXML private CheckBox adAdornosCheckBox;
    @FXML private CheckBox adNegritaCheckBox;
    @FXML private CheckBox adSubrayadoCheckBox;
    @FXML private CheckBox adUsarColoresCheckBox;
    @FXML private TextField adFicheroEstilosField;
    @FXML private ListView<String> adAdornosListView;

    private final ObservableList<String> adAdornosData = FXCollections.observableArrayList();
    private final File adornosFile = new File(System.getProperty("user.home"), "adornos_config.xml");

    @FXML
    public void initialize() {
        adAdornosListView.setItems(adAdornosData);
        loadConfig();
    }

    public void loadConfig() {
        try {
            if (adornosFile.exists()) {
                JAXBContext context = JAXBContext.newInstance(FormularioAdornosConfig.class);
                Unmarshaller unmarshaller = context.createUnmarshaller();
                FormularioAdornosConfig config =
                        (FormularioAdornosConfig) unmarshaller.unmarshal(adornosFile);

                adActivoCheckBox.setSelected(config.isActivo());
                adEstiloField.setText(config.getEstilo());
                adAdornosCheckBox.setSelected(config.isAdornos());
                adNegritaCheckBox.setSelected(config.isNegrita());
                adSubrayadoCheckBox.setSelected(config.isSubrayado());
                adUsarColoresCheckBox.setSelected(config.isUsarColores());
                adFicheroEstilosField.setText(config.getFicheroEstilos());
                if (config.getAdornosList() != null) adAdornosData.setAll(config.getAdornosList());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void guardarConfiguracion() {
        try {
            FormularioAdornosConfig config = new FormularioAdornosConfig();
            config.setActivo(adActivoCheckBox.isSelected());
            config.setEstilo(adEstiloField.getText());
            config.setAdornos(adAdornosCheckBox.isSelected());
            config.setNegrita(adNegritaCheckBox.isSelected());
            config.setSubrayado(adSubrayadoCheckBox.isSelected());
            config.setUsarColores(adUsarColoresCheckBox.isSelected());
            config.setFicheroEstilos(adFicheroEstilosField.getText());
            config.setAdornosList(new ArrayList<>(adAdornosData));

            JAXBContext context = JAXBContext.newInstance(FormularioAdornosConfig.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(config, adornosFile);

            System.out.println("Configuración de adornos guardada en: " + adornosFile.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Métodos adicionales para manejar la lista de adornos
    @FXML
    private void agregarAdorno() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Agregar Adorno");
        dialog.setHeaderText("Introduce el nombre del adorno:");
        dialog.showAndWait().ifPresent(adAdornosData::add);
    }

    @FXML
    private void quitarAdorno() {
        String selected = adAdornosListView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            adAdornosData.remove(selected);
        }
    }
}
