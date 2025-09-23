package java_irc_chat_client;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

public class DCC_DCC_Controller implements Initializable {

    // 📌 ListViews
    @FXML private ListView<String> listAcepta;
    @FXML private ListView<String> listRechazar;
    @FXML private ListView<String> listNoEnviar;

    // 📌 Botones para listas
    @FXML private Button btnAddAcepta;
    @FXML private Button btnRemoveAcepta;
    @FXML private Button btnAddRechazar;
    @FXML private Button btnRemoveRechazar;
    @FXML private Button btnAddNoEnviar;
    @FXML private Button btnRemoveNoEnviar;

    // 📌 Combos
    @FXML private ComboBox<String> cbAutoAceptarChat;
    @FXML private ComboBox<String> cbAutoAceptarEnvios;

    // 📌 TextFields
    @FXML private TextField txtVelocidad;
    @FXML private TextField txtCancelaEnvio;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Inicializar combos (puedes cargarlos dinámicamente en otro método)
        // cbAutoAceptarChat.getItems().addAll("Opción 1", "Opción 2");
        // cbAutoAceptarEnvios.getItems().addAll("Opción 1", "Opción 2");

        // Handlers para botones
        btnAddAcepta.setOnAction(e -> addItemToList(listAcepta, "Nuevo Acepta"));
        btnRemoveAcepta.setOnAction(e -> removeSelectedItem(listAcepta));

        btnAddRechazar.setOnAction(e -> addItemToList(listRechazar, "Nuevo Rechazo"));
        btnRemoveRechazar.setOnAction(e -> removeSelectedItem(listRechazar));

        btnAddNoEnviar.setOnAction(e -> addItemToList(listNoEnviar, "Nuevo NoEnviar"));
        btnRemoveNoEnviar.setOnAction(e -> removeSelectedItem(listNoEnviar));

        // Inicialización de valores por defecto
        txtVelocidad.setText("0");
        txtCancelaEnvio.setText("0");
    }

    // 📌 Método genérico para añadir un item a una lista
    private void addItemToList(ListView<String> listView, String defaultValue) {
        listView.getItems().add(defaultValue);
    }

    // 📌 Método genérico para eliminar el item seleccionado
    private void removeSelectedItem(ListView<String> listView) {
        int selectedIndex = listView.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            listView.getItems().remove(selectedIndex);
        }
    }
}
