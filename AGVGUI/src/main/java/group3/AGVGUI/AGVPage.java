package group3.AGVGUI;

import group3.component.common.services.IGUIProcessingService;
import javafx.application.Application;
import javafx.collections.FXCollections;
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
import javafx.stage.Stage;
import javafx.util.Callback;
import group3.Instruction.InstructionSequence;

public class AGVPage extends Application implements IGUIProcessingService {

    private final InstructionSequence instructionSequence = new InstructionSequence();

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("AGV Page");

        // Buttons
        Button startProductionButton = new Button("Start Production");
        Button stopProductionButton = new Button("Stop Production");
        Button addInstructionSequenceButton = new Button("Add Instruction");
        Button removeInstructionSequenceButton = new Button("Remove Instruction");
        Button clearInstructionQueueButton = new Button("Clear Instruction Queue");

        // Add tooltips to buttons
        startProductionButton.setTooltip(new Tooltip("Start executing the instruction queue"));
        stopProductionButton.setTooltip(new Tooltip("Stop the current production process"));
        addInstructionSequenceButton.setTooltip(new Tooltip("Add the selected instruction to the queue"));
        removeInstructionSequenceButton.setTooltip(new Tooltip("Remove the selected instruction from the queue"));
        clearInstructionQueueButton.setTooltip(new Tooltip("Clear all instructions from the queue"));

        // Instruction list
        ObservableList<String> instructionsAGV = FXCollections.observableArrayList("Storage", "Assembly station", "Pick up", "Put down");
        ListView<String> instructionList = new ListView<>(instructionsAGV);
        ListView<String> instructionQueue = new ListView<>(instructionSequence.getQueue());

        // Labels
        Label titleLabel = new Label("AGV Page");
        titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 28px;");
        titleLabel.setMaxWidth(Double.MAX_VALUE);
        titleLabel.setAlignment(Pos.CENTER);

        Label subtitleLabel = new Label("Control the AGV by adding instructions to the queue.");
        subtitleLabel.setStyle("-fx-font-size: 16px; -fx-font-style: italic;");
        subtitleLabel.setMaxWidth(Double.MAX_VALUE);
        subtitleLabel.setAlignment(Pos.CENTER);

        Label instructionQueueLabel = new Label("AGV Instruction Queue");
        instructionQueueLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        Label instructionListLabel = new Label("AGV Instruction List");
        instructionListLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        Label currentInstructionLabel = new Label("Current Instruction: None");
        currentInstructionLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        // Add tooltips to lists
        instructionList.setTooltip(new Tooltip("Drag instructions from here to the queue"));
        instructionQueue.setTooltip(new Tooltip("Drag and reorder instructions in the queue"));

        // Layouts
        VBox labelBox = new VBox(10, titleLabel, subtitleLabel);
        labelBox.setAlignment(Pos.CENTER);

        HBox buttonsBox = new HBox(15, startProductionButton, stopProductionButton);
        buttonsBox.setAlignment(Pos.CENTER);

        HBox actionButtonsBox = new HBox(15, addInstructionSequenceButton, removeInstructionSequenceButton, clearInstructionQueueButton);
        actionButtonsBox.setAlignment(Pos.CENTER);

        VBox listBox = new VBox(10, instructionListLabel, instructionList);
        listBox.setAlignment(Pos.CENTER);

        VBox queueBox = new VBox(10, instructionQueueLabel, instructionQueue);
        queueBox.setAlignment(Pos.CENTER);

        HBox listsBox = new HBox(20, listBox, queueBox);
        listsBox.setAlignment(Pos.CENTER);

        VBox mainLayout = new VBox(20, labelBox, buttonsBox, actionButtonsBox, listsBox, currentInstructionLabel);
        mainLayout.setPadding(new Insets(20));
        mainLayout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(mainLayout, 800, 600);
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
                System.out.println("Instruction added via drag-and-drop: " + db.getString());
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

        clearInstructionQueueButton.setOnAction(e -> instructionSequence.clearQueue(currentInstructionLabel));

        startProductionButton.setOnAction(e -> instructionSequence.startProduction(currentInstructionLabel,
                startProductionButton, addInstructionSequenceButton, removeInstructionSequenceButton, clearInstructionQueueButton));

        stopProductionButton.setOnAction(e -> instructionSequence.stopProduction(startProductionButton,
                addInstructionSequenceButton, removeInstructionSequenceButton, clearInstructionQueueButton));
    }

    @Override
    public void initialize(Stage stage) {
        start(stage);
    }

    @Override
    public Button getButton() {
        return new Button("AGV Page");
    }
}