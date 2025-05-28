module APIHandler {

    requires com.google.gson;
    requires CommonAPI;
    requires spring.context;
    exports group3.apihandler;

    provides group3.component.common.API.IWarehouseAPIProcessingService with group3.apihandler.App;
    provides group3.component.common.API.IInstructionAPIProcessingService with group3.apihandler.App;
}


