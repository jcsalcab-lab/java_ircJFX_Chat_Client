package java_irc_chat_client;

import javafx.fxml.FXML;
import javafx.scene.control.*;

public class AdornosController {

    @FXML
    private CheckBox activoCheckBox;

    @FXML
    private TextField estiloField;

    @FXML
    private Button estilosTextoButton;

    @FXML
    private Button estilosColorButton;

    @FXML
    private CheckBox adornosCheckBox;

    @FXML
    private TextField textField1;

    @FXML
    private TextField textField2;

    @FXML
    private CheckBox negritaCheckBox;

    @FXML
    private CheckBox subrayadoCheckBox;

    @FXML
    private CheckBox usarColoresCheckBox;

    @FXML
    private Button ojoButton;

    @FXML
    private Button botonExclamacion;

    @FXML
    private Button masEstilosButton;

    @FXML
    private TextField ficheroEstilosField;

    @FXML
    private ListView<String> adornosListView;

    @FXML
    private Button agregarButton;

    @FXML
    private Button eliminarButton;

    @FXML
    public void initialize() {
        // Ejemplos de acción
        estilosTextoButton.setOnAction(e -> System.out.println("Estilos de texto clickeado"));
        estilosColorButton.setOnAction(e -> System.out.println("Estilos de color clickeado"));
        ojoButton.setOnAction(e -> System.out.println("Botón Ojo clickeado"));
        botonExclamacion.setOnAction(e -> System.out.println("Botón ! clickeado"));
        masEstilosButton.setOnAction(e -> System.out.println("Más estilos clickeado"));

        agregarButton.setOnAction(e -> {
            adornosListView.getItems().add("Nuevo Adorno");
        });

        eliminarButton.setOnAction(e -> {
            String seleccionado = adornosListView.getSelectionModel().getSelectedItem();
            if (seleccionado != null) {
                adornosListView.getItems().remove(seleccionado);
            }
        });
    }
}
