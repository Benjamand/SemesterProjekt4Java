import group3.component.common.services.IGUIProcessingService;

module AGVGUI {
     requires Core;
     exports group3.AGVGUI;
     requires javafx.controls;
     requires javafx.graphics;
     requires Common;
     provides IGUIProcessingService with group3.AGVGUI.AGVPage;

}