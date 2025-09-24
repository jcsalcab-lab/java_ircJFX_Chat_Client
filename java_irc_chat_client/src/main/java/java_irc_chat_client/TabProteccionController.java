package java_irc_chat_client;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import java_irc_chat_client.formularios_persistencia.FormularioSetupProteccion;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;

import java.io.File;

public class TabProteccionController {

    @FXML private CheckBox ctcpFloodCheckBox;
    @FXML private CheckBox floodTextoCheckBox;
    @FXML private CheckBox dccFloodCheckBox;
    @FXML private CheckBox queryFloodCheckBox;
    @FXML private CheckBox dosCheckBox;

    @FXML private CheckBox silenceCheckBox;
    @FXML private CheckBox bloqueaTusnamisCheckBox;
    @FXML private CheckBox floodNetCheckBox;

    @FXML private CheckBox activoDeopCheckBox;
    @FXML private CheckBox usarChanservCheckBox;

    @FXML private TextField excluirCtcpTextField;

    private final File configFile = new File(System.getProperty("user.home"), "formulario_setup_proteccion.xml");

    @FXML
    private void initialize() {
        cargarConfiguracion();
    }

    public void cargarConfiguracion() {
        if (configFile.exists()) {
            try {
                JAXBContext context = JAXBContext.newInstance(FormularioSetupProteccion.class);
                Unmarshaller um = context.createUnmarshaller();
                FormularioSetupProteccion data = (FormularioSetupProteccion) um.unmarshal(configFile);

                ctcpFloodCheckBox.setSelected(data.isCtcpFlood());
                floodTextoCheckBox.setSelected(data.isFloodTexto());
                dccFloodCheckBox.setSelected(data.isDccFlood());
                queryFloodCheckBox.setSelected(data.isQueryFlood());
                dosCheckBox.setSelected(data.isDos());

                silenceCheckBox.setSelected(data.isSilence());
                bloqueaTusnamisCheckBox.setSelected(data.isBloqueaTusnamis());
                floodNetCheckBox.setSelected(data.isFloodNet());

                activoDeopCheckBox.setSelected(data.isActivoDeop());
                usarChanservCheckBox.setSelected(data.isUsarChanserv());

                excluirCtcpTextField.setText(data.getExcluirCtcp());

                System.out.println("Configuración de protección cargada desde XML.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void guardarConfiguracion() {
        try {
            FormularioSetupProteccion data = new FormularioSetupProteccion();

            data.setCtcpFlood(ctcpFloodCheckBox.isSelected());
            data.setFloodTexto(floodTextoCheckBox.isSelected());
            data.setDccFlood(dccFloodCheckBox.isSelected());
            data.setQueryFlood(queryFloodCheckBox.isSelected());
            data.setDos(dosCheckBox.isSelected());

            data.setSilence(silenceCheckBox.isSelected());
            data.setBloqueaTusnamis(bloqueaTusnamisCheckBox.isSelected());
            data.setFloodNet(floodNetCheckBox.isSelected());

            data.setActivoDeop(activoDeopCheckBox.isSelected());
            data.setUsarChanserv(usarChanservCheckBox.isSelected());

            data.setExcluirCtcp(excluirCtcpTextField.getText());

            JAXBContext context = JAXBContext.newInstance(FormularioSetupProteccion.class);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            m.marshal(data, configFile);

            System.out.println("Configuración de protección guardada en XML.");
        } catch (Exception e) {
            System.err.println("Error guardando configuración en TabProteccionController");
            e.printStackTrace();
        }
    }
}
