package java_irc_chat_client;

import javafx.fxml.FXML;
import javafx.scene.control.*;

public class ConversorController {

    @FXML
    private CheckBox activoCheckBox;

    @FXML
    private Label descripcionLabel;

    @FXML
    private TextField cantidadMedidaField;

    @FXML
    private Label resultadoLabel;

    @FXML
    private TextField euroDolaresField;

    @FXML
    private Button ayudaTagsButton;

    @FXML
    public void initialize() {
        // Ejemplo: acción del botón Ayuda Tags
        ayudaTagsButton.setOnAction(e -> mostrarAyudaTags());

        // Actualizar resultado al cambiar cantidad
        cantidadMedidaField.textProperty().addListener((obs, oldText, newText) -> actualizarResultado());
    }

    private void mostrarAyudaTags() {
        // Mostrar mensaje de ayuda
        System.out.println("Mostrando ayuda de tags...");
    }

    private void actualizarResultado() {
        // Lógica ejemplo para actualizar label
        String cantidad = cantidadMedidaField.getText();
        resultadoLabel.setText(cantidad + " (ejemplo de conversión)");
    }
}
