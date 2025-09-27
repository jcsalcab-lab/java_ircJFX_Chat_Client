package java_irc_chat_client;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import java_irc_chat_client.formularios_persistencia.FormularioInterfazBarraConfig;

import java.io.File;
import java.util.ArrayList;

public class InterfazBarraController {

    @FXML private CheckBox chkActiva;
    @FXML private CheckBox chkLag;
    @FXML private CheckBox chkPlayer;

    @FXML private TableView<FormularioInterfazBarraConfig.Icono> tblIconos;
    @FXML private TableColumn<FormularioInterfazBarraConfig.Icono, Integer> colNumero;
    @FXML private TableColumn<FormularioInterfazBarraConfig.Icono, String> colIcono;
    @FXML private TableColumn<FormularioInterfazBarraConfig.Icono, String> colDescripcion;
    @FXML private TableColumn<FormularioInterfazBarraConfig.Icono, String> colComando;

    @FXML private Button btnExclamacion;
    @FXML private Button btnPredefinidos;

    @FXML private TextField txtDescripcion;
    @FXML private TextField txtComando;

    @FXML private Button btnAgregar;
    @FXML private Button btnEliminar;
    @FXML private Button btnActualizar;

    private final File configFile = new File(System.getProperty("user.home"), "formulario_setup_barra.xml");

    @FXML
    private void initialize() {
        // Inicializar columnas de la tabla
        colNumero.setCellValueFactory(new PropertyValueFactory<>("numero"));
        colIcono.setCellValueFactory(new PropertyValueFactory<>("ico"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        colComando.setCellValueFactory(new PropertyValueFactory<>("comando"));

        // Cargar configuración
        loadConfig();
    }

    // --- Persistencia XML ---
    public void loadConfig() {
        try {
            if (configFile.exists()) {
                JAXBContext context = JAXBContext.newInstance(FormularioInterfazBarraConfig.class);
                Unmarshaller um = context.createUnmarshaller();
                FormularioInterfazBarraConfig config = (FormularioInterfazBarraConfig) um.unmarshal(configFile);

                chkActiva.setSelected(config.isActiva());
                chkLag.setSelected(config.isMostrarLag());
                chkPlayer.setSelected(config.isPlayer());

                tblIconos.getItems().clear();
                if (config.getIconos() != null) {
                    tblIconos.getItems().addAll(config.getIconos());
                }

                txtDescripcion.setText(config.getDescripcion());
                txtComando.setText(config.getComando());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void guardarConfiguracion() {
        try {
            FormularioInterfazBarraConfig config = new FormularioInterfazBarraConfig();
            config.setActiva(chkActiva.isSelected());
            config.setMostrarLag(chkLag.isSelected());
            config.setPlayer(chkPlayer.isSelected());
            config.setIconos(new ArrayList<>(tblIconos.getItems()));
            config.setDescripcion(txtDescripcion.getText());
            config.setComando(txtComando.getText());

            JAXBContext context = JAXBContext.newInstance(FormularioInterfazBarraConfig.class);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            m.marshal(config, configFile);

            System.out.println("Configuración de Barra guardada en: " + configFile.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // --- Botones de acción ---
    @FXML
    private void onAgregarClick() {
        FormularioInterfazBarraConfig.Icono icono = new FormularioInterfazBarraConfig.Icono();
        icono.setNumero(tblIconos.getItems().size() + 1);
        icono.setDescripcion(txtDescripcion.getText());
        icono.setComando(txtComando.getText());
        icono.setIco("Ico"); // Valor temporal
        tblIconos.getItems().add(icono);
    }

    @FXML
    private void onEliminarClick() {
        FormularioInterfazBarraConfig.Icono selected = tblIconos.getSelectionModel().getSelectedItem();
        if (selected != null) tblIconos.getItems().remove(selected);
    }

    @FXML
    private void onActualizarClick() {
        FormularioInterfazBarraConfig.Icono selected = tblIconos.getSelectionModel().getSelectedItem();
        if (selected != null) {
            selected.setDescripcion(txtDescripcion.getText());
            selected.setComando(txtComando.getText());
            tblIconos.refresh();
        }
    }
}
