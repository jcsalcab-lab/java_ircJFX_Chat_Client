package java_irc_chat_client;

import javafx.fxml.FXML;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

/**
 * Controlador de la ventana Setup.
 * Centraliza la persistencia de todas las configuraciones de las pestañas.
 */
public class SetupController {

    @FXML
    private TabPane setupTabPane;

    // Referencias a todos los controladores de las pestañas
    private ColaDeEnvios_Controller colaDeEnviosController;
    private ConversorController conversorController;
    private CorrectorController correctorController;
    private DCC_DCC_Controller dcc_dcc_Controller;
    private IdiomaController idiomaController;
    private InterfazBarraController interfazBarraController;
    private InterfazCanalBarController interfazCanalBarController;
    private InterfazCartelesController interfazCartelesController;
    private InterfazOpcionesController interfazOpcionesController;
    private InterfazPanelController interfazPanelController;
    private InterfazSkinController interfazSkinController;
    private InterfazTemasController interfazTemasController;
    private InterfazVariosController interfazVariosController;
    private MediaController mediaController;
    private RedesAutoEntrarController redesAutoEntrarController;
    private RedesCanalesController redesCanalesController;
    private RedesController redesController;
    private RedesNicksController redesNicksController;
    private SetupSetController setupSetController;
    private TabAwayController tabAwayController;
    private TabGeneralController taabGeneralController;
    private TabProteccionController tabProteccionController;
    private Varios_VariosController variosVariosController;
    private VariosComandosPersonalizadosController comandosPersonalizadosController;
    private AdornosController adornosController;
    private NickCompletionController nickCompletionController;
    
    @FXML
    private void initialize() {
        System.out.println("SetupController inicializado.");
    }

    /**
     * Se debe llamar desde quien crea el Stage para capturar el evento de cierre.
     */
    public void setStage(Stage stage) {
        stage.setOnCloseRequest(event -> {
            guardarTodasConfiguraciones();
        });
    }

    /**
     * Asignar referencias a los controladores al cargar cada FXML.
     */
    public void setControllers(
            ColaDeEnvios_Controller colaDeEnviosController,
            ConversorController conversorController,
            CorrectorController correctorController,
            DCCController dccController,
            IdiomaController idiomaController,
            InterfazBarraController interfazBarraController,
            InterfazCanalBarController interfazCanalBarController,
            InterfazCartelesController interfazCartelesController,
            InterfazOpcionesController interfazOpcionesController,
            InterfazPanelController interfazPanelController,
            InterfazSkinController interfazSkinController,
            InterfazTemasController interfazTemasController,
            InterfazVariosController interfazVariosController,
            MediaController mediaController,
            RedesAutoEntrarController redesAutoEntrarController,
            RedesCanalesController redesCanalesController,
            RedesController redesController,
            RedesNicksController redesNicksController,
            SetupSetController setupSetController,
            TabAwayController taabAwayController,
            TabProteccionController tabProteccionController,
            Varios_VariosController variosVariosController,
            VariosComandosPersonalizadosController comandosPersonalizadosController,
            AdornosController adornosController,
            NickCompletionController nickCompletionController,
            TabGeneralController tabGeneralController
    ) {
        this.colaDeEnviosController = colaDeEnviosController;
        this.conversorController = conversorController;
        this.correctorController = correctorController;
        this.dcc_dcc_Controller = dcc_dcc_Controller;
        this.idiomaController = idiomaController;
        this.interfazBarraController = interfazBarraController;
        this.interfazCanalBarController = interfazCanalBarController;
        this.interfazCartelesController = interfazCartelesController;
        this.interfazOpcionesController = interfazOpcionesController;
        this.interfazPanelController = interfazPanelController;
        this.interfazSkinController = interfazSkinController;
        this.interfazTemasController = interfazTemasController;
        this.interfazVariosController = interfazVariosController;
        this.mediaController = mediaController;
        this.redesAutoEntrarController = redesAutoEntrarController;
        this.redesCanalesController = redesCanalesController;
        this.redesController = redesController;
        this.redesNicksController = redesNicksController;
        this.setupSetController = setupSetController;
        this.tabAwayController = taabAwayController;
        this.taabGeneralController = taabGeneralController;
        this.tabProteccionController = tabProteccionController;
        this.variosVariosController = variosVariosController;
        this.comandosPersonalizadosController = comandosPersonalizadosController;
        this.adornosController = adornosController;
        this.nickCompletionController = nickCompletionController;
        
    }

    /**
     * Llama a todos los métodos de persistencia de cada controlador.
     */
    private void guardarTodasConfiguraciones() {
        try {
            if (colaDeEnviosController != null) colaDeEnviosController.saveConfig();
            if (conversorController != null) conversorController.guardarConfig();
            if (correctorController != null) correctorController.guardarConfig();
            if (dcc_dcc_Controller != null) dcc_dcc_Controller.saveConfig();
            if (idiomaController != null) idiomaController.guardarConfiguracion();
            if (interfazBarraController != null) interfazBarraController.saveConfig();
            if (interfazCanalBarController != null) interfazCanalBarController.saveConfig();
            if (interfazCartelesController != null) interfazCartelesController.saveConfig();
            if (interfazOpcionesController != null) interfazOpcionesController.saveConfig();
            if (interfazPanelController != null) interfazPanelController.saveConfig();
            if (interfazSkinController != null) interfazSkinController.saveConfig();
            if (interfazTemasController != null) interfazTemasController.saveConfig();
            if (interfazVariosController != null) interfazVariosController.saveConfig();
            if (mediaController != null) mediaController.saveConfig();
            if (redesAutoEntrarController != null) redesAutoEntrarController.guardarConfiguracion();
            if (redesCanalesController != null) redesCanalesController.guardarConfiguracion();
            if (redesController != null) redesController.guardarConfig();
            if (redesNicksController != null) redesNicksController.guardarConfig();
            if (setupSetController != null) setupSetController.saveConfig();
            if (tabAwayController != null) tabAwayController.saveConfig();
            if (tabProteccionController != null) tabProteccionController.guardarConfiguracion();
            if (variosVariosController != null) variosVariosController.guardarConfig();
            if (comandosPersonalizadosController != null) comandosPersonalizadosController.guardarConfiguracion();
            if (adornosController != null) adornosController.guardarConfig();
            if (nickCompletionController != null) nickCompletionController.guardarConfig();
            if (tabProteccionController != null) tabProteccionController.guardarConfiguracion();
            if (taabGeneralController != null) taabGeneralController.guardarConfiguracion();

            System.out.println("Todas las configuraciones se han guardado correctamente.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
