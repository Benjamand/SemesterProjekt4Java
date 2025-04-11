package group3.AGVGUI;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class AGVPage extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("AGV Page");

        // Buttons
        Button startProductionButton = new Button("Start Production");
        Button stopProductionButton = new Button("Stop Production");
        Button addInstructionSequenceButton = new Button("Add Instruction");
        Button removeInstructionSequenceButton = new Button("Remove Instruction");
        Button ClearInstructionQueue = new Button("Clear Instruction Queue");

        // Instruction list
        ObservableList<String> instructionsAGV = FXCollections.observableArrayList("work", "go to", "eat", "sleep");
        ListView<String> instructionList = new ListView<>(instructionsAGV);
        ObservableList<String> queue = FXCollections.observableArrayList();
        ListView<String> instructionQueue = new ListView<>(queue);
        final Timeline[] productionTimeline = new Timeline[1];

        // instruction index
        final int[] currentIndex = {0};

        final boolean[] isRunning = {false};

        // Labels
        Label titleLabel = new Label("Ond cirkel \uD83D\uDE08");
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
        HBox buttonHBox = new HBox(20, addInstructionSequenceButton, removeInstructionSequenceButton, ClearInstructionQueue);
        VBox listbox = new VBox(5, InstructionList, instructionList);
        VBox queuebox = new VBox(5, InstructionQueue, instructionQueue);
        HBox listsBox = new HBox(20, listbox, queuebox);
        VBox mainLayout = new VBox(20, labelBox, buttonsBox, buttonHBox, listsBox, currentInstructionLabel);

        Scene scene = new Scene(mainLayout, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.show();

        // Button Logic
        startProductionButton.setOnAction(e -> {
            if (queue.isEmpty()) {
                System.out.println("Queue is empty. Nothing to process.");
                return;
            }

            if (productionTimeline[0] != null && productionTimeline[0].getStatus() == Timeline.Status.RUNNING) {
                System.out.println("Production is already running.");
                return;
            }

            System.out.println("Production started");
            isRunning[0] = true;

            // Disable the start, add, remove, clear while running
            startProductionButton.setDisable(true);
            addInstructionSequenceButton.setDisable(true);
            removeInstructionSequenceButton.setDisable(true);
            ClearInstructionQueue.setDisable(true);

            productionTimeline[0] = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
                if (!queue.isEmpty()) {
                    String instruction = queue.get(currentIndex[0]);
                    System.out.println("Executing instruction: " + instruction);
                    currentInstructionLabel.setText("Current Instruction: " + instruction);

                    currentIndex[0] = (currentIndex[0] + 1) % queue.size();
                } else {
                    System.out.println("Queue is unexpectedly empty.");
                }
            }));

            productionTimeline[0].setCycleCount(Timeline.INDEFINITE);
            productionTimeline[0].play();
        });

        stopProductionButton.setOnAction(e -> {
            if (productionTimeline[0] != null) {
                productionTimeline[0].stop();
                System.out.println("Production stopped manually.");
                isRunning[0] = false;


                startProductionButton.setDisable(false);
                addInstructionSequenceButton.setDisable(false);
                removeInstructionSequenceButton.setDisable(false);
                ClearInstructionQueue.setDisable(false);
            }
        });

        ClearInstructionQueue.setOnAction(e -> {
            queue.clear(); // Clears the queue
            System.out.println("Queue cleared.");
            currentInstructionLabel.setText("Current Instruction: None");
        });

        addInstructionSequenceButton.setOnAction(e -> {
            if (!isRunning[0]) {
                String selected = instructionList.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    queue.add(selected);
                    System.out.println("Instruction added: " + selected);
                }
            } else {
                System.out.println("Cannot modify queue while production is running.");
            }
        });

        removeInstructionSequenceButton.setOnAction(e -> {
            if (!isRunning[0]) {
                String selected = instructionQueue.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    queue.remove(selected);
                    System.out.println("Instruction removed: " + selected);
                }
            } else {
                System.out.println("Cannot modify queue while production is running.");
            }
        });
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}
