package java_irc_chat_client;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import java_irc_chat_client.formularios_persistencia.FormularioMediaConfig;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.File;

public class MediaController {

    @FXML private CheckBox sonidoLocalCheckBox;
    @FXML private ComboBox<String> eventoComboBox;
    @FXML private TextField sonidoTextField;
    @FXML private TextField audioTextField;
    @FXML private TextField videoTextField;

    @FXML private Button btnSeleccionarSonido;
    @FXML private Button btnReproducirSonido;
    @FXML private Button btnAudioDir;
    @FXML private Button btnVideoDir;
    @FXML private Button btnImportarTema;
    @FXML private Button btnExportarTema;

    private final File configFile = new File(System.getProperty("user.home"), "formulario_setup_media.xml");

    @FXML
    private void initialize() {
        System.out.println("MediaController inicializado.");

        // Valores de ejemplo para el ComboBox
        eventoComboBox.getItems().addAll("Conexión", "Mensaje", "Error");

        // Cargar configuración
        loadConfig();
    }

    public void loadConfig() {
        try {
            if (configFile.exists()) {
                JAXBContext context = JAXBContext.newInstance(FormularioMediaConfig.class);
                Unmarshaller um = context.createUnmarshaller();
                FormularioMediaConfig config = (FormularioMediaConfig) um.unmarshal(configFile);

                sonidoLocalCheckBox.setSelected(config.isSonidoLocal());
                if (config.getEventoSeleccionado() != null) {
                    eventoComboBox.setValue(config.getEventoSeleccionado());
                }
                sonidoTextField.setText(safeString(config.getSonidoPath()));
                audioTextField.setText(safeString(config.getAudioDir()));
                videoTextField.setText(safeString(config.getVideoDir()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void guardarConfiguracion() {
        try {
            FormularioMediaConfig config = new FormularioMediaConfig();
            config.setSonidoLocal(sonidoLocalCheckBox.isSelected());
            config.setEventoSeleccionado(eventoComboBox.getValue());
            config.setSonidoPath(safeString(sonidoTextField.getText()));
            config.setAudioDir(safeString(audioTextField.getText()));
            config.setVideoDir(safeString(videoTextField.getText()));

            JAXBContext context = JAXBContext.newInstance(FormularioMediaConfig.class);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            m.marshal(config, configFile);

            System.out.println("Configuración guardada en: " + configFile.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Devuelve cadena segura: si es null, devuelve cadena vacía
     */
    private String safeString(String text) {
        return text != null ? text : "";
    }
}
