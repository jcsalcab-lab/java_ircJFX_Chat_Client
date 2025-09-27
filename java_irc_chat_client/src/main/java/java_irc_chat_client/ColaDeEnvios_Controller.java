package java_irc_chat_client;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import java_irc_chat_client.formularios_persistencia.FormularioColaEnviosConfig;

import java.io.File;

public class ColaDeEnvios_Controller {

    @FXML private TextField txtMaxEnvios;
    @FXML private TextField txtMaxPorUsuario;
    @FXML private TextField txtSlotsCola;
    @FXML private TextField txtSlotsPorUsuario;
    @FXML private TextField txtSaltarCola;

    private final File configFile = new File(System.getProperty("user.home"), "formulario_setup_colaenvios.xml");

    @FXML
    public void initialize() {
        // Valores por defecto
        txtMaxEnvios.setText("4");
        txtMaxPorUsuario.setText("2");
        txtSlotsCola.setText("0");
        txtSlotsPorUsuario.setText("3");
        txtSaltarCola.setText("10");

        // Cargar configuración existente
        loadConfig();
    }

    // --- Persistencia XML ---
    public void loadConfig() {
        try {
            if (configFile.exists()) {
                JAXBContext context = JAXBContext.newInstance(FormularioColaEnviosConfig.class);
                Unmarshaller um = context.createUnmarshaller();
                FormularioColaEnviosConfig config = (FormularioColaEnviosConfig) um.unmarshal(configFile);

                txtMaxEnvios.setText(String.valueOf(config.getMaxEnvios()));
                txtMaxPorUsuario.setText(String.valueOf(config.getMaxPorUsuario()));
                txtSlotsCola.setText(String.valueOf(config.getSlotsCola()));
                txtSlotsPorUsuario.setText(String.valueOf(config.getSlotsPorUsuario()));
                txtSaltarCola.setText(String.valueOf(config.getSaltarCola()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void guardarConfiguracion() {
        try {
            FormularioColaEnviosConfig config = new FormularioColaEnviosConfig();
            config.setMaxEnvios(parseInt(txtMaxEnvios.getText(), 4));
            config.setMaxPorUsuario(parseInt(txtMaxPorUsuario.getText(), 2));
            config.setSlotsCola(parseInt(txtSlotsCola.getText(), 0));
            config.setSlotsPorUsuario(parseInt(txtSlotsPorUsuario.getText(), 3));
            config.setSaltarCola(parseInt(txtSaltarCola.getText(), 10));

            JAXBContext context = JAXBContext.newInstance(FormularioColaEnviosConfig.class);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            m.marshal(config, configFile);

            System.out.println("Configuración Cola de Envios guardada en: " + configFile.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int parseInt(String value, int defaultValue) {
        try { return Integer.parseInt(value); }
        catch (NumberFormatException e) { return defaultValue; }
    }
}
