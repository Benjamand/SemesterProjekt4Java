package group3.StorageGUI;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Navigation extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Storage Navigation");


        Button storageExampleButton = new Button("Storage Example");
        Button knapButton = new Button("Knap");
        Button exitButton = new Button("Exit");


        storageExampleButton.setOnAction(e -> openStorageExample(primaryStage));
        exitButton.setOnAction(e -> primaryStage.close());

        VBox layout = new VBox(20);
        layout.getChildren().addAll(storageExampleButton, knapButton, exitButton);


        Scene scene = new Scene(layout, 500, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void openStorageExample(Stage stage) {
        StorageEksempel storagePage = new StorageEksempel();
        stage.setScene(storagePage.getScene(stage, this));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
