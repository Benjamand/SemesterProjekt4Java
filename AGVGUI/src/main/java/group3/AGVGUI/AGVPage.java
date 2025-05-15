package group3.AGVGUI;

import group3.component.common.API.IInstructionAPIProcessingService;
import group3.component.common.InstructionSequence.IInstructionSequenceProcessingService;
import group3.component.common.services.IGUIProcessingService;
import group3.component.common.services.IInstructionGUIProcessingService;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

public class AGVPage extends Application implements IGUIProcessingService, IInstructionGUIProcessingService {

    private IInstructionSequenceProcessingService instructionSequence;

    @Override
public void start(Stage primaryStage) {
    primaryStage.setTitle("AGV Page");

    // Buttons
    Button startProductionButton = new Button("Start Production");
    Button stopProductionButton = new Button("Stop Production");
    Button addInstructionSequenceButton = new Button("Add Instruction");
    Button removeInstructionSequenceButton = new Button("Remove Instruction");
    Button clearInstructionQueueButton = new Button("Clear Instruction Queue");

    // Style buttons
    String buttonStyle = "-fx-background-color: #555555; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10 20; -fx-border-radius: 5; -fx-background-radius: 5;";
    startProductionButton.setStyle(buttonStyle);
    stopProductionButton.setStyle(buttonStyle);
    addInstructionSequenceButton.setStyle(buttonStyle);
    removeInstructionSequenceButton.setStyle(buttonStyle);
    clearInstructionQueueButton.setStyle(buttonStyle);

    // Instruction Queue
    ListView<String> instructionQueue = new ListView<>(instructionSequence.getQueue());
    instructionQueue.setStyle("-fx-background-color: #333333; -fx-text-fill: white; -fx-font-size: 14px;");

    // Labels
    Label titleLabel = new Label("AGV Page");
    titleLabel.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 25px;");
    titleLabel.setMaxWidth(Double.MAX_VALUE);
    titleLabel.setAlignment(Pos.CENTER);

    Label subtitleLabel = new Label("Control the AGV by adding instructions to the queue.");
    subtitleLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");
    subtitleLabel.setMaxWidth(Double.MAX_VALUE);
    subtitleLabel.setAlignment(Pos.CENTER);

    Label instructionQueueLabel = new Label("AGV Instruction Queue");
    instructionQueueLabel.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 18px;");

    Label currentInstructionLabel = new Label("Current Instruction: None");
    currentInstructionLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");

    // Layouts
    VBox labelBox = new VBox(10, titleLabel, subtitleLabel);
    labelBox.setAlignment(Pos.CENTER);

    HBox buttonsBox = new HBox(20, startProductionButton, stopProductionButton);
    buttonsBox.setAlignment(Pos.CENTER);

    HBox actionButtonsBox = new HBox(20, addInstructionSequenceButton, removeInstructionSequenceButton, clearInstructionQueueButton);
    actionButtonsBox.setAlignment(Pos.CENTER);

    VBox queueBox = new VBox(10, instructionQueueLabel, instructionQueue);
    queueBox.setAlignment(Pos.CENTER);
    queueBox.setPadding(new Insets(10));
    queueBox.setStyle("-fx-background-color: #444444; -fx-border-color: #555555; -fx-border-width: 2px; -fx-border-radius: 5;");

    VBox mainLayout = new VBox(20, labelBox, buttonsBox, actionButtonsBox, queueBox, currentInstructionLabel);
    mainLayout.setAlignment(Pos.CENTER);
    mainLayout.setPadding(new Insets(20));
    mainLayout.setStyle("-fx-background-color: #222222;");

    Scene scene = new Scene(mainLayout, 700, 500);
    primaryStage.setScene(scene);
    primaryStage.show();


    instructionQueue.setOnDragDetected(event -> {
        String selected = instructionQueue.getSelectionModel().getSelectedItem();
        if (selected != null && !instructionSequence.isRunning()) {
            Dragboard db = instructionQueue.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            content.putString(selected);
            db.setContent(content);
            event.consume();
        }
    });

    instructionQueue.setOnDragOver(event -> {
        if (!instructionSequence.isRunning() && event.getGestureSource() != instructionQueue && event.getDragboard().hasString()) {
            event.acceptTransferModes(TransferMode.COPY);
        }
        event.consume();
    });




          instructionQueue.setCellFactory(new Callback<>() {
               @Override
               public ListCell<String> call(ListView<String> listView) {
                    ListCell<String> cell = new ListCell<>() {
                         @Override
                         protected void updateItem(String item, boolean empty) {
                              super.updateItem(item, empty);
                              setText(empty || item == null ? null : item);
                         }
                    };

                    cell.setOnDragOver(event -> {
                         if (!instructionSequence.isRunning() && event.getGestureSource() != cell && event.getDragboard().hasString()) {
                              event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                         }
                         event.consume();
                    });

                    cell.setOnDragDropped(event -> {
                         Dragboard db = event.getDragboard();
                         if (db.hasString() && !instructionSequence.isRunning()) {
                              String draggedItem = db.getString();
                              int dropIndex = cell.getIndex();
                              ObservableList<String> queue = instructionSequence.getQueue();

                              if (event.getGestureSource() == instructionQueue) {
                                   int draggedIndex = queue.indexOf(draggedItem);
                                   if (draggedIndex != -1 && draggedIndex != dropIndex) {
                                        queue.remove(draggedIndex);
                                        if (dropIndex > draggedIndex) dropIndex--;
                                        queue.add(dropIndex, draggedItem);
                                        System.out.println("Instruction reordered via drag-and-drop: " + draggedItem);
                                   }
                              }
                              event.setDropCompleted(true);
                         } else {
                              event.setDropCompleted(false);
                         }
                         event.consume();
                    });

                    return cell;
               }
          });

          addInstructionSequenceButton.setOnAction(e -> {
               Stage dialogStage = new Stage();
               dialogStage.initModality(Modality.APPLICATION_MODAL);
               dialogStage.setTitle("Add New Instruction");

               Label instructionLabel = new Label("Instruction:");
               ComboBox<String> instructionType = new ComboBox<>();
               instructionType.getItems().addAll("Move", "Pick up", "Put down");
               instructionType.setValue("Move");

               Label locationLabel = new Label("Location:");
               ComboBox<String> locationField = new ComboBox<>();
               locationField.getItems().addAll("Assembly Station", "Warehouse");
               locationField.setValue("Warehouse");

               Label idLabelPickUp = new Label("ID of item to pick up:");
               Label idLabelPutDown = new Label("Slot ID to place item in:");
               TextField idField = new TextField();

               Label itemLabel = new Label("Name of item to put down:");
               TextField itemField = new TextField();

               locationLabel.setVisible(true);
               locationField.setVisible(true);

               idLabelPickUp.setVisible(false);
               idLabelPutDown.setVisible(false);
               idField.setVisible(false);

               itemLabel.setVisible(false);
               itemField.setVisible(false);

               instructionType.setOnAction(event -> {
                    idLabelPickUp.setVisible(false); idLabelPickUp.setManaged(false);
                    idLabelPutDown.setVisible(false); idLabelPutDown.setManaged(false);
                    idField.setVisible(false); idField.setManaged(false);
                    itemLabel.setVisible(false); itemLabel.setManaged(false);
                    itemField.setVisible(false); itemField.setManaged(false);

                    switch (instructionType.getValue()) {
                         case "Move":
                              break;

                         case "Pick up":
                              idLabelPickUp.setVisible(true); idLabelPickUp.setManaged(true);
                              idField.setVisible(true); idField.setManaged(true);
                              break;

                         case "Put down":
                              idLabelPutDown.setVisible(true); idLabelPutDown.setManaged(true);
                              idField.setVisible(true); idField.setManaged(true);
                              itemLabel.setVisible(true); itemLabel.setManaged(true);
                              itemField.setVisible(true); itemField.setManaged(true);
                              break;
                    }
               });

               Button addButton = new Button("Add");
               Button cancelButton = new Button("Cancel");

               HBox buttons = new HBox(10, addButton, cancelButton);
               buttons.setAlignment(Pos.CENTER_RIGHT);
               VBox layout = new VBox(10,
                       instructionLabel, instructionType,
                       locationLabel, locationField,
                       idLabelPickUp, idLabelPutDown, idField,
                       itemLabel, itemField,
                       buttons
               );

               layout.setPadding(new Insets(15));

               Scene dialogScene = new Scene(layout, 400, 400);
               dialogStage.setScene(dialogScene);
               dialogStage.show();

               addButton.setOnAction(event -> {
                    String instruction = instructionType.getValue();
                    String location = locationField.getValue();
                    String fullInstruction = "";

                    switch (instruction) {
                         case "Move":
                              fullInstruction = "Move to " + location;
                              break;

                         case "Pick up":
                              String pickupId = idField.getText().trim();
                              if (pickupId.isEmpty()) {
                                   showAlert("ID missing", "You must enter the ID of an item.");
                                   return;
                              }
                              fullInstruction = "Pick up id: " + pickupId + " at " + location;
                              break;

                         case "Put down":
                              String putDownId = idField.getText().trim();
                              String itemName = itemField.getText().trim();
                              if (putDownId.isEmpty() || itemName.isEmpty()) {
                                   showAlert("Missing Information", "You must enter the ID for a slot and the name of the item.");
                                   return;
                              }
                              fullInstruction = "Put down " + itemName + " into id: " + putDownId + " at " + location;
                              break;
                    }

                    instructionSequence.addInstruction(fullInstruction);
                    dialogStage.close();
               });
               cancelButton.setOnAction(event -> dialogStage.close());
          });

          removeInstructionSequenceButton.setOnAction(e -> {
               String selected = instructionQueue.getSelectionModel().getSelectedItem();
               if (selected != null) instructionSequence.removeInstruction(selected);
          });

          clearInstructionQueueButton.setOnAction(e ->
          instructionSequence.clearQueue(currentInstructionLabel)
      );
      
      startProductionButton.setOnAction(e ->
          instructionSequence.startProduction(currentInstructionLabel,
                  startProductionButton, addInstructionSequenceButton, removeInstructionSequenceButton, clearInstructionQueueButton)
      );
      
      stopProductionButton.setOnAction(e ->
          instructionSequence.stopProduction(startProductionButton, addInstructionSequenceButton, removeInstructionSequenceButton, clearInstructionQueueButton)
      );
     }

     @Override
     public void initialize(Stage stage) {
          start(stage);
     }

     @Override
     public Button getButton() {
          return new Button("AGV Page");
     }

     private void showAlert(String title, String message) {
          Alert alert = new Alert(Alert.AlertType.WARNING);
          alert.setTitle(title);
          alert.setHeaderText(null);
          alert.setContentText(message);
          alert.showAndWait();
     }

     @Override
     public void initializeServices(IInstructionSequenceProcessingService instructionService, IInstructionAPIProcessingService apiService) {
          instructionSequence = instructionService;
          instructionSequence.setService(apiService);
     }

}
