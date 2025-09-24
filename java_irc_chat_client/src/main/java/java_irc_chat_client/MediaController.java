package java_irc_chat_client;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
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
                JAXBContext context = JAXBContext.newInstance(MediaConfig.class);
                Unmarshaller um = context.createUnmarshaller();
                MediaConfig config = (MediaConfig) um.unmarshal(configFile);

                sonidoLocalCheckBox.setSelected(config.isSonidoLocal());
                eventoComboBox.setValue(config.getEventoSeleccionado());
                sonidoTextField.setText(config.getSonidoPath());
                audioTextField.setText(config.getAudioDir());
                videoTextField.setText(config.getVideoDir());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveConfig() {
        try {
            MediaConfig config = new MediaConfig();
            config.setSonidoLocal(sonidoLocalCheckBox.isSelected());
            config.setEventoSeleccionado(eventoComboBox.getValue());
            config.setSonidoPath(sonidoTextField.getText());
            config.setAudioDir(audioTextField.getText());
            config.setVideoDir(videoTextField.getText());

            JAXBContext context = JAXBContext.newInstance(MediaConfig.class);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            m.marshal(config, configFile);

            System.out.println("Configuración guardada en: " + configFile.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
