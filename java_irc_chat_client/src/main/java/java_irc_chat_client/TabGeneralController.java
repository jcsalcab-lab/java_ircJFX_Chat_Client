package java_irc_chat_client;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import java_irc_chat_client.formularios_persistencia.FormularioSetupGeneral;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;

import java.io.File;

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

    private final File configFile = new File(System.getProperty("user.home"), "formulario_setup_general.xml");

    @FXML
    private void initialize() {
        System.out.println("TabGeneralController inicializado.");
        cargarConfiguracion();
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


    // ========================
    // NUEVO: persistencia JAXB
    // ========================
    public void cargarConfiguracion() {
        if (configFile.exists()) {
            try {
                JAXBContext context = JAXBContext.newInstance(FormularioSetupGeneral.class);
                Unmarshaller um = context.createUnmarshaller();
                FormularioSetupGeneral data = (FormularioSetupGeneral) um.unmarshal(configFile);

                setInvisible(data.isInvisible());
                setWallOps(data.isWallOps());
                setNoticias(data.isNoticias());
                setOtros(data.getOtros());
                setNick(data.getNick());

                setMedidorLag(data.isMedidorLag());
                setAntiIdle(data.isAntiIdle());
                setNoResponderCTCP(data.isNoResponderCTCP());
                setAvisoMencion(data.isAvisoMencion());
                setRelocalizadoNicks(data.isRelocalizadoNicks());

                setBloquearPrivados(data.isBloquearPrivados());
                setMensajeExcusa(data.getMensajeExcusa());
                setLimitePrivados(data.getLimitePrivados());

                System.out.println("Configuración cargada desde XML.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void guardarConfiguracion() {
        try {
            FormularioSetupGeneral data = new FormularioSetupGeneral();

            data.setInvisible(isInvisible());
            data.setWallOps(isWallOps());
            data.setNoticias(isNoticias());
            data.setOtros(getOtros());
            data.setNick(getNick());

            data.setMedidorLag(isMedidorLag());
            data.setAntiIdle(isAntiIdle());
            data.setNoResponderCTCP(isNoResponderCTCP());
            data.setAvisoMencion(isAvisoMencion());
            data.setRelocalizadoNicks(isRelocalizadoNicks());

            data.setBloquearPrivados(isBloquearPrivados());
            data.setMensajeExcusa(getMensajeExcusa());
            data.setLimitePrivados(getLimitePrivados());

            JAXBContext context = JAXBContext.newInstance(FormularioSetupGeneral.class);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            m.marshal(data, configFile);

            System.out.println("Configuración guardada en XML.");
            System.out.println("Guardando XML en: " + configFile.getAbsolutePath());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
