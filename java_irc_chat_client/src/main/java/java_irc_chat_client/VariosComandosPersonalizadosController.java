package java_irc_chat_client;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controlador para la pestaña de Comandos Personalizados.
 */
public class VariosComandosPersonalizadosController {

    @FXML private TableView<FormularioComandosPersonalizadosConfig.ComandoItem> tblComandos;
    @FXML private TableColumn<FormularioComandosPersonalizadosConfig.ComandoItem, String> colComando;
    @FXML private TableColumn<FormularioComandosPersonalizadosConfig.ComandoItem, String> colDescripcion;
    @FXML private CheckBox chkActivo;
    @FXML private CheckBox chkOcultar;
    @FXML private TextField txtPrefijo;
    @FXML private Button btnAgregar;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;

    private static final String CONFIG_FILE = "comandos_personalizados_config.xml";

    @FXML
    public void initialize() {
        // Inicializar columnas
        colComando.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getComando()));
        colDescripcion.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getDescripcion()));

        // Botones de ejemplo (puedes implementar agregar/editar/eliminar)
        btnAgregar.setOnAction(e -> agregarComando());
        btnEliminar.setOnAction(e -> eliminarComando());
        btnEditar.setOnAction(e -> editarComando());

        // Recuperar configuración existente
        recuperarConfiguracion();
    }

    private void agregarComando() {
        // Ejemplo: agregar comando dummy
        FormularioComandosPersonalizadosConfig.ComandoItem item =
                new FormularioComandosPersonalizadosConfig.ComandoItem("nuevoComando", "Descripción");
        tblComandos.getItems().add(item);
        guardarConfiguracion();
    }

    private void eliminarComando() {
        FormularioComandosPersonalizadosConfig.ComandoItem selected = tblComandos.getSelectionModel().getSelectedItem();
        if (selected != null) {
            tblComandos.getItems().remove(selected);
            guardarConfiguracion();
        }
    }

    private void editarComando() {
        FormularioComandosPersonalizadosConfig.ComandoItem selected = tblComandos.getSelectionModel().getSelectedItem();
        if (selected != null) {
            selected.setDescripcion(selected.getDescripcion() + " (editado)");
            tblComandos.refresh();
            guardarConfiguracion();
        }
    }

    public void guardarConfiguracion() {
        try {
            FormularioComandosPersonalizadosConfig config = new FormularioComandosPersonalizadosConfig();
            config.setActivo(chkActivo.isSelected());
            config.setOcultar(chkOcultar.isSelected());
            config.setPrefijo(txtPrefijo.getText());

            ObservableList<FormularioComandosPersonalizadosConfig.ComandoItem> items = tblComandos.getItems();
            List<FormularioComandosPersonalizadosConfig.ComandoItem> lista =
                    items.stream()
                            .map(i -> new FormularioComandosPersonalizadosConfig.ComandoItem(i.getComando(), i.getDescripcion()))
                            .collect(Collectors.toList());
            config.setComandos(lista);

            JAXBContext context = JAXBContext.newInstance(FormularioComandosPersonalizadosConfig.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(config, new File(CONFIG_FILE));
        } catch (JAXBException ex) {
            ex.printStackTrace();
        }
    }

    private void recuperarConfiguracion() {
        try {
            File file = new File(CONFIG_FILE);
            if (!file.exists()) return;

            JAXBContext context = JAXBContext.newInstance(FormularioComandosPersonalizadosConfig.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            FormularioComandosPersonalizadosConfig config =
                    (FormularioComandosPersonalizadosConfig) unmarshaller.unmarshal(file);

            // Cargar tabla
            ObservableList<FormularioComandosPersonalizadosConfig.ComandoItem> items =
                    FXCollections.observableArrayList(config.getComandos());
            tblComandos.setItems(items);

            // Cargar checkbox y prefijo
            chkActivo.setSelected(config.isActivo());
            chkOcultar.setSelected(config.isOcultar());
            txtPrefijo.setText(config.getPrefijo());
        } catch (JAXBException ex) {
            ex.printStackTrace();
        }
    }
}
