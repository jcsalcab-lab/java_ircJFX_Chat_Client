package java_irc_chat_client;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import java_irc_chat_client.formularios_persistencia.FormularioInterfazTemasConfig;

import java.io.File;

public class InterfazTemasController {

    // ListView a la izquierda
    @FXML private ListView<String> listViewTemas;

    // Pestaña Info
    @FXML private Label lblInfo;

    // Pestaña Opciones
    @FXML private CheckBox chkDireccion;
    @FXML private CheckBox chkLusers;
    @FXML private CheckBox chkAlinear;
    @FXML private TextField txtJustifica;

    // Controles inferiores
    @FXML private ComboBox<String> comboEsquema;
    @FXML private CheckBox chkIdiomas;
    @FXML private Button btnCargar;
    @FXML private CheckBox chkColores;
    @FXML private CheckBox chkFuentes;
    @FXML private Button btnDescargar;

    private final File configFile = new File(System.getProperty("user.home"), "formulario_setup_temas.xml");

    @FXML
    public void initialize() {
        // Inicialización de ListView
        listViewTemas.getItems().addAll("Tema 1", "Tema 2", "Tema 3");

        // Inicialización de ComboBox
        comboEsquema.getItems().addAll("Esquema 1", "Esquema 2", "Esquema 3");
        comboEsquema.getSelectionModel().selectFirst();

        // Valores por defecto para TextField y CheckBoxes
        txtJustifica.setText("27");
        lblInfo.setText("info");
        chkDireccion.setSelected(true);
        chkLusers.setSelected(true);
        chkAlinear.setSelected(false);
        chkIdiomas.setSelected(false);
        chkColores.setSelected(true);
        chkFuentes.setSelected(true);

        // Cargar configuración previa
        loadConfig();

        // Actualizar etiqueta info al cambiar selección del tema
        listViewTemas.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) lblInfo.setText("Tema seleccionado: " + newVal);
        });
    }

    // --- Botones ---
    @FXML
    private void onCargarClick() {
        System.out.println("Cargar tema seleccionado: " + listViewTemas.getSelectionModel().getSelectedItem());
    }

    @FXML
    private void onDescargarClick() {
        System.out.println("Descargar nuevos temas...");
    }

    // --- Persistencia XML ---
    public void loadConfig() {
        try {
            if (configFile.exists()) {
                JAXBContext context = JAXBContext.newInstance(FormularioInterfazTemasConfig.class);
                Unmarshaller um = context.createUnmarshaller();
                FormularioInterfazTemasConfig config = (FormularioInterfazTemasConfig) um.unmarshal(configFile);

                chkDireccion.setSelected(config.isDireccion());
                chkLusers.setSelected(config.isLusers());
                chkAlinear.setSelected(config.isAlinear());
                txtJustifica.setText(config.getJustifica());
                comboEsquema.setValue(config.getEsquemaSeleccionado());
                chkIdiomas.setSelected(config.isIdiomas());
                chkColores.setSelected(config.isColores());
                chkFuentes.setSelected(config.isFuentes());

                if (config.getTemaSeleccionado() != null) {
                    listViewTemas.getSelectionModel().select(config.getTemaSeleccionado());
                    lblInfo.setText("Tema seleccionado: " + config.getTemaSeleccionado());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void guardarConfiguracion() {
        try {
            FormularioInterfazTemasConfig config = new FormularioInterfazTemasConfig();
            config.setDireccion(chkDireccion.isSelected());
            config.setLusers(chkLusers.isSelected());
            config.setAlinear(chkAlinear.isSelected());
            config.setJustifica(txtJustifica.getText());
            config.setEsquemaSeleccionado(comboEsquema.getValue());
            config.setIdiomas(chkIdiomas.isSelected());
            config.setColores(chkColores.isSelected());
            config.setFuentes(chkFuentes.isSelected());
            config.setTemaSeleccionado(listViewTemas.getSelectionModel().getSelectedItem());

            JAXBContext context = JAXBContext.newInstance(FormularioInterfazTemasConfig.class);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            m.marshal(config, configFile);

            System.out.println("Configuración de Temas guardada en: " + configFile.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
