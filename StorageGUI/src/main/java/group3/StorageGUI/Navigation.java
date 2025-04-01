package group3.StorageGUI;

import group3.AGVGUI.AGVPage;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Navigation extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Storage Navigation");


        Button storagePageButton = new Button("Storage Page");
        Button agvPageButton = new Button("Agv Page");
        Button knapButton = new Button("Knap");
        Button exitButton = new Button("Exit");


        storagePageButton.setOnAction(e -> {
            StoragePage storagePage = new StoragePage();
            Stage storageStage = new Stage();
            try {
                storagePage.start(storageStage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        agvPageButton.setOnAction(e -> {
            AGVPage agvPage = new AGVPage();
            Stage agvStage = new Stage();
            try {
                agvPage.start(agvStage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        exitButton.setOnAction(e -> primaryStage.close());

        VBox layout = new VBox(20);
        layout.getChildren().addAll(storagePageButton, agvPageButton, knapButton, exitButton);


        Scene scene = new Scene(layout, 500, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
