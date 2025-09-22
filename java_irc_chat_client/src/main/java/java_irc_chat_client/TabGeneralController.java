package java_irc_chat_client;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;

public class TabGeneralController {

    @FXML private CheckBox invisibleCheckBox;
    @FXML private CheckBox wallOpsCheckBox;
    @FXML private CheckBox noticiasCheckBox;
    @FXML private TextField otrosTextField;
    @FXML private TextField nickTextField;

    @FXML private CheckBox medidorLagCheckBox;
    @FXML private CheckBox antiIdleCheckBox;
    @FXML private CheckBox noResponderCTCPCheckBox;
    @FXML private CheckBox avisoMencionCheckBox;
    @FXML private CheckBox relocalizadoNicksCheckBox;

    @FXML private CheckBox bloquearPrivadosCheckBox;
    @FXML private TextField mensajeExcusaTextField;
    @FXML private TextField limitePrivadosTextField;

    @FXML
    private void initialize() {
        System.out.println("TabGeneralController inicializado.");
    }

    // --- Getters y Setters ---
    public boolean isInvisible() { return invisibleCheckBox.isSelected(); }
    public void setInvisible(boolean value) { invisibleCheckBox.setSelected(value); }

    public boolean isWallOps() { return wallOpsCheckBox.isSelected(); }
    public void setWallOps(boolean value) { wallOpsCheckBox.setSelected(value); }

    public boolean isNoticias() { return noticiasCheckBox.isSelected(); }
    public void setNoticias(boolean value) { noticiasCheckBox.setSelected(value); }

    public String getOtros() { return otrosTextField.getText(); }
    public void setOtros(String value) { otrosTextField.setText(value); }

    public String getNick() { return nickTextField.getText(); }
    public void setNick(String value) { nickTextField.setText(value); }

    public boolean isMedidorLag() { return medidorLagCheckBox.isSelected(); }
    public void setMedidorLag(boolean value) { medidorLagCheckBox.setSelected(value); }

    public boolean isAntiIdle() { return antiIdleCheckBox.isSelected(); }
    public void setAntiIdle(boolean value) { antiIdleCheckBox.setSelected(value); }

    public boolean isNoResponderCTCP() { return noResponderCTCPCheckBox.isSelected(); }
    public void setNoResponderCTCP(boolean value) { noResponderCTCPCheckBox.setSelected(value); }

    public boolean isAvisoMencion() { return avisoMencionCheckBox.isSelected(); }
    public void setAvisoMencion(boolean value) { avisoMencionCheckBox.setSelected(value); }

    public boolean isRelocalizadoNicks() { return relocalizadoNicksCheckBox.isSelected(); }
    public void setRelocalizadoNicks(boolean value) { relocalizadoNicksCheckBox.setSelected(value); }

    public boolean isBloquearPrivados() { return bloquearPrivadosCheckBox.isSelected(); }
    public void setBloquearPrivados(boolean value) { bloquearPrivadosCheckBox.setSelected(value); }

    public String getMensajeExcusa() { return mensajeExcusaTextField.getText(); }
    public void setMensajeExcusa(String value) { mensajeExcusaTextField.setText(value); }

    public String getLimitePrivados() { return limitePrivadosTextField.getText(); }
    public void setLimitePrivados(String value) { limitePrivadosTextField.setText(value); }
}
