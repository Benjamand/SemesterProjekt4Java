package group3.component.common.API;

public interface IInstructionAPIProcessingService {

    void commandAGV(String move, String location);

    void pickWarehouseItem(String id);

    void insertWarehouseItem(String id, String itemName);
}
