package java_irc_chat_client;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import java_irc_chat_client.formularios_persistencia.FormularioInterfazOpcionesConfig;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.io.File;

public class InterfazOpcionesController {

    @FXML private ComboBox<String> comboModoVentanas;

    @FXML private CheckBox chkPanelCanalPv;
    @FXML private CheckBox chkAbrirPrivados;
    @FXML private CheckBox chkCargarLog;
    @FXML private CheckBox chkDobleClickPriv;
    @FXML private CheckBox chkHotlinks;
    @FXML private CheckBox chkTooltips;

    @FXML private CheckBox chkWhois;
    @FXML private CheckBox chkNotices;
    @FXML private CheckBox chkNotify;
    @FXML private CheckBox chkCTCP;

    @FXML private CheckBox chkVentanasNoticias;
    @FXML private TextField txtFiltrosNoticias;
    @FXML private Button btnTiposTamano;

    private final File configFile = new File(System.getProperty("user.home"), "formulario_setup_opciones.xml");

    @FXML
    private void initialize() {
        // --- Lógica original ---
        comboModoVentanas.getItems().addAll("Modo 1", "Modo 2", "Modo 3");
        comboModoVentanas.getSelectionModel().selectFirst();

        // --- Cargar configuración desde XML si existe ---
        loadConfig();
    }

    // --- Carga de configuración ---
    public void loadConfig() {
        try {
            if (configFile.exists()) {
                JAXBContext context = JAXBContext.newInstance(FormularioInterfazOpcionesConfig.class);
                Unmarshaller um = context.createUnmarshaller();
                FormularioInterfazOpcionesConfig config = (FormularioInterfazOpcionesConfig) um.unmarshal(configFile);

                comboModoVentanas.setValue(config.getModoVentanas());

                chkPanelCanalPv.setSelected(config.isPanelCanalPv());
                chkAbrirPrivados.setSelected(config.isAbrirPrivados());
                chkCargarLog.setSelected(config.isCargarLog());
                chkDobleClickPriv.setSelected(config.isDobleClickPriv());
                chkHotlinks.setSelected(config.isHotlinks());
                chkTooltips.setSelected(config.isTooltips());

                chkWhois.setSelected(config.isWhois());
                chkNotices.setSelected(config.isNotices());
                chkNotify.setSelected(config.isNotify());
                chkCTCP.setSelected(config.isCtcp());

                chkVentanasNoticias.setSelected(config.isVentanasNoticias());
                txtFiltrosNoticias.setText(config.getFiltrosNoticias());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // --- Guardado de configuración ---
    public void saveConfig() {
        try {
            FormularioInterfazOpcionesConfig config = new FormularioInterfazOpcionesConfig();

            config.setModoVentanas(comboModoVentanas.getValue());

            config.setPanelCanalPv(chkPanelCanalPv.isSelected());
            config.setAbrirPrivados(chkAbrirPrivados.isSelected());
            config.setCargarLog(chkCargarLog.isSelected());
            config.setDobleClickPriv(chkDobleClickPriv.isSelected());
            config.setHotlinks(chkHotlinks.isSelected());
            config.setTooltips(chkTooltips.isSelected());

            config.setWhois(chkWhois.isSelected());
            config.setNotices(chkNotices.isSelected());
            config.setNotify(chkNotify.isSelected());
            config.setCtcp(chkCTCP.isSelected());

            config.setVentanasNoticias(chkVentanasNoticias.isSelected());
            config.setFiltrosNoticias(txtFiltrosNoticias.getText());

            JAXBContext context = JAXBContext.newInstance(FormularioInterfazOpcionesConfig.class);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            m.marshal(config, configFile);

            System.out.println("Configuración guardada en: " + configFile.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
