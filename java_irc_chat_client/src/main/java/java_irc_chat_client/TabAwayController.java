package java_irc_chat_client;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.File;

public class TabAwayController {

    @FXML private CheckBox cambiarNickCheckBox;
    @FXML private TextField nickAwayTextField;
    @FXML private CheckBox notificarEstadoCheckBox;
    @FXML private CheckBox deOpCanalesCheckBox;
    @FXML private CheckBox anunciarContestadorCheckBox;
    @FXML private CheckBox desactivarSonidosCheckBox;
    @FXML private CheckBox contestadoCheckBox;
    @FXML private CheckBox multiserverCheckBox;

    @FXML private ListView<String> awayListView;
    @FXML private ListView<String> razonesListView;

    @FXML private CheckBox contestadorActivoCheckBox;
    @FXML private TextField mensajeContestadorTextField;

    @FXML private CheckBox anunciarCheckBox;
    @FXML private TextField cadaTextField;
    @FXML private TextField textoAvisoTextField;
    @FXML private TextField textoVueltaTextField;
    @FXML private TextField anunciarSoloTextField;
    @FXML private TextField noAnunciarTextField;

    @FXML private TextField autoAwayMinsTextField;
    @FXML private TextField razonAutoAwayTextField;
    @FXML private CheckBox minimizarSystrayCheckBox;

    // Botones existentes
    @FXML private CheckBox antiSpamAlAbrirCheckBox;
    @FXML private CheckBox antiSpamSiempreCheckBox;
    @FXML private ListView<String> privateSpamListView;
    @FXML private Button addSpamButton;
    @FXML private Button removeSpamButton;
    @FXML private CheckBox loguearSpamCheckBox;
    @FXML private Button verSpamButton;
    @FXML private Button borrarSpamButton;
    @FXML private CheckBox antiSpamCanalesActivoCheckBox;
    @FXML private Label antiSpamCanalesInfoLabel;
    @FXML private CheckBox anularSpamQuitsCheckBox;

    private final File configFile = new File(System.getProperty("user.home"), "formulario_setup_away.xml");

    @FXML
    private void initialize() {
        System.out.println("TabAwayController inicializado.");

        // Cargar configuración al inicializar
        loadConfig();
    }

