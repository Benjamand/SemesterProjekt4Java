import group3.component.common.InstructionSequence.IInstructionSequenceProcessingService;

module InstructionSequence {
    requires javafx.controls;
    requires CommonInstructionSequence;
    requires CommonAPI;
    exports group3.Instruction;
    provides IInstructionSequenceProcessingService with group3.Instruction.InstructionSequence;
}