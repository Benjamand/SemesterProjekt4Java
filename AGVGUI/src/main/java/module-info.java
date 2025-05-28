import group3.component.common.services.IGUIProcessingService;
import group3.component.common.services.IInstructionGUIProcessingService;

module AGVGUI {

     requires Common;
     requires CommonInstructionSequence;
     requires javafx.controls;
     exports group3.AGVGUI;
     provides IGUIProcessingService with group3.AGVGUI.AGVPage;
     provides IInstructionGUIProcessingService with group3.AGVGUI.AGVPage;


}

