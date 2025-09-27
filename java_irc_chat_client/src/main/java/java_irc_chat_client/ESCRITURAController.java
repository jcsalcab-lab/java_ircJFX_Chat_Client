package java_irc_chat_client;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java_irc_chat_client.formularios_persistencia.*;

public class ESCRITURAController {

    @FXML private TabPane escrituraTabPane;

    // ----- Sets -----
    @FXML private CheckBox chkCorrectoOrtografico;
    @FXML private CheckBox chkConversorUnidades;
    @FXML private CheckBox chkEncriptador;
    @FXML private CheckBox chkNickCompletion;
    @FXML private CheckBox chkAdornosColores;
    @FXML private TextField txtColorMenor;
    @FXML private TextField txtColorArroba;
    @FXML private TextField txtColorNick;
    @FXML private TextField txtColorMayor;
    @FXML private CheckBox chkMostrarNickActivo;
    @FXML private TextField txtMiNickMenor;
    @FXML private TextField txtMiNickArroba;
    @FXML private TextField txtMiNickNick;
    @FXML private TextField txtMiNickMayor;
    @FXML private TextField txtJustificacionValor;
    @FXML private CheckBox chkIncluirArrobaMas;

    // ----- Nick Completion -----
    @FXML private CheckBox ncActivoCheckBox;
    @FXML private TextField ncPrimerSeparadorField;
    @FXML private TextField ncSegundoSeparadorField;
    @FXML private Button ncGuardarEstiloButton;
    @FXML private ListView<String> ncNickListView;
    @FXML private Button ncEliminarButton;

    // ----- Corrector -----
    @FXML private CheckBox crActivoCheckBox;
    @FXML private CheckBox crMayusculaCheckBox;
    @FXML private CheckBox crSubrayarUrlCheckBox;
    @FXML private TableView<FormularioCorrectorConfig.Palabra> crPalabrasTableView;
    @FXML private TableColumn<FormularioCorrectorConfig.Palabra, String> crColReemplazar;
    @FXML private TableColumn<FormularioCorrectorConfig.Palabra, String> crColPor;
    @FXML private Button crAgregarButton;
    @FXML private Button crEliminarButton;

    // ----- Conversor -----
    @FXML private CheckBox cvActivoCheckBox;
    @FXML private TextField cvCantidadMedidaField;
    @FXML private TextField cvEuroDolaresField;
    @FXML private Label cvResultadoLabel;
    @FXML private Button cvAyudaTagsButton;

    // ----- Adornos -----
    @FXML private CheckBox adActivoCheckBox;
    @FXML private TextField adEstiloField;
    @FXML private CheckBox adAdornosCheckBox;
    @FXML private CheckBox adNegritaCheckBox;
    @FXML private CheckBox adSubrayadoCheckBox;
    @FXML private CheckBox adUsarColoresCheckBox;
    @FXML private TextField adFicheroEstilosField;
    @FXML private ListView<String> adAdornosListView;
    @FXML private Button adAgregarButton;
    @FXML private Button adEliminarButton;

    private ObservableList<String> adAdornosData = FXCollections.observableArrayList();

    // ----- Archivos de configuración -----
    private final File setsConfigFile = new File(System.getProperty("user.home"), "setup_config.xml");
    private final File nickCompletionFile = new File("nick_completion_config.xml");
    private final File conversorFile = new File("conversor_config.xml");
    private final File adornosFile = new File(System.getProperty("user.home"), "formulario_setup_escritura_adornos.xml");
    private final File correctorFile = new File(System.getProperty("user.home"), "corrector_config.xml");

    // ----- Datos Corrector -----
    private final ObservableList<FormularioCorrectorConfig.Palabra> palabrasData = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        // Inicializar listas
        adAdornosListView.setItems(adAdornosData);
        crPalabrasTableView.setItems(palabrasData);

