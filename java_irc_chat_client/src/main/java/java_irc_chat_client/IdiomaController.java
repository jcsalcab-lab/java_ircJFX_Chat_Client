package java_irc_chat_client;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import java_irc_chat_client.formularios_persistencia.FormularioIdiomaConfig;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controlador para la pestaña "Idioma".
 * Se encarga de enlazar los campos de la interfaz con la lógica de persistencia.
 */
public class IdiomaController {

    @FXML private TableView<FormularioIdiomaConfig.IdiomaItem> tblIdiomas;
    @FXML private TableColumn<FormularioIdiomaConfig.IdiomaItem, String> colPrefIdioma;
    @FXML private TableColumn<FormularioIdiomaConfig.IdiomaItem, String> colIdioma;
    @FXML private CheckBox chkCambiarTema;
    @FXML private Button btnDescargarIdiomas;
    @FXML private Label lblInfo;

    private static final String CONFIG_FILE = "idioma_config.xml";

    @FXML
    public void initialize() {
        // Inicializar columnas
        colPrefIdioma.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getPrefIdioma()));
        colIdioma.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getIdioma()));

        // Botón guardar automáticamente la configuración
        btnDescargarIdiomas.setOnAction(e -> descargarIdiomas());

        // Recuperar configuración si existe
        recuperarConfiguracion();
    }

    private void descargarIdiomas() {
        // Aquí puedes implementar la lógica para descargar idiomas
        mostrarAlerta("Info", "Función de descarga aún no implementada.");
    }

    /**
     * Guarda la configuración actual en el XML.
     */
    public void guardarConfiguracion() {
        try {
            FormularioIdiomaConfig config = new FormularioIdiomaConfig();

            // Guardar lista de idiomas
            ObservableList<FormularioIdiomaConfig.IdiomaItem> items = tblIdiomas.getItems();
            List<FormularioIdiomaConfig.IdiomaItem> lista = items.stream()
                    .map(i -> new FormularioIdiomaConfig.IdiomaItem(i.getPrefIdioma(), i.getIdioma()))
                    .collect(Collectors.toList());
            config.setIdiomas(lista);

            // Guardar checkbox
            config.setCambiarTema(chkCambiarTema.isSelected());

            JAXBContext context = JAXBContext.newInstance(FormularioIdiomaConfig.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            marshaller.marshal(config, new File(CONFIG_FILE));
            mostrarAlerta("Guardado exitoso", "La configuración se guardó correctamente.");
        } catch (JAXBException ex) {
            ex.printStackTrace();
            mostrarAlerta("Error", "No se pudo guardar la configuración.");
        }
    }

    /**
     * Recupera la configuración desde el XML y la carga en la interfaz.
     */
    private void recuperarConfiguracion() {
        try {
            File file = new File(CONFIG_FILE);
            if (!file.exists()) return;

            JAXBContext context = JAXBContext.newInstance(FormularioIdiomaConfig.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            FormularioIdiomaConfig config = (FormularioIdiomaConfig) unmarshaller.unmarshal(file);

            // Cargar lista de idiomas
            ObservableList<FormularioIdiomaConfig.IdiomaItem> items = FXCollections.observableArrayList(config.getIdiomas());
            tblIdiomas.setItems(items);

            // Cargar checkbox
            chkCambiarTema.setSelected(config.isCambiarTema());
        } catch (JAXBException ex) {
            ex.printStackTrace();
            mostrarAlerta("Error", "No se pudo recuperar la configuración.");
        }
    }

    /**
     * Muestra un cuadro de alerta informativo.
     */
    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Información");
        alert.setHeaderText(titulo);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
