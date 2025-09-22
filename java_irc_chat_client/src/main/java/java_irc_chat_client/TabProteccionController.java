package java_irc_chat_client;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;

public class TabProteccionController {

    @FXML private CheckBox ctcpFloodCheckBox;
    @FXML private CheckBox floodTextoCheckBox;
    @FXML private CheckBox dccFloodCheckBox;
    @FXML private CheckBox queryFloodCheckBox;
    @FXML private CheckBox dosAttacksCheckBox;

    @FXML private CheckBox reforzarSilenceCheckBox;
    @FXML private CheckBox bloqueaTusnamisCheckBox;
    @FXML private CheckBox floodNetTextoCheckBox;

    @FXML private CheckBox antiDeopActivoCheckBox;
    @FXML private CheckBox usarChanservCheckBox;

    @FXML private TextField excluirCTCPTextField;
    @FXML private Button infoProteccionesButton;

    @FXML
    private void initialize() {
        System.out.println("TabProteccionController inicializado.");
    }

    // --- Botón info ---
    @FXML
    private void onInfoProteccionesClicked() {
        System.out.println("Botón infoProtecciones pulsado");
    }

    // --- Getters y Setters ---
    public boolean isCTCPFlood() { return ctcpFloodCheckBox.isSelected(); }
    public void setCTCPFlood(boolean value) { ctcpFloodCheckBox.setSelected(value); }

    public boolean isFloodTexto() { return floodTextoCheckBox.isSelected(); }
    public void setFloodTexto(boolean value) { floodTextoCheckBox.setSelected(value); }

    public boolean isDCCFlood() { return dccFloodCheckBox.isSelected(); }
    public void setDCCFlood(boolean value) { dccFloodCheckBox.setSelected(value); }

    public boolean isQueryFlood() { return queryFloodCheckBox.isSelected(); }
    public void setQueryFlood(boolean value) { queryFloodCheckBox.setSelected(value); }

    public boolean isDosAttacks() { return dosAttacksCheckBox.isSelected(); }
    public void setDosAttacks(boolean value) { dosAttacksCheckBox.setSelected(value); }

    public boolean isReforzarSilence() { return reforzarSilenceCheckBox.isSelected(); }
    public void setReforzarSilence(boolean value) { reforzarSilenceCheckBox.setSelected(value); }

    public boolean isBloqueaTusnamis() { return bloqueaTusnamisCheckBox.isSelected(); }
    public void setBloqueaTusnamis(boolean value) { bloqueaTusnamisCheckBox.setSelected(value); }

    public boolean isFloodNetTexto() { return floodNetTextoCheckBox.isSelected(); }
    public void setFloodNetTexto(boolean value) { floodNetTextoCheckBox.setSelected(value); }

    public boolean isAntiDeopActivo() { return antiDeopActivoCheckBox.isSelected(); }
    public void setAntiDeopActivo(boolean value) { antiDeopActivoCheckBox.setSelected(value); }

    public boolean isUsarChanserv() { return usarChanservCheckBox.isSelected(); }
    public void setUsarChanserv(boolean value) { usarChanservCheckBox.setSelected(value); }

    public String getExcluirCTCP() { return excluirCTCPTextField.getText(); }
    public void setExcluirCTCP(String value) { excluirCTCPTextField.setText(value); }
}
