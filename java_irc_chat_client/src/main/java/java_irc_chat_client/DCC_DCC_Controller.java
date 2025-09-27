package java_irc_chat_client;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import java_irc_chat_client.formularios_persistencia.FormularioDCCConfig;

import java.io.File;
import java.util.ArrayList;

public class DCC_DCC_Controller {

    // Listas de aceptación/rechazo/no enviar
    @FXML private ListView<String> listAcepta;
    @FXML private ListView<String> listRechazar;
    @FXML private ListView<String> listNoEnviar;

    @FXML private Button btnAddAcepta;
    @FXML private Button btnRemoveAcepta;
    @FXML private Button btnAddRechazar;
    @FXML private Button btnRemoveRechazar;
    @FXML private Button btnAddNoEnviar;
    @FXML private Button btnRemoveNoEnviar;

    // Auto aceptar
    @FXML private ComboBox<String> cbAutoAceptarChat;
    @FXML private ComboBox<String> cbAutoAceptarEnvios;
    @FXML private CheckBox chkVisorFserve;
    @FXML private CheckBox chkCerrarQueries;
    @FXML private CheckBox chkPedirSiFalta;
    @FXML private CheckBox chkPermiteDCCIP;
    @FXML private CheckBox chkDCCPasivo;
    @FXML private CheckBox chkResponderComandosCola;

    // TextFields velocidad
    @FXML private TextField txtVelocidad;
    @FXML private TextField txtCancelaEnvio;

    private ObservableList<String> aceptaData;
    private ObservableList<String> rechazarData;
    private ObservableList<String> noEnviarData;

    private final File configFile = new File(System.getProperty("user.home"), "formulario_setup_dcc.xml");

    @FXML
    private void initialize() {
        // Inicializar listas
        aceptaData = FXCollections.observableArrayList();
        rechazarData = FXCollections.observableArrayList();
        noEnviarData = FXCollections.observableArrayList();

        listAcepta.setItems(aceptaData);
        listRechazar.setItems(rechazarData);
        listNoEnviar.setItems(noEnviarData);

        // Inicializar Comboboxes con valores por defecto
        cbAutoAceptarChat.getItems().addAll("Todos", "Amigos", "Nadie");
        cbAutoAceptarChat.getSelectionModel().selectFirst();

        cbAutoAceptarEnvios.getItems().addAll("Todos", "Amigos", "Nadie");
        cbAutoAceptarEnvios.getSelectionModel().selectFirst();

        // Inicializar TextFields
        txtVelocidad.setText("0");
        txtCancelaEnvio.setText("0");

        // Botones de lista
        btnAddAcepta.setOnAction(e -> agregarItem(aceptaData, listAcepta));
        btnRemoveAcepta.setOnAction(e -> eliminarItem(listAcepta, aceptaData));
        btnAddRechazar.setOnAction(e -> agregarItem(rechazarData, listRechazar));
        btnRemoveRechazar.setOnAction(e -> eliminarItem(listRechazar, rechazarData));
        btnAddNoEnviar.setOnAction(e -> agregarItem(noEnviarData, listNoEnviar));
        btnRemoveNoEnviar.setOnAction(e -> eliminarItem(listNoEnviar, noEnviarData));

        // Cargar configuración existente
        loadConfig();
    }

    // --- Métodos auxiliares para listas ---
    private void agregarItem(ObservableList<String> data, ListView<String> listView) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Añadir elemento");
        dialog.setHeaderText("Añadir elemento a la lista");
        dialog.setContentText("Valor:");
        dialog.showAndWait().ifPresent(value -> data.add(value));
    }

    private void eliminarItem(ListView<String> listView, ObservableList<String> data) {
        String selected = listView.getSelectionModel().getSelectedItem();
        if (selected != null) data.remove(selected);
    }

    // --- Persistencia XML ---
    public void loadConfig() {
        try {
            if (configFile.exists()) {
                JAXBContext context = JAXBContext.newInstance(FormularioDCCConfig.class);
                Unmarshaller um = context.createUnmarshaller();
                FormularioDCCConfig config = (FormularioDCCConfig) um.unmarshal(configFile);

                // Listas
                aceptaData.clear();
                if (config.getAcepta() != null) aceptaData.addAll(config.getAcepta());

                rechazarData.clear();
                if (config.getRechazar() != null) rechazarData.addAll(config.getRechazar());

                noEnviarData.clear();
                if (config.getNoEnviar() != null) noEnviarData.addAll(config.getNoEnviar());

                // Auto aceptar
                if (config.getAutoAceptarChat() != null)
                    cbAutoAceptarChat.getSelectionModel().select(config.getAutoAceptarChat());

                if (config.getAutoAceptarEnvios() != null)
                    cbAutoAceptarEnvios.getSelectionModel().select(config.getAutoAceptarEnvios());

                chkVisorFserve.setSelected(config.isVisorFserve());
                chkCerrarQueries.setSelected(config.isCerrarQueries());
                chkPedirSiFalta.setSelected(config.isPedirSiFalta());
                chkPermiteDCCIP.setSelected(config.isPermiteDCCIP());
                chkDCCPasivo.setSelected(config.isDccPasivo());
                chkResponderComandosCola.setSelected(config.isResponderComandosCola());

                txtVelocidad.setText(String.valueOf(config.getVelocidadMax()));
                txtCancelaEnvio.setText(String.valueOf(config.getCancelaEnvioDebajo()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void guardarConfiguracion() {
        try {
            FormularioDCCConfig config = new FormularioDCCConfig();
            config.setAcepta(new ArrayList<>(aceptaData));
            config.setRechazar(new ArrayList<>(rechazarData));
            config.setNoEnviar(new ArrayList<>(noEnviarData));

            config.setAutoAceptarChat(cbAutoAceptarChat.getSelectionModel().getSelectedItem());
            config.setAutoAceptarEnvios(cbAutoAceptarEnvios.getSelectionModel().getSelectedItem());

            config.setVisorFserve(chkVisorFserve.isSelected());
            config.setCerrarQueries(chkCerrarQueries.isSelected());
            config.setPedirSiFalta(chkPedirSiFalta.isSelected());
            config.setPermiteDCCIP(chkPermiteDCCIP.isSelected());
            config.setDccPasivo(chkDCCPasivo.isSelected());
            config.setResponderComandosCola(chkResponderComandosCola.isSelected());

            config.setVelocidadMax(parseInt(txtVelocidad.getText()));
            config.setCancelaEnvioDebajo(parseInt(txtCancelaEnvio.getText()));

            JAXBContext context = JAXBContext.newInstance(FormularioDCCConfig.class);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            m.marshal(config, configFile);

            System.out.println("Configuración DCC guardada en: " + configFile.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int parseInt(String value) {
        try { return Integer.parseInt(value); } catch (NumberFormatException e) { return 0; }
    }
}
