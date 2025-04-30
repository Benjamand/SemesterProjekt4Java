import group3.component.common.services.IGUIProcessingService;

module StorageGUI {
    requires Core;
    requires javafx.controls;
    requires Common;
    requires APIHandler;

    requires CommonAPI;
    uses group3.apihandler.App;
    uses group3.component.common.API.IWarehouseAPIProcessingService;

    exports group3.StorageGUI;
    provides IGUIProcessingService with group3.StorageGUI.StoragePage;
}