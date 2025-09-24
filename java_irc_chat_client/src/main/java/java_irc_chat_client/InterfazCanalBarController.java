package java_irc_chat_client;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;

import java.io.File;
import java.util.ArrayList;

public class InterfazCanalBarController {

    @FXML private CheckBox chkActiva;

    @FXML private TableView<ComandoItem> tablePrivados;
    @FXML private TableColumn<ComandoItem, String> colNumPriv;
    @FXML private TableColumn<ComandoItem, String> colComandoPriv;

    @FXML private TableView<ComandoItem> tableCanales;
    @FXML private TableColumn<ComandoItem, String> colNumCanal;
    @FXML private TableColumn<ComandoItem, String> colComandoCanal;

    @FXML private Button btnPredefinidos;
    @FXML private TextField txtDescripcion;
    @FXML private TextField txtComando;
    @FXML private Button btnAgregar;
    @FXML private Button btnEliminar;
    @FXML private Button btnActualizar;

    private ObservableList<ComandoItem> privadosData;
    private ObservableList<ComandoItem> canalesData;

    private final File configFile = new File(System.getProperty("user.home"), "formulario_setup_canalbar.xml");

    @FXML
    public void initialize() {
        // Columnas
        colNumPriv.setCellValueFactory(data -> data.getValue().numeroProperty());
        colComandoPriv.setCellValueFactory(data -> data.getValue().comandoProperty());
        colNumCanal.setCellValueFactory(data -> data.getValue().numeroProperty());
        colComandoCanal.setCellValueFactory(data -> data.getValue().comandoProperty());

        // Listas observables
        privadosData = FXCollections.observableArrayList();
        canalesData = FXCollections.observableArrayList();
        tablePrivados.setItems(privadosData);
        tableCanales.setItems(canalesData);

        // Cargar configuración XML
        loadConfig();

        // Botones
        btnAgregar.setOnAction(e -> agregarComando());
        btnEliminar.setOnAction(e -> eliminarComando());
        btnActualizar.setOnAction(e -> actualizarComando());
        btnPredefinidos.setOnAction(e -> cargarPredefinidos());
    }

    // --- Persistencia ---
    public void loadConfig() {
        try {
            if (configFile.exists()) {
                JAXBContext context = JAXBContext.newInstance(FormularioCanalBarConfig.class);
                Unmarshaller um = context.createUnmarshaller();
                FormularioCanalBarConfig config = (FormularioCanalBarConfig) um.unmarshal(configFile);

                chkActiva.setSelected(config.isActiva());

                privadosData.clear();
                if (config.getPrivados() != null) {
                    for (FormularioCanalBarConfig.ItemComando item : config.getPrivados()) {
                        privadosData.add(new ComandoItem(item.getNumero(), item.getComando()));
                    }
                }

                canalesData.clear();
                if (config.getCanales() != null) {
                    for (FormularioCanalBarConfig.ItemComando item : config.getCanales()) {
                        canalesData.add(new ComandoItem(item.getNumero(), item.getComando()));
                    }
                }

                txtDescripcion.setText(config.getDescripcion());
                txtComando.setText(config.getComando());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveConfig() {
        try {
            FormularioCanalBarConfig config = new FormularioCanalBarConfig();
            config.setActiva(chkActiva.isSelected());

            ArrayList<FormularioCanalBarConfig.ItemComando> privadosList = new ArrayList<>();
            for (ComandoItem item : privadosData) {
                privadosList.add(new FormularioCanalBarConfig.ItemComando(item.getNumero(), item.getComando()));
            }
            config.setPrivados(privadosList);

            ArrayList<FormularioCanalBarConfig.ItemComando> canalesList = new ArrayList<>();
            for (ComandoItem item : canalesData) {
                canalesList.add(new FormularioCanalBarConfig.ItemComando(item.getNumero(), item.getComando()));
            }
            config.setCanales(canalesList);

            config.setDescripcion(txtDescripcion.getText());
            config.setComando(txtComando.getText());

            JAXBContext context = JAXBContext.newInstance(FormularioCanalBarConfig.class);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            m.marshal(config, configFile);

            System.out.println("Configuración CanalBar guardada en: " + configFile.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // --- Botones ---
    private void agregarComando() {
        String desc = txtDescripcion.getText().trim();
        String cmd = txtComando.getText().trim();
        if (!desc.isEmpty() && !cmd.isEmpty()) {
            privadosData.add(new ComandoItem(desc, cmd));
            txtDescripcion.clear();
            txtComando.clear();
        }
    }

    private void eliminarComando() {
        ComandoItem seleccionado = tablePrivados.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            privadosData.remove(seleccionado);
        }
    }

    private void actualizarComando() {
        ComandoItem seleccionado = tablePrivados.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            seleccionado.numeroProperty().set(txtDescripcion.getText());
            seleccionado.comandoProperty().set(txtComando.getText());
            tablePrivados.refresh();
        }
    }

    private void cargarPredefinidos() {
        privadosData.clear();
        privadosData.addAll(
            new ComandoItem("1", "/msg Hola"),
            new ComandoItem("2", "/nick NuevoNick")
        );
    }

    // --- Clase interna ---
    public static class ComandoItem {
        private final SimpleStringProperty numero;
        private final SimpleStringProperty comando;

        public ComandoItem(String numero, String comando) {
            this.numero = new SimpleStringProperty(numero);
            this.comando = new SimpleStringProperty(comando);
        }

        public SimpleStringProperty numeroProperty() { return numero; }
        public SimpleStringProperty comandoProperty() { return comando; }

        public String getNumero() { return numero.get(); }
        public String getComando() { return comando.get(); }
    }
}
