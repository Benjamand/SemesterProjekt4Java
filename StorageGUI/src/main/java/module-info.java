import group3.component.common.services.IGUIProcessingService;

module StorageGUI {
     requires Core;
    requires javafx.controls;

     requires Common;
     exports group3.StorageGUI;
     provides IGUIProcessingService with group3.StorageGUI.StoragePage;
}