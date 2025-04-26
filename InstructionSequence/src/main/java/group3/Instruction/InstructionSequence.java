package group3.Instruction;

import group3.component.common.InstructionSequence.IInstructionSequenceProcessingService;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.util.Duration;


public class InstructionSequence implements IInstructionSequenceProcessingService {

    //Observablelist det skal opdateres når der tilføges api calls.
    //check også agv gui da den også indeholder et dele af Sequence.

    private final ObservableList<String> queue = FXCollections.observableArrayList();
    private final IntegerProperty currentIndex = new SimpleIntegerProperty(0);
    private final BooleanProperty isRunning = new SimpleBooleanProperty(false);
    private Timeline productionTimeline;

    public ObservableList<String> getQueue() {
        return queue;
    }

    public boolean isRunning() {
        return isRunning.get();
    }


    //basic manipulering af sequence
    public void addInstruction(String instruction) {
        if (!isRunning()) {
            queue.add(instruction);
            System.out.println("Instruction added: " + instruction);
        } else {
            System.out.println("Cannot modify queue while production is running.");
        }
    }

    public void removeInstruction(String instruction) {
        if (!isRunning()) {
            queue.remove(instruction);
            System.out.println("Instruction removed: " + instruction);
        } else {
            System.out.println("Cannot modify queue while production is running.");
        }
    }

    public void clearQueue(Label currentInstructionLabel) {
        if (!isRunning()) {
            queue.clear();
            System.out.println("Queue cleared.");
            currentInstructionLabel.setText("Current Instruction: None");
        } else {
            System.out.println("Cannot clear queue while production is running.");
        }
    }

    public void startProduction(Label currentInstructionLabel, Button... buttonsToDisable) {
        if (queue.isEmpty()) {
            System.out.println("Queue is empty. Nothing to process.");
            return;
        }

        if (isRunning()) {
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
                currentIndex.set((currentIndex.get() + 1) % queue.size());
            }
        }));
        productionTimeline.setCycleCount(Timeline.INDEFINITE);
        productionTimeline.play();
    }

    public void stopProduction(Button... buttonsToEnable) {
        if (productionTimeline != null) {
            productionTimeline.stop();
            isRunning.set(false);
            for (Button b : buttonsToEnable) b.setDisable(false);
            System.out.println("Production stopped manually.");
        }
    }
}
