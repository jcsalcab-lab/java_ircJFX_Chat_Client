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
    @FXML private TextField primerSeparadorField;
    @FXML private TextField segundoSeparadorField;
    @FXML private ListView<String> nickListView;

    private final File configFile = new File(System.getProperty("user.home"), "nick_completion_config.xml");

    // ObservableList para la ListView
    private final javafx.collections.ObservableList<String> nickListData = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        nickListView.setItems(nickListData);
        loadConfig();
    }

    public void loadConfig() {
        try {
            if (configFile.exists()) {
                JAXBContext context = JAXBContext.newInstance(FormularioNickCompletionConfig.class);
                Unmarshaller unmarshaller = context.createUnmarshaller();
                FormularioNickCompletionConfig config =
                        (FormularioNickCompletionConfig) unmarshaller.unmarshal(configFile);

                activoCheckBox.setSelected(config.isActivo());
                primerSeparadorField.setText(config.getPrimerSeparador());
                segundoSeparadorField.setText(config.getSegundoSeparador());
                if (config.getNickList() != null) nickListData.setAll(config.getNickList());
            }
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    public void guardarConfiguracion() {
        try {
            FormularioNickCompletionConfig config = new FormularioNickCompletionConfig();
            config.setActivo(activoCheckBox.isSelected());
            config.setPrimerSeparador(primerSeparadorField.getText());
            config.setSegundoSeparador(segundoSeparadorField.getText());
            config.setNickList(new java.util.ArrayList<>(nickListData));

            JAXBContext context = JAXBContext.newInstance(FormularioNickCompletionConfig.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(config, configFile);

        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }
}
