package java_irc_chat_client;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import java_irc_chat_client.formularios_persistencia.FormularioNickCompletionConfig;

import java.io.File;

public class NickCompletionController {

    @FXML private CheckBox activoCheckBox;
    @FXML private Label nickLabel;
    @FXML private Button separadorButton;
    @FXML private TextField primerSeparadorField;
    @FXML private Button textoButton;
    @FXML private TextField segundoSeparadorField;
    @FXML private Button guardarEstiloButton;
    @FXML private ListView<String> nickListView;
    @FXML private Button eliminarButton;

    private final File configFile = new File("nick_completion_config.xml");

    // ---------------- Inicialización ----------------
    @FXML
    public void initialize() {
        guardarEstiloButton.setOnAction(e -> guardarEstilo());
        separadorButton.setOnAction(e -> agregarSeparador());
        textoButton.setOnAction(e -> agregarTexto());
        eliminarButton.setOnAction(e -> eliminarElemento());

        // Cargar configuración persistente
        FormularioNickCompletionConfig config = cargarConfig();
        if (config != null) {
            aplicarConfigAlFormulario(config);
        }
    }

    // ---------------- Cargar configuración ----------------
    private FormularioNickCompletionConfig cargarConfig() {
        try {
            if (configFile.exists()) {
                JAXBContext context = JAXBContext.newInstance(FormularioNickCompletionConfig.class);
                Unmarshaller unmarshaller = context.createUnmarshaller();
                return (FormularioNickCompletionConfig) unmarshaller.unmarshal(configFile);
            }
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return null;
    }

    // ---------------- Aplicar configuración al formulario ----------------
    private void aplicarConfigAlFormulario(FormularioNickCompletionConfig config) {
        activoCheckBox.setSelected(config.activo);
        primerSeparadorField.setText(config.primerSeparador);
        segundoSeparadorField.setText(config.segundoSeparador);
        if (config.nickList != null) {
            nickListView.setItems(FXCollections.observableArrayList(config.nickList));
        }
    }

    // ---------------- Guardar configuración ----------------
    public void guardarConfig() {
        try {
            FormularioNickCompletionConfig config = new FormularioNickCompletionConfig();
            config.activo = activoCheckBox.isSelected();
            config.primerSeparador = primerSeparadorField.getText();
            config.segundoSeparador = segundoSeparadorField.getText();
            config.nickList = nickListView.getItems();

            JAXBContext context = JAXBContext.newInstance(FormularioNickCompletionConfig.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(config, configFile);

            System.out.println("Configuración guardada en: " + configFile.getAbsolutePath());
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    // ---------------- Acciones de botones ----------------
    private void guardarEstilo() {
        guardarConfig();
        System.out.println("Botón 'Guardar Estilo' clickeado");
    }

    private void agregarSeparador() {
        System.out.println("Agregar Separador clickeado");
    }

    private void agregarTexto() {
        System.out.println("Agregar Texto clickeado");
    }

    private void eliminarElemento() {
        String seleccionado = nickListView.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            nickListView.getItems().remove(seleccionado);
        }
    }
}

