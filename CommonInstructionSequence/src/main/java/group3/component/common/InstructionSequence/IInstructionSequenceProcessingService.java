package group3.component.common.InstructionSequence;

import group3.component.common.API.IInstructionAPIProcessingService;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public interface IInstructionSequenceProcessingService {
     ObservableList<String> getQueue();

     void setService(IInstructionAPIProcessingService service);

     boolean isRunning();

     void addInstruction(String instruction);
     void removeInstruction(String instruction);

     void clearQueue(Label currentInstructionLabel);

     void startProduction(Label currentInstructionLabel, Button... buttonsToDisable);

     void stopProduction(Button... buttonsToDisable);
}
