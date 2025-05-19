import group3.component.common.services.IGUIProcessingService;

module StorageGUI {
    requires javafx.controls;
    requires Common;
    requires CommonAPI;
    exports group3.StorageGUI;
    provides IGUIProcessingService with group3.StorageGUI.StoragePage;
}