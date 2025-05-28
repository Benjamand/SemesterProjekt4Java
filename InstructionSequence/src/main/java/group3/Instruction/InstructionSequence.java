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
import java.util.Arrays;
import java.util.List;

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

        productionTimeline = new Timeline(new KeyFrame(Duration.seconds(8), event -> {
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
        try {
            System.out.println(instruction);
            String lowerInstruction = instruction.toLowerCase();
            String command = null;
            String location = null;
            String id = null;

            if (lowerInstruction.startsWith("move to")) {
                command = "move";
                location = instruction.substring("Move to ".length()).trim();

            } else if (lowerInstruction.startsWith("pick up id:")) {
                command = "pick";
                int atIndex = instruction.toLowerCase().indexOf(" at ");
                if (atIndex == -1) throw new IllegalArgumentException("Invalid pick up instruction format.");

                id = instruction.substring("Pick up id: ".length(), atIndex).trim();
                location = instruction.substring(atIndex + 4).trim();

            } else if (lowerInstruction.startsWith("put down")) {
                command = "put";
                int intoIndex = instruction.toLowerCase().indexOf(" into id:");
                int atIndex = instruction.toLowerCase().indexOf(" at ");

                if (intoIndex == -1 || atIndex == -1) {
                    throw new IllegalArgumentException("Invalid put down instruction format.");
                }

                id = instruction.substring(intoIndex + " into id:".length(), atIndex).trim();
                location = instruction.substring(atIndex + " at ".length()).trim();
            }

            if (command == null || location == null) {
                throw new IllegalArgumentException("Unable to parse instruction.");
            }

            String normalizedLocation = location.toLowerCase();

            String response;
            if (id != null) {
                response = apiService.commandAGV(command, normalizedLocation, id);
            } else {
                response = apiService.commandAGV(command, normalizedLocation);
            }

            System.out.println("AGV response: " + response);

        } catch (Exception e) {
            System.err.println("Error processing instruction: " + instruction);
            e.printStackTrace();
        }
    }

}