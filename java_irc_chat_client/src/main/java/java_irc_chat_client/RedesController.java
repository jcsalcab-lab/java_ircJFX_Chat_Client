package java_irc_chat_client;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import java_irc_chat_client.formularios_persistencia.FormularioRedesConfig;

import java.io.File;

public class RedesController {

    @FXML private ComboBox<String> comboRedes;
    @FXML private Button btnNueva;
    @FXML private Button btnBorrar;

    @FXML private TextField txtNombreRed;
    @FXML private TextField txtFicheroMenus;
    @FXML private Button btnBuscarFichero;
    @FXML private TextField txtApareceComo;

    @FXML private TextField txtMascaraIpBots;
    @FXML private TextField txtNickserv;
    @FXML private TextField txtChanserv;
    @FXML private TextField txtMemoserv;
    @FXML private TextField txtPideId;

    @FXML private CheckBox chkAutoCargarMenus;
    @FXML private CheckBox chkMensajeBotsPrivado;
    @FXML private CheckBox chkSinPerfil;

    @FXML private Button btnAyuda;
    @FXML private Button btnGuarda;

    private final File configFile = new File("redes_config.xml");
    private final ObservableList<String> redesList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        comboRedes.setItems(redesList);

        btnNueva.setOnAction(e -> nuevaRed());
        btnBorrar.setOnAction(e -> borrarRed());
        btnGuarda.setOnAction(e -> guardarConfig());
        btnBuscarFichero.setOnAction(e -> buscarFichero());
        btnAyuda.setOnAction(e -> mostrarAyuda());

        // Cargar configuración al iniciar
        FormularioRedesConfig config = cargarConfig();
        if (config != null) {
            aplicarConfigAlFormulario(config);
        }
    }

    @FXML
    private void onNueva() {
        // Lógica de crear nueva red
        System.out.println("Botón Nueva clickeado");
    }

    @FXML
    private void onBorrar() {
        // Lógica de borrar red
        System.out.println("Botón Borrar clickeado");
    }

    @FXML
    private void onBuscarFichero() {
        // Lógica de buscar fichero
        System.out.println("Botón Buscar Fichero clickeado");
    }

    @FXML
    private void onAyuda() {
        // Lógica de ayuda
        System.out.println("Botón Ayuda clickeado");
    }

    @FXML
    private void onGuarda() {
        // Guardar configuración
        System.out.println("Botón Guarda clickeado");
    }

    
    // ---------------- Crear nueva red ----------------
    private void nuevaRed() {
        String nombre = "RedNueva";
        redesList.add(nombre);
        comboRedes.getSelectionModel().select(nombre);
    }

    // ---------------- Borrar red seleccionada ----------------
    private void borrarRed() {
        String seleccion = comboRedes.getSelectionModel().getSelectedItem();
        if (seleccion != null) {
            redesList.remove(seleccion);
        }
    }

    // ---------------- Guardar configuración ----------------
    public void guardarConfig() {
        try {
            FormularioRedesConfig config = new FormularioRedesConfig();
            config.listaRedes = redesList;
            config.nombreRed = txtNombreRed.getText();
            config.ficheroMenus = txtFicheroMenus.getText();
            config.apareceComo = txtApareceComo.getText();
            config.mascaraIpBots = txtMascaraIpBots.getText();
            config.nickserv = txtNickserv.getText();
            config.chanserv = txtChanserv.getText();
            config.memoserv = txtMemoserv.getText();
            config.pideId = txtPideId.getText();
            config.autoCargarMenus = chkAutoCargarMenus.isSelected();
            config.mensajeBotsPrivado = chkMensajeBotsPrivado.isSelected();
            config.sinPerfil = chkSinPerfil.isSelected();

            JAXBContext context = JAXBContext.newInstance(FormularioRedesConfig.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(config, configFile);

            System.out.println("Configuración de Redes guardada en: " + configFile.getAbsolutePath());
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    // ---------------- Cargar configuración ----------------
    private FormularioRedesConfig cargarConfig() {
        try {
            if (configFile.exists()) {
                JAXBContext context = JAXBContext.newInstance(FormularioRedesConfig.class);
                Unmarshaller unmarshaller = context.createUnmarshaller();
                return (FormularioRedesConfig) unmarshaller.unmarshal(configFile);
            }
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return null;
    }

    // ---------------- Aplicar configuración al formulario ----------------
    private void aplicarConfigAlFormulario(FormularioRedesConfig config) {
        redesList.setAll(config.listaRedes);
        txtNombreRed.setText(config.nombreRed);
        txtFicheroMenus.setText(config.ficheroMenus);
        txtApareceComo.setText(config.apareceComo);
        txtMascaraIpBots.setText(config.mascaraIpBots);
        txtNickserv.setText(config.nickserv);
        txtChanserv.setText(config.chanserv);
        txtMemoserv.setText(config.memoserv);
        txtPideId.setText(config.pideId);
        chkAutoCargarMenus.setSelected(config.autoCargarMenus);
        chkMensajeBotsPrivado.setSelected(config.mensajeBotsPrivado);
        chkSinPerfil.setSelected(config.sinPerfil);
    }

    // ---------------- Acciones de ejemplo ----------------
    private void buscarFichero() {
        // Implementar selector de archivos si se desea
        System.out.println("Buscar fichero clickeado");
    }

    private void mostrarAyuda() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Ayuda Redes");
        alert.setHeaderText("Configuración de redes");
        alert.setContentText("Aquí puede configurar las redes y sus parámetros.");
        alert.showAndWait();
    }
}
