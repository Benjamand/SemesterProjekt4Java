package group3.component.common.API;

import java.io.IOException;

public interface IInstructionAPIProcessingService {

    String commandAGV(String move, String location) throws IOException;

    String pickWarehouseItem(String id) throws IOException;

    String insertWarehouseItem(String id, String itemName) throws IOException;
}
