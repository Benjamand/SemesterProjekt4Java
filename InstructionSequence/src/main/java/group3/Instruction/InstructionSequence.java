package group3.Instruction;

import group3.component.common.API.IInstructionAPIProcessingService;
import group3.component.common.InstructionSequence.IInstructionSequenceProcessingService;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.util.Duration;

import java.io.IOException;

public class InstructionSequence implements IInstructionSequenceProcessingService {

    private final ObservableList<String> queue = FXCollections.observableArrayList();
    private IInstructionAPIProcessingService apiService;
    private final IntegerProperty currentIndex = new SimpleIntegerProperty(0);
    private final BooleanProperty isRunning = new SimpleBooleanProperty(false);
    private Timeline productionTimeline;

    @Override
    public ObservableList<String> getQueue() {
        return queue;
    }

    @Override
    public boolean isRunning() {
        return isRunning.get();
    }

    @Override
    public void setService(IInstructionAPIProcessingService service) {
        this.apiService = service;
    }

    // Add an instruction to the queue
    public void addInstruction(String instruction) {
        if (!isRunning.get()) {
            queue.add(instruction);
            System.out.println("Instruction added: " + instruction);
        } else {
            System.out.println("Cannot modify queue while production is running.");
        }
    }

    // Remove an instruction from the queue
    public void removeInstruction(String instruction) {
        if (!isRunning.get()) {
            queue.remove(instruction);
            System.out.println("Instruction removed: " + instruction);
        } else {
            System.out.println("Cannot modify queue while production is running.");
        }
    }

    // Clear the queue
    public void clearQueue(Label currentInstructionLabel) {
        if (!isRunning.get()) {
            queue.clear();
            System.out.println("Queue cleared.");
            currentInstructionLabel.setText("Current Instruction: None");
        } else {
            System.out.println("Cannot clear queue while production is running.");
        }
    }

    // Start production, execute instructions in the queue
    public void startProduction(Label currentInstructionLabel, Button... buttonsToDisable) {
        if (queue.isEmpty()) {
            System.out.println("Queue is empty. Nothing to process.");
            return;
        }

        if (isRunning.get()) {
            System.out.println("Production is already running.");
            return;
        }

        isRunning.set(true);
        for (Button b : buttonsToDisable) b.setDisable(true);

        productionTimeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            if (!queue.isEmpty()) {
                String instruction = queue.get(currentIndex.get());
                System.out.println("Executing instruction: " + instruction);
                currentInstructionLabel.setText("Current Instruction: " + instruction);
                try {
                    processInstruction(instruction, currentInstructionLabel);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                currentIndex.set((currentIndex.get() + 1) % queue.size());
            }
        }));
        productionTimeline.setCycleCount(Timeline.INDEFINITE);
        productionTimeline.play();
    }

    // Stop production
    public void stopProduction(Button... buttonsToEnable) {
        if (productionTimeline != null) {
            productionTimeline.stop();
            isRunning.set(false);
            for (Button b : buttonsToEnable) b.setDisable(false);
            System.out.println("Production stopped manually.");
        }
    }

    // Process each instruction and execute corresponding API commands
    private void processInstruction(String instruction, Label currentInstructionLabel) throws IOException {
        if (instruction.startsWith("Move to")) {
            String location = instruction.replace("Move to ", "").trim();
            apiService.commandAGV("move", location);
            currentInstructionLabel.setText("Executing Move: " + location);

        } else if (instruction.startsWith("Pick up id:")) {
            String[] parts = instruction.replace("Pick up id:", "").split("at");
            String id = parts[0].trim();
            String location = parts[1].trim();

            apiService.commandAGV("move", location);
            apiService.pickWarehouseItem(id);
            currentInstructionLabel.setText("Executing Pick up: " + id + " at " + location);

        } else if (instruction.startsWith("Put down")) {
            String afterPut = instruction.replace("Put down ", "");
            String[] parts = afterPut.split("into id:|at");
            String itemName = parts[0].trim();
            String id = parts[1].trim();
            String location = parts[2].trim();

            apiService.commandAGV("move", location);
            apiService.insertWarehouseItem(id, itemName);
            currentInstructionLabel.setText("Executing Put down: " + itemName + " into " + id + " at " + location);

        } else {
            currentInstructionLabel.setText("Unknown instruction format.");
        }
    }
}