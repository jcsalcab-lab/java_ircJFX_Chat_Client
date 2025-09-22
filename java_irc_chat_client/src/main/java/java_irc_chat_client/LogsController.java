package java_irc_chat_client;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class LogsController {

    @FXML private TextField searchField;
    @FXML private Button searchButton, viewButton, editButton, deleteButton;
    @FXML private TableView<File> logsTable;
    @FXML private TableColumn<File, String> nameColumn;
    @FXML private TableColumn<File, String> sizeColumn;

    private final File logsDir = new File(System.getProperty("user.dir"), "logs");

    @FXML
    public void initialize() {
        // Configurar columnas
        nameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
        sizeColumn.setCellValueFactory(data -> new SimpleStringProperty(formatSize(data.getValue().length())));

        // TableRowFactory para resaltar coincidencias y doble clic
        logsTable.setRowFactory(tv -> {
            TableRow<File> row = new TableRow<>() {
                @Override
                protected void updateItem(File file, boolean empty) {
                    super.updateItem(file, empty);
                    if (file == null || empty) {
                        setStyle("");
                    } else {
                        String query = searchField.getText().trim().toLowerCase();
                        boolean match = !query.isEmpty() &&
                                (file.getName().toLowerCase().contains(query) || containsInFile(file, query));
                        if (match) {
                            setStyle("-fx-background-color: #39FF14; -fx-text-fill: black;"); // verde fosforito
                        } else {
                            setStyle("");
                        }
                    }
                }
            };

            // Doble clic en fila
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    File selected = row.getItem();
                    abrirEnNotepad(selected);
                }
            });

            return row;
        });

        // Cargar todos los logs inicialmente
        loadLogs();
    }

    // Carga todos los logs
    private void loadLogs() {
        logsTable.getItems().clear();
        if (!logsDir.exists()) logsDir.mkdirs();

        File[] files = logsDir.listFiles((dir, name) -> name.endsWith(".log"));
        if (files != null) logsTable.getItems().addAll(files);
    }

    // Comprueba si el contenido del fichero contiene la cadena
    private boolean containsInFile(File file, String query) {
        if (query == null || query.isEmpty()) return false;
        try {
            return Files.lines(file.toPath())
                    .anyMatch(line -> line.toLowerCase().contains(query.toLowerCase()));
        } catch (IOException e) {
            return false;
        }
    }

    // Formatea el tamaño del fichero
    private String formatSize(long bytes) {
        if (bytes < 1024) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(1024));
        String pre = "KMGTPE".charAt(exp - 1) + "B";
        double size = bytes / Math.pow(1024, exp);
        return String.format("%.2f %s", size, pre);
    }

    @FXML
    private void onSearch() {
        // Solo refresca el estilo de filas según la búsqueda
        logsTable.refresh();
    }

    @FXML
    private void onView() {
        File selected = logsTable.getSelectionModel().getSelectedItem();
        if (selected == null) { showAlert("Selecciona un log para ver."); return; }

        try {
            List<String> lines = Files.readAllLines(selected.toPath());
            TextArea textArea = new TextArea(String.join("\n", lines));
            textArea.setEditable(false);

            Stage stage = new Stage();
            stage.setTitle("Ver log: " + selected.getName());
            stage.setScene(new javafx.scene.Scene(new javafx.scene.layout.BorderPane(textArea), 600, 400));
            stage.show();
        } catch (IOException e) {
            showAlert("Error al leer el log.");
        }
    }

    @FXML
    private void onEdit() {
        File selected = logsTable.getSelectionModel().getSelectedItem();
        if (selected == null) { showAlert("Selecciona un log para editar."); return; }

        try {
            if (Desktop.isDesktopSupported()) Desktop.getDesktop().edit(selected);
            else showAlert("La edición no es soportada en este sistema.");
        } catch (IOException e) {
            showAlert("Error al abrir el log para edición.");
        }
    }

    @FXML
    private void onDelete() {
        File selected = logsTable.getSelectionModel().getSelectedItem();
        if (selected == null) { showAlert("Selecciona un log para borrar."); return; }

        if (selected.delete()) logsTable.getItems().remove(selected);
        else showAlert("No se pudo borrar el log.");
    }

    private void abrirEnNotepad(File file) {
        try {
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().edit(file);
            } else {
                // Alternativa para Windows
                new ProcessBuilder("notepad.exe", file.getAbsolutePath()).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("No se pudo abrir el log en Notepad.");
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK);
        alert.showAndWait();
    }
}
