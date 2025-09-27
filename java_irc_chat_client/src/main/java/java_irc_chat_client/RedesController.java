package java_irc_chat_client;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.io.File;
import java.util.*;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import java_irc_chat_client.formularios_persistencia.FormularioRedesConfig;
import java_irc_chat_client.formularios_persistencia.RedesConfigWrapper;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.charset.StandardCharsets;

public class RedesController {

    // Combo y campos generales
    @FXML private ComboBox<String> comboRedes;
    @FXML private TextField txtNombreRed, txtFicheroMenus, txtApareceComo, txtMascaraIpBots,
            txtNickserv, txtChanserv, txtMemoserv, txtPideId;

    @FXML private CheckBox chkAutoCargarMenus, chkMensajeBotsPrivado, chkSinPerfil;

    @FXML private Button btnNuevaRed, btnBorrarRed;

    // Checkboxes subpestañas
    @FXML private CheckBox chkIdentificarNick, chkLiberarOcupado, chkAutoFundador;
    @FXML private CheckBox chkAutoEntrarSubpestana; // Auto-entrar subpestaña
    @FXML private TextField txtRetardoSubpestana;

    // ListViews y combos
    @FXML private ListView<String> listViewNicks, listViewCanales, listViewCanalesAutoEntrar;
    @FXML private ComboBox<String> comboRedesAutoEntrar;

    private final Map<String, FormularioRedesConfig> configsPorRed = new HashMap<>();
    private FormularioRedesConfig configActiva;
    private final File ficheroXML = new File("redes_config.xml");

