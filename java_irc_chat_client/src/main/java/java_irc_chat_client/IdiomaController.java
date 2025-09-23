package java_irc_chat_client;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class IdiomaController {

    // Tabla y columnas
    @FXML
    private TableView<Idioma> tblIdiomas;

    @FXML
    private TableColumn<Idioma, String> colPrefIdioma;

    @FXML
    private TableColumn<Idioma, String> colIdioma;

    // Label de información
    @FXML
    private Label lblInfo;

    // Checkbox
    @FXML
    private CheckBox chkCambiarTema;

    // Botón
    @FXML
    private Button btnDescargarIdiomas;

    @FXML
    private void initialize() {
        // Configuración inicial de la tabla
        colPrefIdioma.setCellValueFactory(new PropertyValueFactory<>("prefIdioma"));
        colIdioma.setCellValueFactory(new PropertyValueFactory<>("idioma"));

        // Ejemplo: agregar algunos datos iniciales
        tblIdiomas.getItems().addAll(
            new Idioma("Español", "Spanish"),
            new Idioma("Inglés", "English")
        );

        // Botón de ejemplo
        btnDescargarIdiomas.setOnAction(event -> {
            lblInfo.setText("Descargando idiomas...");
            // Aquí iría la lógica real
        });

        // Checkbox de ejemplo
        chkCambiarTema.setOnAction(event -> {
            if (chkCambiarTema.isSelected()) {
                lblInfo.setText("Tema activado");
            } else {
                lblInfo.setText("Tema desactivado");
            }
        });
    }

    // Clase interna para representar un idioma
    public static class Idioma {
        private final String prefIdioma;
        private final String idioma;

        public Idioma(String prefIdioma, String idioma) {
            this.prefIdioma = prefIdioma;
            this.idioma = idioma;
        }

        public String getPrefIdioma() {
            return prefIdioma;
        }

        public String getIdioma() {
            return idioma;
        }
    }
}
