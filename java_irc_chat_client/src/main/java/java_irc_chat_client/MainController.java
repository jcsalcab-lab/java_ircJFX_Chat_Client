package java_irc_chat_client;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ToolBar;

public class MainController {

	@FXML
	private ToolBar toolbarInclude;

	private ToolBarController toolbarController;

	@FXML
	public void initialize() {
	    // Obtener el controlador del FXML incluido
	    toolbarController = (ToolBarController) toolbarInclude.getProperties().get("fx:controller");

	    if (toolbarController == null) {
	        System.out.println("No se pudo obtener el controlador de ToolBar");
	    } else {
	        // Por ejemplo, puedes agregar un listener para clicks en la barra
	        toolbarInclude.setOnMouseClicked(e -> {
	            System.out.println("Click en toolbar desde MainController");
	        });
	    }
	}
}
	


