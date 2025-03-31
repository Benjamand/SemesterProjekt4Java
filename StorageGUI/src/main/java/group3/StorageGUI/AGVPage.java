package group3.StorageGUI;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AGVPage extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("AGV Page");

        Button startProductionButton = new Button("Start Production");
        Button stopProductionButton = new Button("Stop Production");

        VBox layout = new VBox(20, startProductionButton, stopProductionButton);
        Scene scene = new Scene(layout, 600, 400);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
