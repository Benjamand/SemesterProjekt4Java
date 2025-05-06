package group3.Instruction;

import group3.component.common.API.IInstructionAPIProcessingService;
import group3.component.common.InstructionSequence.IInstructionSequenceProcessingService;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.util.Duration;

import java.io.IOException;

public class InstructionSequence implements IInstructionSequenceProcessingService {

    private final ObservableList<String> instructionQueue = FXCollections.observableArrayList();
    private IInstructionAPIProcessingService apiService;
    private boolean running = false;
    private Timeline productionTimeline;

    @Override
    public ObservableList<String> getQueue() {
        return instructionQueue;
    }

    @Override
    public void setService(IInstructionAPIProcessingService service) {
        this.apiService = service;
    }

    @Override
    public boolean isRunning() {
        return running;
    }

    @Override
    public void addInstruction(String instruction) {
        instructionQueue.add(instruction);
    }

    @Override
    public void removeInstruction(String instruction) {
        instructionQueue.remove(instruction);
    }

    @Override
    public void clearQueue(Label currentInstructionLabel) {
        instructionQueue.clear();
        currentInstructionLabel.setText("Current Instruction: None");
    }

    @Override
    public void startProduction(Label currentInstructionLabel, Button... buttonsToDisable) {
        if (running || apiService == null) return;

        running = true;
        for (Button button : buttonsToDisable) button.setDisable(true);

        productionTimeline = new Timeline(
                new KeyFrame(Duration.seconds(3), event -> {
                    if (!instructionQueue.isEmpty()) {
                        processNextInstruction(currentInstructionLabel);
                    } else {
                        stopProduction(buttonsToDisable);
                        currentInstructionLabel.setText("Production complete.");
                    }
                })
        );
        productionTimeline.setCycleCount(Timeline.INDEFINITE);
        productionTimeline.play();
    }

    @Override
    public void stopProduction(Button... buttonsToDisable) {
        if (!running) return;

        running = false;
        if (productionTimeline != null) productionTimeline.stop();
        for (Button button : buttonsToDisable) button.setDisable(false);
    }

    private void processNextInstruction(Label statusLabel) {
        if (instructionQueue.isEmpty()) return;

        String instruction = instructionQueue.remove(0);
        statusLabel.setText("Executing: " + instruction);

        if (instruction.startsWith("Move to")) {
            String location = instruction.replace("Move to ", "").trim();
            apiService.commandAGV("move", location);

        } else if (instruction.startsWith("Pick up id:")) {
            String[] parts = instruction.replace("Pick up id:", "").split("at");
            String id = parts[0].trim();
            String location = parts[1].trim();

            apiService.commandAGV("move", location);
            apiService.pickWarehouseItem(id);

        } else if (instruction.startsWith("Put down")) {
            // Example: "Put down itemName into id: 3 at Warehouse"
            String afterPut = instruction.replace("Put down ", "");
            String[] parts = afterPut.split("into id:|at");
            String itemName = parts[0].trim();
            String id = parts[1].trim();
            String location = parts[2].trim();

            apiService.commandAGV("move", location);
            apiService.insertWarehouseItem(id, itemName);

        } else {
            statusLabel.setText("Unknown instruction format.");
        }
    }
}