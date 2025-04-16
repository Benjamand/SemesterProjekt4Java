package group3.component.common.services;

import javafx.stage.Stage;
import javafx.scene.control.Button;

public interface IGUIProcessingService {
     void initialize(Stage stage);

     Button getButton();
}
