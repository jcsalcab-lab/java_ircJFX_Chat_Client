package java_irc_chat_client;

import javafx.fxml.FXML;
import javafx.scene.control.*;

public class TabAwayController {

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

    @FXML
    private void initialize() {
        System.out.println("TabAwayController inicializado.");
    }

    // --- Botones ---
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

    // --- Getters y Setters ---
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
}
