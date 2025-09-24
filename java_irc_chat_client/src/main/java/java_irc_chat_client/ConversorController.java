package java_irc_chat_client;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import java_irc_chat_client.formularios_persistencia.FormularioConversorConfig;

import java.io.File;

public class ConversorController {

    @FXML private CheckBox activoCheckBox;
    @FXML private Label descripcionLabel;
    @FXML private TextField cantidadMedidaField;
    @FXML private Label resultadoLabel;
    @FXML private TextField euroDolaresField;
    @FXML private Button ayudaTagsButton;

    private final File configFile = new File("conversor_config.xml");

    // ---------------- Inicialización ----------------
    @FXML
    public void initialize() {
        // Asociar botón de ayuda (ejemplo)
        ayudaTagsButton.setOnAction(e -> mostrarAyuda());

        // Cargar configuración si existe
        FormularioConversorConfig config = cargarConfig();
        if (config != null) {
            aplicarConfigAlFormulario(config);
        }
    }

    // ---------------- Cargar configuración ----------------
    private FormularioConversorConfig cargarConfig() {
        try {
            if (configFile.exists()) {
                JAXBContext context = JAXBContext.newInstance(FormularioConversorConfig.class);
                Unmarshaller unmarshaller = context.createUnmarshaller();
                return (FormularioConversorConfig) unmarshaller.unmarshal(configFile);
            }
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return null;
    }

    // ---------------- Aplicar configuración al formulario ----------------
    private void aplicarConfigAlFormulario(FormularioConversorConfig config) {
        activoCheckBox.setSelected(config.activo);
        cantidadMedidaField.setText(config.cantidadMedida);
        resultadoLabel.setText(config.resultado);
        euroDolaresField.setText(config.euroDolares);
    }

    // ---------------- Guardar configuración ----------------
    public void guardarConfig() {
        try {
            FormularioConversorConfig config = new FormularioConversorConfig();
            config.activo = activoCheckBox.isSelected();
            config.cantidadMedida = cantidadMedidaField.getText();
            config.resultado = resultadoLabel.getText();
            config.euroDolares = euroDolaresField.getText();

            JAXBContext context = JAXBContext.newInstance(FormularioConversorConfig.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(config, configFile);

            System.out.println("Configuración del Conversor guardada en: " + configFile.getAbsolutePath());
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    // ---------------- Ejemplo de acción de ayuda ----------------
    private void mostrarAyuda() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Ayuda Conversor");
        alert.setHeaderText("Formato de entrada del conversor");
        alert.setContentText("Formato: NN&CODIGO, donde NN es la cantidad y CODIGO la unidad.\nEjemplo: 10$USD");
        alert.showAndWait();
    }

    // Puedes llamar a este método desde un botón de guardar
    @FXML
    private void guardarEstilo() {
        guardarConfig();
    }
}
