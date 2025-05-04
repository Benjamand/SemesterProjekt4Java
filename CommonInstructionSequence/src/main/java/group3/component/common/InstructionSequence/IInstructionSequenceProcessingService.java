package group3.component.common.InstructionSequence;

import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public interface IInstructionSequenceProcessingService {

     ObservableList<String> getQueue();

     boolean isRunning();

     void addInstruction(String string);


     void removeInstruction(String selected);

     void clearQueue(Label currentInstructionLabel);

     void startProduction(Label currentInstructionLabel, Button... buttonsToDisable);

     void stopProduction(Button... buttonsToDisable);
}
