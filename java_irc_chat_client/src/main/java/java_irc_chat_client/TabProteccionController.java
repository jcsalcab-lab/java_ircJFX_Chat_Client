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
import java.util.ArrayList;
import java.util.List;

public class TabProteccionController {

    // Flood/DOS
    @FXML private CheckBox ctcpFloodCheckBox;
    @FXML private CheckBox floodTextoCheckBox;
    @FXML private CheckBox dccFloodCheckBox;
    @FXML private CheckBox queryFloodCheckBox;
    @FXML private CheckBox ataquesDosCheckBox;

    // Extras
    @FXML private CheckBox reforzarSilenceCheckBox;
    @FXML private CheckBox bloqueaTusnamisCheckBox;
    @FXML private CheckBox floodNetTextoCheckBox;

    // Anti deop/ban/kick
    @FXML private CheckBox antiDeopActivoCheckBox;
    @FXML private CheckBox usarChanservCheckBox;

    // Excluir protecciones
    @FXML private TextField excluirCtcpTextField;

    // Anti-spam mensajes privados
    @FXML private CheckBox antiSpamAlAbrirCheckBox;
    @FXML private CheckBox antiSpamSiempreCheckBox;
    @FXML private ListView<String> privateSpamListView;
    @FXML private CheckBox logSpamCheckBox;

    // Anti-spam canales
    @FXML private CheckBox antiSpamCanalesActivoCheckBox;
    @FXML private CheckBox anularSpamQuitsCheckBox;

    private static final String CONFIG_FILE = "proteccion_config.xml";

    @FXML
    public void initialize() {
        // Inicialización de la ListView
        privateSpamListView.setItems(FXCollections.observableArrayList());
    }

    // ===== Guardar configuración =====
    public void guardarConfiguracion() {
        try {
            FormularioProteccionConfig config = new FormularioProteccionConfig();

            // Flood/DOS
            config.setCtcpFlood(ctcpFloodCheckBox.isSelected());
            config.setFloodTexto(floodTextoCheckBox.isSelected());
            config.setDccFlood(dccFloodCheckBox.isSelected());
            config.setQueryFlood(queryFloodCheckBox.isSelected());
            config.setAtaquesDos(ataquesDosCheckBox.isSelected());

            // Extras
            config.setReforzarSilence(reforzarSilenceCheckBox.isSelected());
            config.setBloqueaTusnamis(bloqueaTusnamisCheckBox.isSelected());
            config.setFloodNetTexto(floodNetTextoCheckBox.isSelected());

            // Anti deop/ban/kick
            config.setAntiDeopActivo(antiDeopActivoCheckBox.isSelected());
            config.setUsarChanserv(usarChanservCheckBox.isSelected());

            // Excluir protecciones
            config.setExcluirCtcp(excluirCtcpTextField.getText());

            // Anti-spam mensajes privados
            config.setAntiSpamAlAbrir(antiSpamAlAbrirCheckBox.isSelected());
            config.setAntiSpamSiempre(antiSpamSiempreCheckBox.isSelected());
            config.setListaPrivateSpam(new ArrayList<>(privateSpamListView.getItems()));
            config.setLogSpam(logSpamCheckBox.isSelected());

            // Anti-spam canales
            config.setAntiSpamCanalesActivo(antiSpamCanalesActivoCheckBox.isSelected());
            config.setAnularSpamQuits(anularSpamQuitsCheckBox.isSelected());

            JAXBContext context = JAXBContext.newInstance(FormularioProteccionConfig.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            marshaller.marshal(config, new File(CONFIG_FILE));

            System.out.println("Configuración de protección guardada exitosamente.");
        } catch (JAXBException ex) {
            ex.printStackTrace();
        }
    }

    // ===== Cargar configuración =====
    public void cargarConfiguracion() {
        try {
            File file = new File(CONFIG_FILE);
            if (!file.exists()) return;

            JAXBContext context = JAXBContext.newInstance(FormularioProteccionConfig.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            FormularioProteccionConfig config = (FormularioProteccionConfig) unmarshaller.unmarshal(file);

            // Flood/DOS
            ctcpFloodCheckBox.setSelected(config.isCtcpFlood());
            floodTextoCheckBox.setSelected(config.isFloodTexto());
            dccFloodCheckBox.setSelected(config.isDccFlood());
            queryFloodCheckBox.setSelected(config.isQueryFlood());
            ataquesDosCheckBox.setSelected(config.isAtaquesDos());

            // Extras
            reforzarSilenceCheckBox.setSelected(config.isReforzarSilence());
            bloqueaTusnamisCheckBox.setSelected(config.isBloqueaTusnamis());
            floodNetTextoCheckBox.setSelected(config.isFloodNetTexto());

            // Anti deop/ban/kick
            antiDeopActivoCheckBox.setSelected(config.isAntiDeopActivo());
            usarChanservCheckBox.setSelected(config.isUsarChanserv());

            // Excluir protecciones
            excluirCtcpTextField.setText(config.getExcluirCtcp());

            // Anti-spam mensajes privados
            antiSpamAlAbrirCheckBox.setSelected(config.isAntiSpamAlAbrir());
            antiSpamSiempreCheckBox.setSelected(config.isAntiSpamSiempre());
            privateSpamListView.setItems(FXCollections.observableArrayList(config.getListaPrivateSpam()));
            logSpamCheckBox.setSelected(config.isLogSpam());

            // Anti-spam canales
            antiSpamCanalesActivoCheckBox.setSelected(config.isAntiSpamCanalesActivo());
            anularSpamQuitsCheckBox.setSelected(config.isAnularSpamQuits());

            System.out.println("Configuración de protección cargada exitosamente.");
        } catch (JAXBException ex) {
            ex.printStackTrace();
        }
    }
}
