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
    @FXML private TextField cantidadMedidaField;
    @FXML private Label resultadoLabel;
    @FXML private TextField euroDolaresField;

    private final File configFile = new File(System.getProperty("user.home"), "conversor_config.xml");

    @FXML
    public void initialize() {
        loadConfig();
    }

    public void loadConfig() {
        try {
            if (configFile.exists()) {
                JAXBContext context = JAXBContext.newInstance(FormularioConversorConfig.class);
                Unmarshaller unmarshaller = context.createUnmarshaller();
                FormularioConversorConfig config =
                        (FormularioConversorConfig) unmarshaller.unmarshal(configFile);

                activoCheckBox.setSelected(config.isActivo());
                cantidadMedidaField.setText(config.getCantidadMedida());
                resultadoLabel.setText(config.getResultado());
                euroDolaresField.setText(config.getEuroDolares());
            }
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    public void guardarConfiguracion() {
        try {
            FormularioConversorConfig config = new FormularioConversorConfig();
            config.setActivo(activoCheckBox.isSelected());
            config.setCantidadMedida(cantidadMedidaField.getText());
            config.setResultado(resultadoLabel.getText());
            config.setEuroDolares(euroDolaresField.getText());

            JAXBContext context = JAXBContext.newInstance(FormularioConversorConfig.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(config, configFile);

        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }
}
