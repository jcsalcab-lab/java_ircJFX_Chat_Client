package java_irc_chat_client;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import java_irc_chat_client.formularios_persistencia.FormularioVariosConfig;

public class VariosController {

    // ===== Subpestaña VARIOS =====
    @FXML private CheckBox chkIntentaEntrarBan;
    @FXML private CheckBox chkReentrarSalirTodos;
    @FXML private TextField txtContadorKicks;
    @FXML private CheckBox chkUsarContadorKicks;
    @FXML private TextField txtUsando;
    @FXML private TextField txtLimiteUsuariosIAL;

    @FXML private TextField txtAntivirusPath;
    @FXML private TextField txtSigueNick;
    @FXML private CheckBox chkMostrarConsejo;

    @FXML private CheckBox chkAutoOp;
    @FXML private CheckBox chkProtegidos;
    @FXML private CheckBox chkAlObtenerArroba;
    @FXML private CheckBox chkAutoVoz;
    @FXML private CheckBox chkAutoKick;

    // ===== Subpestaña IDIOMA =====
    @FXML private TableView<String> tblIdiomas;
    @FXML private CheckBox chkCambiarTema;

    // ===== Subpestaña COMANDOS PERSONALIZADOS =====
    @FXML private TableView<String> tblComandos;
    @FXML private CheckBox chkActivo;
    @FXML private CheckBox chkOcultar;
    @FXML private TextField txtPrefijo;

    private final File ficheroXML = new File("varios_config.xml");
    private FormularioVariosConfig config;

    @FXML
    public void initialize() {
        cargarConfiguracion();
    }

    public void cargarConfiguracion() {
        try {
            if (!ficheroXML.exists()) {
                config = new FormularioVariosConfig();
                return;
            }

            JAXBContext context = JAXBContext.newInstance(FormularioVariosConfig.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            config = (FormularioVariosConfig) unmarshaller.unmarshal(ficheroXML);

            // ===== Cargar valores en controles =====
            chkIntentaEntrarBan.setSelected(config.isChkIntentaEntrarBan());
            chkReentrarSalirTodos.setSelected(config.isChkReentrarSalirTodos());
            txtContadorKicks.setText(config.getTxtContadorKicks());
            chkUsarContadorKicks.setSelected(config.isChkUsarContadorKicks());
            txtUsando.setText(config.getTxtUsando());
            txtLimiteUsuariosIAL.setText(config.getTxtLimiteUsuariosIAL());

            txtAntivirusPath.setText(config.getTxtAntivirusPath());
            txtSigueNick.setText(config.getTxtSigueNick());
            chkMostrarConsejo.setSelected(config.isChkMostrarConsejo());

            chkAutoOp.setSelected(config.isChkAutoOp());
            chkProtegidos.setSelected(config.isChkProtegidos());
            chkAlObtenerArroba.setSelected(config.isChkAlObtenerArroba());
            chkAutoVoz.setSelected(config.isChkAutoVoz());
            chkAutoKick.setSelected(config.isChkAutoKick());

            chkCambiarTema.setSelected(config.isChkCambiarTema());
            if (config.getIdiomas() != null)
                tblIdiomas.setItems(FXCollections.observableArrayList(config.getIdiomas()));

            chkActivo.setSelected(config.isChkActivo());
            chkOcultar.setSelected(config.isChkOcultar());
            txtPrefijo.setText(config.getTxtPrefijo());
            if (config.getComandos() != null)
                tblComandos.setItems(FXCollections.observableArrayList(config.getComandos()));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void guardarConfiguracion() {
        try {
            // ===== Actualizar modelo desde controles =====
            config.setChkIntentaEntrarBan(chkIntentaEntrarBan.isSelected());
            config.setChkReentrarSalirTodos(chkReentrarSalirTodos.isSelected());
            config.setTxtContadorKicks(txtContadorKicks.getText());
            config.setChkUsarContadorKicks(chkUsarContadorKicks.isSelected());
            config.setTxtUsando(txtUsando.getText());
            config.setTxtLimiteUsuariosIAL(txtLimiteUsuariosIAL.getText());

            config.setTxtAntivirusPath(txtAntivirusPath.getText());
            config.setTxtSigueNick(txtSigueNick.getText());
            config.setChkMostrarConsejo(chkMostrarConsejo.isSelected());

            config.setChkAutoOp(chkAutoOp.isSelected());
            config.setChkProtegidos(chkProtegidos.isSelected());
            config.setChkAlObtenerArroba(chkAlObtenerArroba.isSelected());
            config.setChkAutoVoz(chkAutoVoz.isSelected());
            config.setChkAutoKick(chkAutoKick.isSelected());

            config.setChkCambiarTema(chkCambiarTema.isSelected());
            config.setIdiomas(new ArrayList<>(tblIdiomas.getItems()));

            config.setChkActivo(chkActivo.isSelected());
            config.setChkOcultar(chkOcultar.isSelected());
            config.setTxtPrefijo(txtPrefijo.getText());
            config.setComandos(new ArrayList<>(tblComandos.getItems()));

            // ===== Guardar XML =====
            JAXBContext context = JAXBContext.newInstance(FormularioVariosConfig.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(config, ficheroXML);

            System.out.println("[DEBUG] Configuración de Varios guardada en: " + ficheroXML.getAbsolutePath());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
