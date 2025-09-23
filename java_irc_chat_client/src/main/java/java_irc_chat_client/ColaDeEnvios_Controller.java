package java_irc_chat_client;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class ColaDeEnvios_Controller {

    // TableView y columnas
    @FXML
    private TableView<Envio> tableColaEnvios;
    @FXML
    private TableColumn<Envio, Integer> colNumero;
    @FXML
    private TableColumn<Envio, String> colNick;
    @FXML
    private TableColumn<Envio, String> colFichero;
    @FXML
    private TableColumn<Envio, String> colHace;

    // TextFields
    @FXML
    private TextField txtMaxEnvios;
    @FXML
    private TextField txtMaxPorUsuario;
    @FXML
    private TextField txtSlotsCola;
    @FXML
    private TextField txtSlotsPorUsuario;
    @FXML
    private TextField txtSaltarCola;

    // Botones
    @FXML
    private Button btnSubir;
    @FXML
    private Button btnBajar;
    @FXML
    private Button btnBorrarLinea;
    @FXML
    private Button btnBorrarTodo;

    @FXML
    public void initialize() {
        // Inicializar columnas del TableView
        colNumero.setCellValueFactory(new PropertyValueFactory<>("numero"));
        colNick.setCellValueFactory(new PropertyValueFactory<>("nick"));
        colFichero.setCellValueFactory(new PropertyValueFactory<>("fichero"));
        colHace.setCellValueFactory(new PropertyValueFactory<>("hace"));

        // Aquí puedes agregar acciones a los botones
        btnSubir.setOnAction(event -> subirLinea());
        btnBajar.setOnAction(event -> bajarLinea());
        btnBorrarLinea.setOnAction(event -> borrarLinea());
        btnBorrarTodo.setOnAction(event -> borrarTodo());
    }

    // Métodos de ejemplo para los botones
    private void subirLinea() {
        // Lógica para subir línea
    }

    private void bajarLinea() {
        // Lógica para bajar línea
    }

    private void borrarLinea() {
        // Lógica para borrar línea seleccionada
    }

    private void borrarTodo() {
        // Lógica para borrar todas las líneas
    }

    // Clase interna de ejemplo para representar un envío en la TableView
    public static class Envio {
        private final Integer numero;
        private final String nick;
        private final String fichero;
        private final String hace;

        public Envio(Integer numero, String nick, String fichero, String hace) {
            this.numero = numero;
            this.nick = nick;
            this.fichero = fichero;
            this.hace = hace;
        }

        public Integer getNumero() {
            return numero;
        }

        public String getNick() {
            return nick;
        }

        public String getFichero() {
            return fichero;
        }

        public String getHace() {
            return hace;
        }
    }
}

