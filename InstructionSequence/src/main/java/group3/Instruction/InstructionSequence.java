package group3.Instruction;

import group3.component.common.API.IInstructionAPIProcessingService;
import group3.component.common.InstructionSequence.*;
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
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Map;
import java.util.function.Consumer;


public class InstructionSequence implements IInstructionSequenceProcessingService {

    //Observablelist det skal opdateres når der tilføges api calls.
    //check også agv gui da den også indeholder et dele af Sequence.

    private final ObservableList<String> visualQueue = FXCollections.observableArrayList();
    private final ArrayList<Instruction> instructionQueue = new ArrayList<>();
    private final IntegerProperty currentIndex = new SimpleIntegerProperty(0);
    private final BooleanProperty isRunning = new SimpleBooleanProperty(false);
    private Timeline productionTimeline;


    private IInstructionAPIProcessingService service;

    private final Map<ActionType, Consumer<Instruction>> actionHandlers = new EnumMap<>(ActionType.class);
    public InstructionSequence() {
        initializeActionHandlers();
    }

    @Override
    public void setService(IInstructionAPIProcessingService service) {
        this.service = service;
    }


    private void initializeActionHandlers() {
        actionHandlers.put(ActionType.PickUp, instruction -> {
            HandleItemInstruction newInstruction = (HandleItemInstruction) instruction;
             try {
                  service.pickWarehouseItem(String.valueOf(newInstruction.getItemId()));
             } catch (IOException e) {
                  throw new RuntimeException(e);
             }
        });

        actionHandlers.put(ActionType.PutDown, instruction -> {
            HandleItemInstruction newInstruction = (HandleItemInstruction) instruction;
            try {
                service.insertWarehouseItem(String.valueOf(newInstruction.getItemId()), newInstruction.getItem());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        actionHandlers.put(ActionType.Move, instruction -> {
            MoveInstruction newInstruction = (MoveInstruction) instruction;
            try {
                service.commandAGV("move", newInstruction.getLocation().name());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public boolean isRunning() {
        return isRunning.get();
    }

    //basic manipulering af sequence
    public void addInstruction(Instruction instruction) {
        if (!isRunning()) {
            visualQueue.add(instruction.toString());
            instructionQueue.add(instruction);
            System.out.println("Instruction added: " + instruction);
        } else {
            System.out.println("Cannot modify queue while production is running.");
        }
    }

    public void removeInstruction(Instruction instruction) {
        if (!isRunning()) {
            visualQueue.remove(instruction.toString());
            instructionQueue.remove(instruction);
            System.out.println("Instruction removed: " + instruction);
        } else {
            System.out.println("Cannot modify queue while production is running.");
        }
    }

    public void clearQueue(Label currentInstructionLabel) {
        if (!isRunning()) {
            visualQueue.clear();
            instructionQueue.clear();
            System.out.println("Queue cleared.");
            currentInstructionLabel.setText("Current Instruction: None");
        } else {
            System.out.println("Cannot clear queue while production is running.");
        }
    }

    public void startProduction(Label currentInstructionLabel, Button... buttonsToDisable) {
        if (instructionQueue.isEmpty()) {
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
            if (!instructionQueue.isEmpty()) {
                Instruction instruction = instructionQueue.get(currentIndex.get());
                Consumer<Instruction> handler = actionHandlers.get(instruction.getType());
                if (handler != null) {
                    handler.accept(instruction);
                } else {
                    System.out.println("Unknown instruction type: " + instruction.getType());
                }


                currentIndex.set((currentIndex.get() + 1) % instructionQueue.size());
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

    @Override
    public ObservableList<String> getQueue() {
        return visualQueue;
    }
}
