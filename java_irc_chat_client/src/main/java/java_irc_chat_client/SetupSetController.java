package java_irc_chat_client;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

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

    // Método de inicialización
    @FXML
    private void initialize() {
        // Aquí puedes inicializar valores por defecto
        lblColoreadoNick.setText("@2Nick|");
        lblMiNick.setText("@El_ArWen|");
        txtJustificacionValor.setText("27");
    }

    // Ejemplo de acción con botones
    @FXML
    private void onTiposTamanoLetra() {
        System.out.println("Abrir configuración de Tipos y tamaño de letra");
    }

    @FXML
    private void onPonerHora() {
        System.out.println("Configurar timestamp activado");
    }
}
