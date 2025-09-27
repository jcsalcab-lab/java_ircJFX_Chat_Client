package java_irc_chat_client;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;

import java.io.File;

public class SetupSetController {

    // ----- Opciones -----
    @FXML private CheckBox chkCorrectoOrtografico;
    @FXML private CheckBox chkConversorUnidades;
    @FXML private CheckBox chkEncriptador;
    @FXML private CheckBox chkNickCompletion;
    @FXML private CheckBox chkAdornosColores;

    @FXML private Button btnTiposTamanoLetra;
    @FXML private Button btnPonerHora;

    // ----- Coloreado de nicks -----
    @FXML private Label lblColoreadoNick;

    @FXML private TextField txtColorMenor;
    @FXML private TextField txtColorArroba;
    @FXML private TextField txtColorNick;
    @FXML private TextField txtColorMayor;

    // ----- Mostrar tu nick al escribir -----
    @FXML private CheckBox chkMostrarNickActivo;
    @FXML private Label lblMiNick;

    @FXML private TextField txtMiNickMenor;
    @FXML private TextField txtMiNickArroba;
    @FXML private TextField txtMiNickNick;
    @FXML private TextField txtMiNickMayor;

    // ----- Justificación -----
    @FXML private TextField txtJustificacionValor;
    @FXML private CheckBox chkIncluirArrobaMas;

    // Archivo de configuración
    private final File configFile = new File(System.getProperty("user.home"), "setup_config.xml");

    // ---------------- Configuración interna ----------------
    @jakarta.xml.bind.annotation.XmlRootElement(name = "setupConfig")
    public static class SetupConfig {
        public boolean correctoOrtografico;
        public boolean conversorUnidades;
        public boolean encriptador;
        public boolean nickCompletion;
        public boolean adornosColores;

        public String colorMenor;
        public String colorArroba;
        public String colorNick;
        public String colorMayor;

        public boolean mostrarNickActivo;
        public String miNickMenor;
        public String miNickArroba;
        public String miNickNick;
        public String miNickMayor;

        public int justificacionValor;
        public boolean incluirArrobaMas;
    }

    // ---------------- Inicialización ----------------
    @FXML
    private void initialize() {
        lblColoreadoNick.setText("@2Nick|");
        lblMiNick.setText("@El_ArWen|");
        txtJustificacionValor.setText("27");

        loadConfig();
    }

    // ---------------- Cargar configuración ----------------
    public void loadConfig() {
        try {
            if (configFile.exists()) {
                JAXBContext context = JAXBContext.newInstance(SetupConfig.class);
                Unmarshaller unmarshaller = context.createUnmarshaller();
                SetupConfig config = (SetupConfig) unmarshaller.unmarshal(configFile);
                loadToForm(config);
            }
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    // ---------------- Aplicar valores al formulario ----------------
    private void loadToForm(SetupConfig config) {
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

    // ---------------- Guardar configuración ----------------
    @FXML
    public void guardarConfiguracion() {
        try {
            SetupConfig config = new SetupConfig();

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

            JAXBContext context = JAXBContext.newInstance(SetupConfig.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(config, configFile);

            System.out.println("Configuración guardada en " + configFile.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ---------------- Acciones de botones ----------------
    @FXML
    private void onTiposTamanoLetra() {
        System.out.println("Abrir configuración de Tipos y tamaño de letra");
    }

    @FXML
    private void onPonerHora() {
        System.out.println("Configurar timestamp activado");
    }
}
