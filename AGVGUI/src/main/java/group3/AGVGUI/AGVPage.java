package group3.AGVGUI;

import group3.component.common.API.IInstructionAPIProcessingService;
import group3.component.common.InstructionSequence.IInstructionSequenceProcessingService;
import group3.component.common.services.IGUIProcessingService;
import group3.component.common.services.IInstructionGUIProcessingService;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;




public class AGVPage extends Application implements IGUIProcessingService, IInstructionGUIProcessingService {

     IInstructionSequenceProcessingService instructionSequence;

     @Override
     public void start(Stage primaryStage) {
          primaryStage.setTitle("AGV Page");

          // Buttons
          Button startProductionButton = new Button("Start Production");
          Button stopProductionButton = new Button("Stop Production");
          Button addInstructionSequenceButton = new Button("Add Instruction");
          Button removeInstructionSequenceButton = new Button("Remove Instruction");
          Button ClearInstructionQueueButton = new Button("Clear Instruction Queue");

          // Instruction list
          ObservableList<String> instructionsAGV = FXCollections.observableArrayList("Storage", "Assembly Station", "Pick up", "Put down");
          ListView<String> instructionList = new ListView<>(instructionsAGV);
          ListView<String> instructionQueue = new ListView<>(instructionSequence.getQueue());

          // Labels
          Label titleLabel = new Label("AGV Page");
          titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 25px;");
          titleLabel.setMaxWidth(Double.MAX_VALUE);
          titleLabel.setAlignment(javafx.geometry.Pos.CENTER);

          Label subtitleLabel = new Label("Control the AGV by adding instructions to the Queue.");
          subtitleLabel.setStyle("-fx-font-weight: bold;");
          subtitleLabel.setMaxWidth(Double.MAX_VALUE);
          subtitleLabel.setAlignment(javafx.geometry.Pos.CENTER);

          Label InstructionQueue = new Label("AGV Instruction Queue");
          InstructionQueue.setStyle("-fx-font-weight: bold;");
          Label InstructionList = new Label("AGV Instruction List");
          InstructionList.setStyle("-fx-font-weight: bold;");
          Label currentInstructionLabel = new Label("Current Instruction: None");
          currentInstructionLabel.setStyle("-fx-font-weight: bold;");

          // Layouts
          VBox labelBox = new VBox(10, titleLabel, subtitleLabel);
          HBox buttonsBox = new HBox(20, startProductionButton, stopProductionButton);
          HBox buttonHBox = new HBox(20, addInstructionSequenceButton, removeInstructionSequenceButton, ClearInstructionQueueButton);
          VBox listbox = new VBox(5, InstructionList, instructionList);
          VBox queuebox = new VBox(5, InstructionQueue, instructionQueue);
          HBox listsBox = new HBox(20, listbox, queuebox);
          VBox mainLayout = new VBox(20, labelBox, buttonsBox, buttonHBox, listsBox, currentInstructionLabel);

          Scene scene = new Scene(mainLayout, 600, 400);
          primaryStage.setScene(scene);
          primaryStage.show();

          // Drag and drop from instructionList
          instructionList.setOnDragDetected(event -> {
               String selected = instructionList.getSelectionModel().getSelectedItem();
               if (selected != null) {
                    Dragboard db = instructionList.startDragAndDrop(TransferMode.COPY);
                    ClipboardContent content = new ClipboardContent();
                    content.putString(selected);
                    db.setContent(content);
                    event.consume();
               }
          });

          // Drag from queue for reordering
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

          instructionQueue.setOnDragDropped(event -> {
               Dragboard db = event.getDragboard();
               if (db.hasString() && !instructionSequence.isRunning()) {
                    instructionSequence.addInstruction(db.getString());
                    System.out.println("Instruction added via drag-and-drop (bottom): " + db.getString());
                    event.setDropCompleted(true);
               } else {
                    event.setDropCompleted(false);
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

                              if (event.getGestureSource() == instructionList) {
                                   queue.add(dropIndex, draggedItem);
                                   System.out.println("Instruction added via drag-and-drop: " + draggedItem);
                              } else if (event.getGestureSource() == instructionQueue) {
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

          // Button Actions
          addInstructionSequenceButton.setOnAction(e -> {
               String selected = instructionList.getSelectionModel().getSelectedItem();
               if (selected != null) instructionSequence.addInstruction(selected);
          });

          removeInstructionSequenceButton.setOnAction(e -> {
               String selected = instructionQueue.getSelectionModel().getSelectedItem();
               if (selected != null) instructionSequence.removeInstruction(selected);
          });

          ClearInstructionQueueButton.setOnAction(e ->
                  instructionSequence.clearQueue(currentInstructionLabel)
          );

          startProductionButton.setOnAction(e ->
                  instructionSequence.startProduction(currentInstructionLabel,
                          startProductionButton, addInstructionSequenceButton, removeInstructionSequenceButton, ClearInstructionQueueButton)
          );

          stopProductionButton.setOnAction(e ->
                  instructionSequence.stopProduction(startProductionButton, addInstructionSequenceButton, removeInstructionSequenceButton, ClearInstructionQueueButton)
          );
     }

     @Override
     public void initialize(Stage stage) {
          start(stage);

     }

     @Override
     public void initializeServices(IInstructionSequenceProcessingService instructionService, IInstructionAPIProcessingService apiService) {
          instructionSequence = instructionService;
          instructionSequence.setService(apiService);
     }

     @Override
     public Button getButton() {
          return new Button("AGV Page");
     }
}
