package java_irc_chat_client;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import java_irc_chat_client.formularios_persistencia.FormularioInterfazPanelConfig;

import java.io.File;
import java.util.ArrayList;

public class InterfazPanelController {

    @FXML private ListView<String> listApartados;
    @FXML private Button btnAgregar;
    @FXML private Button btnEliminar;
    @FXML private CheckBox chkActivo; // <- ahora enlazado al FXML

    private final File configFile = new File(System.getProperty("user.home"), "formulario_setup_panel.xml");

    @FXML
    public void initialize() {
        // Solo configurar botones aquí, no cargar aún (lo hará INTERFAZController)
        btnAgregar.setOnAction(e -> {
            listApartados.getItems().add("Nuevo Apartado");
            guardarConfiguracion();
        });

        btnEliminar.setOnAction(e -> {
            int selectedIndex = listApartados.getSelectionModel().getSelectedIndex();
            if (selectedIndex >= 0) {
                listApartados.getItems().remove(selectedIndex);
                guardarConfiguracion();
            }
        });
    }

    // --- Persistencia XML ---
    public void loadConfig() {
        try {
            if (configFile.exists()) {
                JAXBContext context = JAXBContext.newInstance(FormularioInterfazPanelConfig.class);
                Unmarshaller um = context.createUnmarshaller();
                FormularioInterfazPanelConfig config = (FormularioInterfazPanelConfig) um.unmarshal(configFile);

                listApartados.getItems().clear();
                if (config.getApartados() != null) {
                    listApartados.getItems().addAll(config.getApartados());
                }

                chkActivo.setSelected(config.isActivo()); // <- persistencia del CheckBox
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void guardarConfiguracion() {
        try {
            FormularioInterfazPanelConfig config = new FormularioInterfazPanelConfig();
            config.setApartados(new ArrayList<>(listApartados.getItems()));
            config.setActivo(chkActivo.isSelected()); // <- guardar estado del CheckBox

            JAXBContext context = JAXBContext.newInstance(FormularioInterfazPanelConfig.class);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            m.marshal(config, configFile);

            System.out.println("Configuración de Panel guardada en: " + configFile.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
