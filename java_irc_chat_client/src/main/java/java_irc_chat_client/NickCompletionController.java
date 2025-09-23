package java_irc_chat_client;

import javafx.fxml.FXML;
import javafx.scene.control.*;

public class NickCompletionController {

    @FXML
    private CheckBox activoCheckBox;

    @FXML
    private Label nickLabel;

    @FXML
    private Button separadorButton;

    @FXML
    private TextField primerSeparadorField;

    @FXML
    private Button textoButton;

    @FXML
    private TextField segundoSeparadorField;

    @FXML
    private Button guardarEstiloButton;

    @FXML
    private ListView<String> nickListView;

    @FXML
    private Button eliminarButton;

    @FXML
    public void initialize() {
        // Ejemplo de acción: botón guardar
        guardarEstiloButton.setOnAction(e -> guardarEstilo());
        separadorButton.setOnAction(e -> agregarSeparador());
        textoButton.setOnAction(e -> agregarTexto());
        eliminarButton.setOnAction(e -> eliminarElemento());
    }

    private void guardarEstilo() {
        // Lógica de guardado
        System.out.println("Guardar estilo clickeado");
    }

    private void agregarSeparador() {
        // Lógica de agregar separador
        System.out.println("Agregar separador clickeado");
    }

    private void agregarTexto() {
        // Lógica de agregar texto
        System.out.println("Agregar texto clickeado");
    }

    private void eliminarElemento() {
        String seleccionado = nickListView.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            nickListView.getItems().remove(seleccionado);
        }
    }
}
