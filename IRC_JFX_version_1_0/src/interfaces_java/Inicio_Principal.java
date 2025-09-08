package interfaces_java;

import java.net.URL;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Inicio_Principal extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Carga el archivo FXML desde recursos (classpath)
    	
    	

    	
        Parent root = FXMLLoader.load(getClass().getResource("/interfaces_java/Principal_Main.fxml"));

        Scene scene = new Scene(root);
        primaryStage.setTitle("Mi Aplicación JavaFX");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

