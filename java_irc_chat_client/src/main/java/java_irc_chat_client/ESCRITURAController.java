package java_irc_chat_client;

import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class ESCRITURAController {

    @FXML
    private TabPane escrituraTabPane;

    @FXML
    private Tab tabSets;

    @FXML
    private Tab tabNickCompletion;

    @FXML
    private Tab tabCorrector;

    @FXML
    private Tab tabConverso;

    @FXML
    private Tab tabAdornos;

    @FXML
    public void initialize() {
        System.out.println("Controlador de Escritura inicializado");

        tabSets.setOnSelectionChanged(event -> {
            if (tabSets.isSelected()) System.out.println("Subpestaña Sets seleccionada");
        });
        tabNickCompletion.setOnSelectionChanged(event -> {
            if (tabNickCompletion.isSelected()) System.out.println("Subpestaña Nick Completion seleccionada");
        });
        tabCorrector.setOnSelectionChanged(event -> {
            if (tabCorrector.isSelected()) System.out.println("Subpestaña Corrector seleccionada");
        });
        tabConverso.setOnSelectionChanged(event -> {
            if (tabConverso.isSelected()) System.out.println("Subpestaña Converso seleccionada");
        });
        tabAdornos.setOnSelectionChanged(event -> {
            if (tabAdornos.isSelected()) System.out.println("Subpestaña Adornos seleccionada");
        });
    }
}
