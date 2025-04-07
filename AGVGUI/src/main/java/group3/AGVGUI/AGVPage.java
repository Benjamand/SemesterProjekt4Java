package group3.AGVGUI;

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

        //OnAction test
        startProductionButton.setOnAction(e -> {
            System.out.println("Production started");
        });

        //OnAction test
        stopProductionButton.setOnAction(e -> {
            System.out.println("Production stopped");
        });
    }



    public static void main(String[] args) {
        Application.launch(args);
    }
}
