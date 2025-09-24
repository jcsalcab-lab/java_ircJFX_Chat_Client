package java_irc_chat_client;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import java_irc_chat_client.formularios_persistencia.FormularioCartelesConfig;

import java.io.File;
import java.util.ArrayList;

public class InterfazCartelesController {

    @FXML private ListView<String> listEventos;
    @FXML private ComboBox<String> comboMostrarCarteles;
    @FXML private CheckBox chkDesactivarAway;

    private ObservableList<String> eventosData;
    private final File configFile = new File(System.getProperty("user.home"), "formulario_setup_carteles.xml");

    @FXML
    public void initialize() {
        // Inicialización básica (valores de prueba)
        eventosData = FXCollections.observableArrayList("Evento 1", "Evento 2", "Evento 3");
        listEventos.setItems(eventosData);

        comboMostrarCarteles.getItems().addAll("Siempre", "Solo cuando no está away", "Nunca");
        comboMostrarCarteles.getSelectionModel().selectFirst();

        chkDesactivarAway.setSelected(false);

        // Cargar configuración guardada si existe
        loadConfig();
    }

    // --- Persistencia XML ---
    public void loadConfig() {
        try {
            if (configFile.exists()) {
                JAXBContext context = JAXBContext.newInstance(FormularioCartelesConfig.class);
                Unmarshaller um = context.createUnmarshaller();
                FormularioCartelesConfig config = (FormularioCartelesConfig) um.unmarshal(configFile);

                eventosData.clear();
                if (config.getEventos() != null) {
                    eventosData.addAll(config.getEventos());
                }

                if (config.getModoMostrarCarteles() != null) {
                    comboMostrarCarteles.getSelectionModel().select(config.getModoMostrarCarteles());
                }

                chkDesactivarAway.setSelected(config.isDesactivarAway());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveConfig() {
        try {
            FormularioCartelesConfig config = new FormularioCartelesConfig();
            config.setEventos(new ArrayList<>(eventosData));
            config.setModoMostrarCarteles(comboMostrarCarteles.getSelectionModel().getSelectedItem());
            config.setDesactivarAway(chkDesactivarAway.isSelected());

            JAXBContext context = JAXBContext.newInstance(FormularioCartelesConfig.class);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            m.marshal(config, configFile);

            System.out.println("Configuración de carteles guardada en: " + configFile.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // --- Métodos auxiliares ---
    public boolean isDesactivarAway() { return chkDesactivarAway.isSelected(); }
    public String getModoMostrarCarteles() { return comboMostrarCarteles.getSelectionModel().getSelectedItem(); }
    public ObservableList<String> getEventosData() { return eventosData; }

    public void agregarEvento(String evento) {
        if (evento != null && !evento.trim().isEmpty()) {
            eventosData.add(evento.trim());
        }
    }

    public void eliminarEvento(String evento) {
        eventosData.remove(evento);
    }
}