    @FXML
    public void initialize() {
        comboRedes.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (oldVal != null) actualizarConfigActivaDesdeFormulario();
            if (newVal != null) cargarConfig(newVal);
        });

        btnNuevaRed.setOnAction(e -> nuevaRed());
        btnBorrarRed.setOnAction(e -> borrarRed());

        cargarConfiguracion();

        if (!comboRedes.getItems().isEmpty())
            comboRedes.getSelectionModel().select(0);

        Platform.runLater(this::bindCheckboxesSeguros);
    }

    private void bindCheckboxesSeguros() {
        if (chkAutoEntrarSubpestana == null) {
            chkAutoEntrarSubpestana = new CheckBox();
        }
    }

    private void nuevaRed() {
        String nombre = "Red" + (configsPorRed.size() + 1);
        FormularioRedesConfig cfg = new FormularioRedesConfig();
        cfg.setNombreRed(nombre);
        cfg.setNicks(new ArrayList<>());
        cfg.setCanales(new ArrayList<>());
        cfg.setAutoEntrarList(new ArrayList<>());
        cfg.setAutoEntrar(false);
        cfg.setRetardo("0");

        configsPorRed.put(nombre, cfg);
        comboRedes.getItems().add(nombre);
        comboRedes.getSelectionModel().select(nombre);
    }

    private void borrarRed() {
        String seleccionada = comboRedes.getSelectionModel().getSelectedItem();
        if (seleccionada != null) {
            configsPorRed.remove(seleccionada);
            comboRedes.getItems().remove(seleccionada);
            limpiarFormulario();
            configActiva = null;
        }
    }

    private void cargarConfig(String nombreRed) {
        configActiva = configsPorRed.get(nombreRed);
        if (configActiva == null) {
            configActiva = new FormularioRedesConfig();
            configActiva.setNombreRed(nombreRed);
            configActiva.setAutoEntrar(false);
            configActiva.setRetardo("0");
            configsPorRed.put(nombreRed, configActiva);
        }
        bindFormulario(configActiva);
    }

    private void bindFormulario(FormularioRedesConfig cfg) {
        if (cfg == null) return;

        txtNombreRed.setText(cfg.getNombreRed() != null ? cfg.getNombreRed() : "");
        txtFicheroMenus.setText(cfg.getFicheroMenus() != null ? cfg.getFicheroMenus() : "");
        txtApareceComo.setText(cfg.getApareceComo() != null ? cfg.getApareceComo() : "");
        txtMascaraIpBots.setText(cfg.getMascaraIpBots() != null ? cfg.getMascaraIpBots() : "");
        txtNickserv.setText(cfg.getNickserv() != null ? cfg.getNickserv() : "");
        txtChanserv.setText(cfg.getChanserv() != null ? cfg.getChanserv() : "");
        txtMemoserv.setText(cfg.getMemoserv() != null ? cfg.getMemoserv() : "");
        txtPideId.setText(cfg.getPideId() != null ? cfg.getPideId() : "");

        chkAutoCargarMenus.setSelected(cfg.isAutoCargarMenus());
        chkMensajeBotsPrivado.setSelected(cfg.isMensajeBotsPrivado());
        chkSinPerfil.setSelected(cfg.isSinPerfil());

        chkIdentificarNick.setSelected(cfg.isIdentificarNick());
        chkLiberarOcupado.setSelected(cfg.isLiberarOcupado());
        chkAutoFundador.setSelected(cfg.isAutoFundador());

        chkAutoEntrarSubpestana.setSelected(cfg.isAutoEntrar());
        System.out.println("[DEBUG CARGAR] Red=" + cfg.getNombreRed()
            + " AutoEntrar (en XML)=" + cfg.isAutoEntrar()
            + " -> CheckBox seleccionado=" + chkAutoEntrarSubpestana.isSelected());

        txtRetardoSubpestana.setText(cfg.getRetardo() != null ? cfg.getRetardo() : "0");

        listViewNicks.setItems(FXCollections.observableArrayList(cfg.getNicks() != null ? cfg.getNicks() : Collections.emptyList()));
        listViewCanales.setItems(FXCollections.observableArrayList(cfg.getCanales() != null ? cfg.getCanales() : Collections.emptyList()));
        listViewCanalesAutoEntrar.setItems(FXCollections.observableArrayList(cfg.getAutoEntrarList() != null ? cfg.getAutoEntrarList() : Collections.emptyList()));
    }

    private void limpiarFormulario() {
        txtNombreRed.clear(); txtFicheroMenus.clear(); txtApareceComo.clear(); txtMascaraIpBots.clear();
        txtNickserv.clear(); txtChanserv.clear(); txtMemoserv.clear(); txtPideId.clear(); txtRetardoSubpestana.clear();

        chkAutoCargarMenus.setSelected(false); chkMensajeBotsPrivado.setSelected(false); chkSinPerfil.setSelected(false);
        chkIdentificarNick.setSelected(false); chkLiberarOcupado.setSelected(false); chkAutoFundador.setSelected(false); chkAutoEntrarSubpestana.setSelected(false);

        listViewNicks.getItems().clear();
        listViewCanales.getItems().clear();
        listViewCanalesAutoEntrar.getItems().clear();
    }

    private void actualizarConfigActivaDesdeFormulario() {
        if (configActiva == null) return;

        configActiva.setNombreRed(txtNombreRed.getText());
        configActiva.setFicheroMenus(txtFicheroMenus.getText());
        configActiva.setApareceComo(txtApareceComo.getText());
        configActiva.setMascaraIpBots(txtMascaraIpBots.getText());
        configActiva.setNickserv(txtNickserv.getText());
        configActiva.setChanserv(txtChanserv.getText());
        configActiva.setMemoserv(txtMemoserv.getText());
        configActiva.setPideId(txtPideId.getText());

        configActiva.setAutoCargarMenus(chkAutoCargarMenus.isSelected());
        configActiva.setMensajeBotsPrivado(chkMensajeBotsPrivado.isSelected());
        configActiva.setSinPerfil(chkSinPerfil.isSelected());

        configActiva.setIdentificarNick(chkIdentificarNick.isSelected());
        configActiva.setLiberarOcupado(chkLiberarOcupado.isSelected());
        configActiva.setAutoFundador(chkAutoFundador.isSelected());

        configActiva.setAutoEntrar(chkAutoEntrarSubpestana.isSelected());
        configActiva.setRetardo(txtRetardoSubpestana.getText());

        configActiva.setNicks(new ArrayList<>(listViewNicks.getItems()));
        configActiva.setCanales(new ArrayList<>(listViewCanales.getItems()));
        configActiva.setAutoEntrarList(new ArrayList<>(listViewCanalesAutoEntrar.getItems()));

        System.out.println("[DEBUG actualizar] Red=" + configActiva.getNombreRed()
            + " CheckBox Auto_entrar=" + chkAutoEntrarSubpestana.isSelected()
            + " -> Guardado en modelo=" + configActiva.isAutoEntrar());
    }

    public void guardarTodasSubpestanas() {
        System.out.println("[DEBUG GUARDAR] Valor real del CheckBox en UI="
            + chkAutoEntrarSubpestana);

        if (chkAutoEntrarSubpestana != null) {
            System.out.println("[DEBUG GUARDAR] CheckBox seleccionado="
                + chkAutoEntrarSubpestana.isSelected());
        } else {
            System.out.println("[DEBUG GUARDAR] El CheckBox es NULL (no está enlazado desde FXML)");
        }

        if (txtNombreRed.getScene() != null)
            txtNombreRed.getParent().requestFocus();

        actualizarConfigActivaDesdeFormulario();
        guardarConfiguracion();

        System.out.println("--------- DEBUG Redes ---------");
        System.out.println("Archivo XML: " + ficheroXML.getAbsolutePath());
        for (FormularioRedesConfig cfg : configsPorRed.values()) {
            System.out.println("Red: " + cfg.getNombreRed()
                + ", Auto-entrar: " + cfg.isAutoEntrar()
                + ", Retardo: " + cfg.getRetardo());
        }
        System.out.println("Todas las configuraciones se han guardado correctamente.");
    }




    

    public void guardarConfiguracion() {
        actualizarConfigActivaDesdeFormulario();
        try {
            RedesConfigWrapper wrapper = new RedesConfigWrapper();
            wrapper.setRedes(new ArrayList<>(configsPorRed.values()));

            System.out.println("[DEBUG GUARDAR] ficheroXML=" + ficheroXML.getAbsolutePath()
                + " exists=" + ficheroXML.exists()
                + " lastModified=" + (ficheroXML.exists() ? ficheroXML.lastModified() : 0)
                + " size=" + (ficheroXML.exists() ? ficheroXML.length() : 0));

            // Marshal to a StringWriter first so we can see exact XML that JAXB genera
            JAXBContext context = JAXBContext.newInstance(RedesConfigWrapper.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            StringWriter sw = new StringWriter();
            marshaller.marshal(wrapper, sw);
            String xmlString = sw.toString();

            System.out.println("[DEBUG GUARDAR] XML a escribir:\n" + xmlString);

            // Escribir en fichero (real)
            marshaller.marshal(wrapper, ficheroXML);

            System.out.println("[DEBUG GUARDAR] fichero escrito correctamente. newSize=" + ficheroXML.length()
                + " newLastModified=" + ficheroXML.lastModified());

            // Stacktrace para ver quién está llamando a guardar (si hay sobreescrituras)
            System.out.println("[DEBUG GUARDAR] Stack trace (para localizar llamadas):");
            StackTraceElement[] st = Thread.currentThread().getStackTrace();
            for (int i = 0; i < Math.min(st.length, 20); i++) { // limitar líneas
                System.out.println("\t at " + st[i]);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void cargarConfiguracion() {
        try {
            System.out.println("[DEBUG CARGAR] ficheroXML=" + ficheroXML.getAbsolutePath()
                + " exists=" + ficheroXML.exists()
                + " size=" + (ficheroXML.exists() ? ficheroXML.length() : 0)
                + " lastModified=" + (ficheroXML.exists() ? ficheroXML.lastModified() : 0));

            if (!ficheroXML.exists()) return;

            // Leer y mostrar contenido del fichero antes de unmarshalling (útil para saber qué hay realmente)
            try {
                String fileContent = Files.readString(ficheroXML.toPath(), StandardCharsets.UTF_8);
                System.out.println("[DEBUG CARGAR] Contenido actual del fichero redes_config.xml:\n" + fileContent);
            } catch (Exception exRead) {
                System.out.println("[DEBUG CARGAR] No se pudo leer el contenido del fichero antes de unmarshal: " + exRead.getMessage());
            }

            JAXBContext context = JAXBContext.newInstance(RedesConfigWrapper.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            RedesConfigWrapper wrapper = (RedesConfigWrapper) unmarshaller.unmarshal(ficheroXML);

            configsPorRed.clear();
            comboRedes.getItems().clear();

            for (FormularioRedesConfig cfg : wrapper.getRedes()) {
                System.out.println("[DEBUG CARGAR] Red=" + cfg.getNombreRed()
                    + " AutoEntrar (leído XML)=" + cfg.isAutoEntrar()
                    + " Retardo=" + cfg.getRetardo());
                configsPorRed.put(cfg.getNombreRed(), cfg);
                comboRedes.getItems().add(cfg.getNombreRed());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