        // Inicializar columnas TableView Corrector
        crColReemplazar.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getReemplazar()));
        crColPor.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getPor()));

        // Listeners Corrector para persistencia automática
        crActivoCheckBox.selectedProperty().addListener((obs, oldV, newV) -> saveCorrectorConfig());
        crMayusculaCheckBox.selectedProperty().addListener((obs, oldV, newV) -> saveCorrectorConfig());
        crSubrayarUrlCheckBox.selectedProperty().addListener((obs, oldV, newV) -> saveCorrectorConfig());
        palabrasData.addListener((javafx.collections.ListChangeListener.Change<? extends FormularioCorrectorConfig.Palabra> c) -> saveCorrectorConfig());

        // Botones Corrector
        crAgregarButton.setOnAction(e -> agregarPalabra());
        crEliminarButton.setOnAction(e -> eliminarPalabra());

        // Botones Adornos
        adAgregarButton.setOnAction(e -> {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Añadir Adorno");
            dialog.setHeaderText("Agregar un nuevo adorno");
            dialog.setContentText("Valor:");
            dialog.showAndWait().ifPresent(adAdornosData::add);
            saveAdornosConfig();
        });
        adEliminarButton.setOnAction(e -> {
            String sel = adAdornosListView.getSelectionModel().getSelectedItem();
            if (sel != null) {
                adAdornosData.remove(sel);
                saveAdornosConfig();
            }
        });

        // Listeners Adornos para persistencia automática
        adActivoCheckBox.selectedProperty().addListener((obs, oldV, newV) -> saveAdornosConfig());
        adAdornosCheckBox.selectedProperty().addListener((obs, oldV, newV) -> saveAdornosConfig());
        adNegritaCheckBox.selectedProperty().addListener((obs, oldV, newV) -> saveAdornosConfig());
        adSubrayadoCheckBox.selectedProperty().addListener((obs, oldV, newV) -> saveAdornosConfig());
        adUsarColoresCheckBox.selectedProperty().addListener((obs, oldV, newV) -> saveAdornosConfig());

        // Cargar todas las configuraciones
        loadConfig();
    }

    // ---------------- Configuración General ----------------
    public void loadConfig() {
        loadSetsConfig();
        loadNickCompletionConfig();
        loadConversorConfig();
        loadAdornosConfig();
        loadCorrectorConfig();
    }

    public void guardarConfiguracion() {
        saveSetsConfig();
        saveNickCompletionConfig();
        saveConversorConfig();
        saveAdornosConfig();
        saveCorrectorConfig();
    }

    // ---------------- Métodos Corrector ----------------
    private void loadCorrectorConfig() {
        try {
            if (correctorFile.exists()) {
                JAXBContext context = JAXBContext.newInstance(FormularioCorrectorConfig.class);
                Unmarshaller unmarshaller = context.createUnmarshaller();
                FormularioCorrectorConfig config = (FormularioCorrectorConfig) unmarshaller.unmarshal(correctorFile);

                crActivoCheckBox.setSelected(config.isActivo());
                crMayusculaCheckBox.setSelected(config.isMayuscula());
                crSubrayarUrlCheckBox.setSelected(config.isSubrayarUrl());
                if (config.getPalabras() != null) palabrasData.setAll(config.getPalabras());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveCorrectorConfig() {
        try {
            FormularioCorrectorConfig config = new FormularioCorrectorConfig();
            config.setActivo(crActivoCheckBox.isSelected());
            config.setMayuscula(crMayusculaCheckBox.isSelected());
            config.setSubrayarUrl(crSubrayarUrlCheckBox.isSelected());
            config.setPalabras(new ArrayList<>(palabrasData));

            JAXBContext context = JAXBContext.newInstance(FormularioCorrectorConfig.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(config, correctorFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void agregarPalabra() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Agregar palabra");
        dialog.setHeaderText("Agregar reemplazo");
        dialog.setContentText("Formato: palabra|por");
        dialog.showAndWait().ifPresent(input -> {
            String[] partes = input.split("\\|", 2);
            if (partes.length == 2) {
                FormularioCorrectorConfig.Palabra p = new FormularioCorrectorConfig.Palabra(partes[0], partes[1]);
                palabrasData.add(p);
            }
        });
    }

    private void eliminarPalabra() {
        FormularioCorrectorConfig.Palabra selected = crPalabrasTableView.getSelectionModel().getSelectedItem();
        if (selected != null) palabrasData.remove(selected);
    }

    

    // ---------------- Métodos Adornos ----------------
    private void loadAdornosConfig() {
        try {
            if (adornosFile.exists()) {
                JAXBContext context = JAXBContext.newInstance(FormularioAdornosConfig.class);
                Unmarshaller unmarshaller = context.createUnmarshaller();
                FormularioAdornosConfig config = (FormularioAdornosConfig) unmarshaller.unmarshal(adornosFile);

                adActivoCheckBox.setSelected(config.isActivo());
                adEstiloField.setText(config.getEstilo());
                adAdornosCheckBox.setSelected(config.isAdornos());
                adNegritaCheckBox.setSelected(config.isNegrita());
                adSubrayadoCheckBox.setSelected(config.isSubrayado());
                adUsarColoresCheckBox.setSelected(config.isUsarColores());
                adFicheroEstilosField.setText(config.getFicheroEstilos());
                if (config.getAdornosList() != null) adAdornosData.setAll(config.getAdornosList());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveAdornosConfig() {
        try {
            FormularioAdornosConfig config = new FormularioAdornosConfig();
            config.setActivo(adActivoCheckBox.isSelected());
            config.setEstilo(adEstiloField.getText());
            config.setAdornos(adAdornosCheckBox.isSelected());
            config.setNegrita(adNegritaCheckBox.isSelected());
            config.setSubrayado(adSubrayadoCheckBox.isSelected());
            config.setUsarColores(adUsarColoresCheckBox.isSelected());
            config.setFicheroEstilos(adFicheroEstilosField.getText());
            config.setAdornosList(new ArrayList<>(adAdornosData));

            JAXBContext context = JAXBContext.newInstance(FormularioAdornosConfig.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(config, adornosFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void loadSetsConfig() {
        try {
            if (setsConfigFile.exists()) {
                JAXBContext context = JAXBContext.newInstance(SetupSetController.SetupConfig.class);
                Unmarshaller unmarshaller = context.createUnmarshaller();
                SetupSetController.SetupConfig config = (SetupSetController.SetupConfig) unmarshaller.unmarshal(setsConfigFile);

                chkCorrectoOrtografico.setSelected(config.correctoOrtografico);
                chkConversorUnidades.setSelected(config.conversorUnidades);
                chkEncriptador.setSelected(config.encriptador);
                chkNickCompletion.setSelected(config.nickCompletion);
                chkAdornosColores.setSelected(config.adornosColores);

                txtColorMenor.setText(config.colorMenor);
                txtColorArroba.setText(config.colorArroba);
                txtColorNick.setText(config.colorNick);
                txtColorMayor.setText(config.colorMayor);

                chkMostrarNickActivo.setSelected(config.mostrarNickActivo);
                txtMiNickMenor.setText(config.miNickMenor);
                txtMiNickArroba.setText(config.miNickArroba);
                txtMiNickNick.setText(config.miNickNick);
                txtMiNickMayor.setText(config.miNickMayor);

                txtJustificacionValor.setText(String.valueOf(config.justificacionValor));
                chkIncluirArrobaMas.setSelected(config.incluirArrobaMas);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveSetsConfig() {
        try {
            SetupSetController.SetupConfig config = new SetupSetController.SetupConfig();
            config.correctoOrtografico = chkCorrectoOrtografico.isSelected();
            config.conversorUnidades = chkConversorUnidades.isSelected();
            config.encriptador = chkEncriptador.isSelected();
            config.nickCompletion = chkNickCompletion.isSelected();
            config.adornosColores = chkAdornosColores.isSelected();

            config.colorMenor = txtColorMenor.getText();
            config.colorArroba = txtColorArroba.getText();
            config.colorNick = txtColorNick.getText();
            config.colorMayor = txtColorMayor.getText();

            config.mostrarNickActivo = chkMostrarNickActivo.isSelected();
            config.miNickMenor = txtMiNickMenor.getText();
            config.miNickArroba = txtMiNickArroba.getText();
            config.miNickNick = txtMiNickNick.getText();
            config.miNickMayor = txtMiNickMayor.getText();

            config.justificacionValor = Integer.parseInt(txtJustificacionValor.getText());
            config.incluirArrobaMas = chkIncluirArrobaMas.isSelected();

            JAXBContext context = JAXBContext.newInstance(SetupSetController.SetupConfig.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(config, setsConfigFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void loadNickCompletionConfig() {
        try {
            if (nickCompletionFile.exists()) {
                JAXBContext context = JAXBContext.newInstance(FormularioNickCompletionConfig.class);
                Unmarshaller unmarshaller = context.createUnmarshaller();
                FormularioNickCompletionConfig config =
                        (FormularioNickCompletionConfig) unmarshaller.unmarshal(nickCompletionFile);

                ncActivoCheckBox.setSelected(config.isActivo());
                ncPrimerSeparadorField.setText(config.getPrimerSeparador());
                ncSegundoSeparadorField.setText(config.getSegundoSeparador());
                if (config.getNickList() != null) 
                    ncNickListView.setItems(FXCollections.observableArrayList(config.getNickList()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveNickCompletionConfig() {
        try {
            FormularioNickCompletionConfig config = new FormularioNickCompletionConfig();
            config.setActivo(ncActivoCheckBox.isSelected());
            config.setPrimerSeparador(ncPrimerSeparadorField.getText());
            config.setSegundoSeparador(ncSegundoSeparadorField.getText());
            config.setNickList(new ArrayList<>(ncNickListView.getItems()));

            JAXBContext context = JAXBContext.newInstance(FormularioNickCompletionConfig.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(config, nickCompletionFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void loadConversorConfig() {
        try {
            if (conversorFile.exists()) {
                JAXBContext context = JAXBContext.newInstance(FormularioConversorConfig.class);
                Unmarshaller unmarshaller = context.createUnmarshaller();
                FormularioConversorConfig config =
                        (FormularioConversorConfig) unmarshaller.unmarshal(conversorFile);

                cvActivoCheckBox.setSelected(config.isActivo());
                cvCantidadMedidaField.setText(config.getCantidadMedida());
                cvEuroDolaresField.setText(config.getEuroDolares());
                cvResultadoLabel.setText(config.getResultado());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveConversorConfig() {
        try {
            FormularioConversorConfig config = new FormularioConversorConfig();
            config.setActivo(cvActivoCheckBox.isSelected());
            config.setCantidadMedida(cvCantidadMedidaField.getText());
            config.setEuroDolares(cvEuroDolaresField.getText());
            config.setResultado(cvResultadoLabel.getText());

            JAXBContext context = JAXBContext.newInstance(FormularioConversorConfig.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(config, conversorFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}

