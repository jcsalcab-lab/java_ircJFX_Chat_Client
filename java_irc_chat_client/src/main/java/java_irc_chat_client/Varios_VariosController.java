package java_irc_chat_client;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import java_irc_chat_client.formularios_persistencia.FormularioVariosVariosConfig;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.File;

public class Varios_VariosController {

    // ===== En #canales =====
    @FXML private CheckBox chkIntentaEntrarBan;
    @FXML private CheckBox chkReentrarSalirTodos;
    @FXML private TextField txtContadorKicks;
    @FXML private CheckBox chkUsarContadorKicks;
    @FXML private TextField txtUsando;
    @FXML private TextField txtLimiteUsuariosIAL;
    @FXML private Button btnConfigurarCanales;

    // ===== Antivirus =====
    @FXML private TextField txtAntivirusPath;
    @FXML private TextField txtSigueNick;
    @FXML private CheckBox chkMostrarConsejo;
    @FXML private Button btnGuardarAntivirus;
    @FXML private Button btnRecuperarAntivirus;
    @FXML private Button btnBrowseAntivirus;

    // ===== Listas de usuarios =====
    @FXML private CheckBox chkAutoOp;
    @FXML private CheckBox chkProtegidos;
    @FXML private CheckBox chkAlObtenerArroba;
    @FXML private CheckBox chkAutoVoz;
    @FXML private CheckBox chkAutoKick;
    @FXML private Button btnListas;

   

    private static final String CONFIG_FILE = "varios_varios_config.xml";

    @FXML
    public void initialize() {
        // Eventos antivirus
        btnGuardarAntivirus.setOnAction(e -> guardarConfiguracion());
        btnRecuperarAntivirus.setOnAction(e -> cargarConfig());
    }

    public void guardarConfiguracion() {
        try {
            FormularioVariosVariosConfig config = new FormularioVariosVariosConfig();

            // Sección canales
            config.setChkIntentaEntrarBan(chkIntentaEntrarBan.isSelected());
            config.setChkReentrarSalirTodos(chkReentrarSalirTodos.isSelected());
            config.setTxtContadorKicks(txtContadorKicks.getText());
            config.setChkUsarContadorKicks(chkUsarContadorKicks.isSelected());
            config.setTxtUsando(txtUsando.getText());
            config.setTxtLimiteUsuariosIAL(txtLimiteUsuariosIAL.getText());

            // Sección antivirus
            config.setTxtAntivirusPath(txtAntivirusPath.getText());
            config.setTxtSigueNick(txtSigueNick.getText());
            config.setChkMostrarConsejo(chkMostrarConsejo.isSelected());

            // Listas de usuarios
            config.setChkAutoOp(chkAutoOp.isSelected());
            config.setChkProtegidos(chkProtegidos.isSelected());
            config.setChkAlObtenerArroba(chkAlObtenerArroba.isSelected());
            config.setChkAutoVoz(chkAutoVoz.isSelected());
            config.setChkAutoKick(chkAutoKick.isSelected());

            JAXBContext context = JAXBContext.newInstance(FormularioVariosVariosConfig.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(config, new File(CONFIG_FILE));

            mostrarAlerta("Guardado exitoso", "La configuración se guardó correctamente.");
        } catch (JAXBException ex) {
            ex.printStackTrace();
            mostrarAlerta("Error", "No se pudo guardar la configuración.");
        }
    }

    private void cargarConfig() {
        try {
            File file = new File(CONFIG_FILE);
            if (!file.exists()) {
                mostrarAlerta("Aviso", "No existe configuración guardada aún.");
                return;
            }

            JAXBContext context = JAXBContext.newInstance(FormularioVariosVariosConfig.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            FormularioVariosVariosConfig config =
                    (FormularioVariosVariosConfig) unmarshaller.unmarshal(file);

            // Canales
            chkIntentaEntrarBan.setSelected(config.isChkIntentaEntrarBan());
            chkReentrarSalirTodos.setSelected(config.isChkReentrarSalirTodos());
            txtContadorKicks.setText(config.getTxtContadorKicks());
            chkUsarContadorKicks.setSelected(config.isChkUsarContadorKicks());
            txtUsando.setText(config.getTxtUsando());
            txtLimiteUsuariosIAL.setText(config.getTxtLimiteUsuariosIAL());

            // Antivirus
            txtAntivirusPath.setText(config.getTxtAntivirusPath());
            txtSigueNick.setText(config.getTxtSigueNick());
            chkMostrarConsejo.setSelected(config.isChkMostrarConsejo());

            // Listas de usuarios
            chkAutoOp.setSelected(config.isChkAutoOp());
            chkProtegidos.setSelected(config.isChkProtegidos());
            chkAlObtenerArroba.setSelected(config.isChkAlObtenerArroba());
            chkAutoVoz.setSelected(config.isChkAutoVoz());
            chkAutoKick.setSelected(config.isChkAutoKick());

            mostrarAlerta("Recuperado exitoso", "La configuración se cargó correctamente.");
        } catch (JAXBException ex) {
            ex.printStackTrace();
            mostrarAlerta("Error", "No se pudo recuperar la configuración.");
        }
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Información");
        alert.setHeaderText(titulo);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
