module APIHandler {
    requires Core;
    requires com.google.gson;
    requires CommonAPI;
    exports group3.apihandler;

    provides group3.component.common.API.IWarehouseAPIProcessingService with group3.apihandler.App;
}