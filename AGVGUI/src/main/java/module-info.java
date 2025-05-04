import group3.component.common.services.IGUIProcessingService;

module AGVGUI {
     requires Core;
     requires javafx.controls;
     requires javafx.graphics;
     requires Common;
     requires CommonInstructionSequence;
     exports group3.AGVGUI;
     provides IGUIProcessingService with group3.AGVGUI.AGVPage;


}