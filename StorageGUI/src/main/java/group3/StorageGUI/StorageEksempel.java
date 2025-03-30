package group3.StorageGUI;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class StorageEksempel {

     public Scene getScene(Stage stage, Navigation navigation) {
          VBox layout = new VBox(20);

          Button backButton = new Button("←");
          backButton.setOnAction(e -> navigation.start(stage));

          Label message = new Label("det virker");
          layout.getChildren().addAll(backButton, message);

          return new Scene(layout, 500, 500);
     }
}
