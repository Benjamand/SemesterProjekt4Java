module Core {
     uses group3.component.common.services.IGUIProcessingService;
     uses group3.component.common.InstructionSequence.IInstructionSequenceProcessingService;
     uses group3.component.common.API.IWarehouseAPIProcessingService;
     uses group3.component.common.API.IInstructionAPIProcessingService;
     requires javafx.controls;
     requires Common;
     requires CommonInstructionSequence;
     requires CommonAPI;
     requires spring.context;
     exports group3.component;
}