    // --- Botones de SPAM ---
    @FXML
    private void onAddSpamButtonClicked() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Añadir palabra");
        dialog.setHeaderText("Añadir palabra a la lista de spam");
        dialog.setContentText("Palabra:");
        dialog.showAndWait().ifPresent(word -> privateSpamListView.getItems().add(word));
    }

    @FXML
    private void onRemoveSpamButtonClicked() {
        String selected = privateSpamListView.getSelectionModel().getSelectedItem();
        if (selected != null) privateSpamListView.getItems().remove(selected);
    }

    @FXML
    private void onVerSpamButtonClicked() {
        System.out.println("Ver SPAM.log");
    }

    @FXML
    private void onBorrarSpamButtonClicked() {
        System.out.println("Borrar SPAM.log");
    }

    // --- Getters y Setters existentes (sin perderlos) ---
    public boolean isAntiSpamAlAbrir() { return antiSpamAlAbrirCheckBox.isSelected(); }
    public void setAntiSpamAlAbrir(boolean value) { antiSpamAlAbrirCheckBox.setSelected(value); }

    public boolean isAntiSpamSiempre() { return antiSpamSiempreCheckBox.isSelected(); }
    public void setAntiSpamSiempre(boolean value) { antiSpamSiempreCheckBox.setSelected(value); }

    public ListView<String> getPrivateSpamListView() { return privateSpamListView; }

    public boolean isLoguearSpam() { return loguearSpamCheckBox.isSelected(); }
    public void setLoguearSpam(boolean value) { loguearSpamCheckBox.setSelected(value); }

    public boolean isAntiSpamCanalesActivo() { return antiSpamCanalesActivoCheckBox.isSelected(); }
    public void setAntiSpamCanalesActivo(boolean value) { antiSpamCanalesActivoCheckBox.setSelected(value); }

    public boolean isAnularSpamQuits() { return anularSpamQuitsCheckBox.isSelected(); }
    public void setAnularSpamQuits(boolean value) { anularSpamQuitsCheckBox.setSelected(value); }

    // --- Persistencia XML ---
    public void loadConfig() {
        try {
            if (configFile.exists()) {
                JAXBContext context = JAXBContext.newInstance(FormularioAwayConfig.class);
                Unmarshaller um = context.createUnmarshaller();
                FormularioAwayConfig config = (FormularioAwayConfig) um.unmarshal(configFile);

                cambiarNickCheckBox.setSelected(config.isCambiarNick());
                nickAwayTextField.setText(config.getNickAway());
                notificarEstadoCheckBox.setSelected(config.isNotificarEstado());
                deOpCanalesCheckBox.setSelected(config.isDeOpCanales());
                anunciarContestadorCheckBox.setSelected(config.isAnunciarContestador());
                desactivarSonidosCheckBox.setSelected(config.isDesactivarSonidos());
                contestadoCheckBox.setSelected(config.isContestado());
                multiserverCheckBox.setSelected(config.isMultiserver());

                awayListView.getItems().setAll(config.getAwayList());
                razonesListView.getItems().setAll(config.getRazonesList());

                contestadorActivoCheckBox.setSelected(config.isContestadorActivo());
                mensajeContestadorTextField.setText(config.getMensajeContestador());

                anunciarCheckBox.setSelected(config.isAnunciar());
                cadaTextField.setText(String.valueOf(config.getCadaMinutos()));
                textoAvisoTextField.setText(config.getTextoAviso());
                textoVueltaTextField.setText(config.getTextoVuelta());
                anunciarSoloTextField.setText(config.getAnunciarSolo());
                noAnunciarTextField.setText(config.getNoAnunciar());

                autoAwayMinsTextField.setText(String.valueOf(config.getAutoAwayMins()));
                razonAutoAwayTextField.setText(config.getRazonAutoAway());
                minimizarSystrayCheckBox.setSelected(config.isMinimizarSystray());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveConfig() {
        try {
            FormularioAwayConfig config = new FormularioAwayConfig();

            config.setCambiarNick(cambiarNickCheckBox.isSelected());
            config.setNickAway(nickAwayTextField.getText());
            config.setNotificarEstado(notificarEstadoCheckBox.isSelected());
            config.setDeOpCanales(deOpCanalesCheckBox.isSelected());
            config.setAnunciarContestador(anunciarContestadorCheckBox.isSelected());
            config.setDesactivarSonidos(desactivarSonidosCheckBox.isSelected());
            config.setContestado(contestadoCheckBox.isSelected());
            config.setMultiserver(multiserverCheckBox.isSelected());

            config.setAwayList(awayListView.getItems());
            config.setRazonesList(razonesListView.getItems());

            config.setContestadorActivo(contestadorActivoCheckBox.isSelected());
            config.setMensajeContestador(mensajeContestadorTextField.getText());

            config.setAnunciar(anunciarCheckBox.isSelected());
            config.setCadaMinutos(Integer.parseInt(cadaTextField.getText()));
            config.setTextoAviso(textoAvisoTextField.getText());
            config.setTextoVuelta(textoVueltaTextField.getText());
            config.setAnunciarSolo(anunciarSoloTextField.getText());
            config.setNoAnunciar(noAnunciarTextField.getText());

            config.setAutoAwayMins(Integer.parseInt(autoAwayMinsTextField.getText()));
            config.setRazonAutoAway(razonAutoAwayTextField.getText());
            config.setMinimizarSystray(minimizarSystrayCheckBox.isSelected());

            JAXBContext context = JAXBContext.newInstance(FormularioAwayConfig.class);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            m.marshal(config, configFile);

            System.out.println("Configuración guardada en: " + configFile.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

