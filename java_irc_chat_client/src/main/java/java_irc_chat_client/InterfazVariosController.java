package java_irc_chat_client;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;

import java.io.File;
import java.util.ArrayList;

public class InterfazVariosController {

    @FXML private ListView<String> listViewColores;
    @FXML private CheckBox chkAnimacionCerrar;
    @FXML private CheckBox chkMostrarTextura;
    @FXML private TextField txtRedondeo;

    private ObservableList<String> coloresData;
    private final File configFile = new File(System.getProperty("user.home"), "formulario_setup_varios.xml");

    @FXML
    public void initialize() {
        // Inicialización básica
        coloresData = FXCollections.observableArrayList("Rojo", "Verde", "Azul", "Amarillo");
        listViewColores.setItems(coloresData);

        chkAnimacionCerrar.setSelected(false);
        chkMostrarTextura.setSelected(false);
        txtRedondeo.setText("8");

        // Cargar configuración si existe
        loadConfig();
    }

    // --- Persistencia XML ---
    public void loadConfig() {
        try {
            if (configFile.exists()) {
                JAXBContext context = JAXBContext.newInstance(FormularioVariosVariosConfig.class);
                Unmarshaller um = context.createUnmarshaller();
                FormularioVariosVariosConfig config = (FormularioVariosVariosConfig) um.unmarshal(configFile);

                coloresData.clear();
                if (config.getColores() != null) {
                    coloresData.addAll(config.getColores());
                }

                chkAnimacionCerrar.setSelected(config.isAnimacionCerrar());
                chkMostrarTextura.setSelected(config.isMostrarTextura());
                txtRedondeo.setText(String.valueOf(config.getRedondeoGUI()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveConfig() {
        try {
            FormularioVariosVariosConfig config = new FormularioVariosVariosConfig();
            config.setColores(new ArrayList<>(coloresData));
            config.setAnimacionCerrar(chkAnimacionCerrar.isSelected());
            config.setMostrarTextura(chkMostrarTextura.isSelected());
            config.setRedondeoGUI(getRedondeo());

            JAXBContext context = JAXBContext.newInstance(FormularioVariosVariosConfig.class);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            m.marshal(config, configFile);

            System.out.println("Configuración guardada en: " + configFile.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // --- Métodos de acceso existentes ---
    public boolean isAnimacionCerrar() { return chkAnimacionCerrar.isSelected(); }
    public boolean isMostrarTextura() { return chkMostrarTextura.isSelected(); }
    public int getRedondeo() {
        try {
            return Integer.parseInt(txtRedondeo.getText());
        } catch (NumberFormatException e) {
            return 8;
        }
    }

    // --- Métodos para manipular lista de colores ---
    public void agregarColor(String color) {
        if (color != null && !color.trim().isEmpty()) coloresData.add(color.trim());
    }

    public void eliminarColor(String color) {
        coloresData.remove(color);
    }
}
