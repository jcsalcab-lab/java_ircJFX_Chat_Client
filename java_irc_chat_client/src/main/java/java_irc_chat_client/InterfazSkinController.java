package java_irc_chat_client;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import java_irc_chat_client.formularios_persistencia.FormularioInterfazSkinConfig;

import java.io.File;

public class InterfazSkinController {

    @FXML private ListView<String> listSkins;
    @FXML private Label lblInfo;
    @FXML private Button btnVistaWeb;
    @FXML private Button btnMasInfo;
    @FXML private Button btnCargar;
    @FXML private Button btnDescargar;

    @FXML private CheckBox chkTemaTexto;
    @FXML private CheckBox chkTemaSonido;
    @FXML private CheckBox chkColores;
    @FXML private CheckBox chkFuentes;
    @FXML private CheckBox chkFondos;

    // Archivo de configuración XML
    private final File configFile = new File(System.getProperty("user.home"), "formulario_setup_skin.xml");

    @FXML
    private void initialize() {
        // Inicialización de lista de skins
        listSkins.getItems().addAll("Skin 1", "Skin 2", "Skin 3");
        listSkins.getSelectionModel().selectFirst();

        // Cargar configuración previa si existe
        loadConfig();

        // Actualizar etiqueta info al seleccionar otra skin
        listSkins.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                lblInfo.setText("Skin seleccionada: " + newVal);
            }
        });
    }

    // --- Cargar configuración desde XML ---
    public void loadConfig() {
        try {
            if (configFile.exists()) {
                JAXBContext context = JAXBContext.newInstance(FormularioInterfazSkinConfig.class);
                Unmarshaller um = context.createUnmarshaller();
                FormularioInterfazSkinConfig config = (FormularioInterfazSkinConfig) um.unmarshal(configFile);

                chkTemaTexto.setSelected(config.isTemaTexto());
                chkTemaSonido.setSelected(config.isTemaSonido());
                chkColores.setSelected(config.isColores());
                chkFuentes.setSelected(config.isFuentes());
                chkFondos.setSelected(config.isFondos());

                if (config.getSkinSeleccionada() != null) {
                    listSkins.getSelectionModel().select(config.getSkinSeleccionada());
                    lblInfo.setText("Skin seleccionada: " + config.getSkinSeleccionada());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // --- Guardar configuración a XML ---
    public void guardarConfiguracion() {
        try {
            FormularioInterfazSkinConfig config = new FormularioInterfazSkinConfig();
            config.setTemaTexto(chkTemaTexto.isSelected());
            config.setTemaSonido(chkTemaSonido.isSelected());
            config.setColores(chkColores.isSelected());
            config.setFuentes(chkFuentes.isSelected());
            config.setFondos(chkFondos.isSelected());
            config.setSkinSeleccionada(listSkins.getSelectionModel().getSelectedItem());

            JAXBContext context = JAXBContext.newInstance(FormularioInterfazSkinConfig.class);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            m.marshal(config, configFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
